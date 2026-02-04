package com.zsasko.rawg_kmp.viewmodel

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.zsasko.rawg_kmp.api.ApiService
import com.zsasko.rawg_kmp.data.domain.repository.GenreRepository
import com.zsasko.rawg_kmp.data.intents.GamesUiIntent
import com.zsasko.rawg_kmp.data.state.IosGameScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

// used because for swift, pagination3 is currently not supported
// (it's only supported in compose KMP shared UI way of project setup)
@KoinViewModel
class IosGamesViewModel(
    private val genreRepository: GenreRepository,
    private val apiService: ApiService
) :
    ViewModel() {

    private val _gamesUiState =
        MutableStateFlow<IosGameScreenState>(IosGameScreenState.Loading(emptyList(), 0))

    @NativeCoroutinesState
    val gamesUiState: StateFlow<IosGameScreenState> =
        _gamesUiState.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = IosGameScreenState.Loading(emptyList(), 0)
        )

    init {
        observeGenreChanges()
    }

    private fun observeGenreChanges() {
        viewModelScope.launch {
            genreRepository.getSelectedGenresFlow()
                .collect { selectedGenres ->
                    // Reset and reload when genres change
                    resetAndReloadData()
                }
        }
    }

    private fun resetAndReloadData() {
        _gamesUiState.value = IosGameScreenState.Loading(emptyList(), 0)
        getNextPage()
    }

    fun canLoadNextPage(): Boolean {
        (gamesUiState.value as? IosGameScreenState.Loading)?.let {
            return (it.items.count() == 0)
        }
        return (gamesUiState.value as? IosGameScreenState.Success)?.hasMore ?: false
    }

    private fun getNextPage() {
        if (!canLoadNextPage()) {
            return
        }
        val currrentState = _gamesUiState.value
        viewModelScope.launch {
            try {
                // display loading overlay only 1 time when content is being loaded or when error happened
                if (currrentState.currentPage == 0 || currrentState is IosGameScreenState.Error) {
                    _gamesUiState.value = IosGameScreenState.Loading(
                        items = currrentState.items,
                        currentPage = currrentState.currentPage
                    )
                }
                val pageToLoad = currrentState.currentPage + 1
                val genres = genreRepository.getSelectedGenres()
                val selectedGenres = genres.map { it.genreId }.joinToString(",")
                val response = apiService.getGames(selectedGenres, pageToLoad.toString())
                val newItems = currrentState.items + response.results
                val hasMore = response.next != null
                _gamesUiState.value = IosGameScreenState.Success(
                    currentPage = pageToLoad,
                    items = newItems,
                    hasMore = hasMore
                )
            } catch (e: Exception) {
                _gamesUiState.value = IosGameScreenState.Error(
                    items = currrentState.items,
                    currentPage = currrentState.currentPage,
                    errorMessage = e.message ?: ""
                )
            }

        }
    }

    fun handleIntent(intent: GamesUiIntent) {
        if (intent is GamesUiIntent.LoadGames) {
            getNextPage()
        }
    }

}