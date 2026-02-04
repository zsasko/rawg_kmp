package com.zsasko.rawg_kmp.viewmodel

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.data.model.toChecked
import com.zsasko.rawg_kmp.data.domain.repository.GenreRepository
import com.zsasko.rawg_kmp.data.intents.SelectGenreUiIntent
import com.zsasko.rawg_kmp.data.state.SelectGenreUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectGenreViewModel(private val genreRepository: GenreRepository) :
    ViewModel() {

    private val updateGenres = MutableStateFlow<Boolean>(false)


    @NativeCoroutinesState
    @OptIn(ExperimentalCoroutinesApi::class)
    val genresAll: StateFlow<SelectGenreUiState> = updateGenres.flatMapLatest {
        combine(
            updateGenres, genreRepository.getSelectedGenresFlow(),
            genreRepository.getGenres()
        ) { _, selectedGenres, allGenres ->
            when (allGenres) {
                is NetworkResponse.Success -> {
                    SelectGenreUiState.Success(
                        allGenres.data.results.toChecked(
                            selectedGenres
                        )
                    )
                }

                is NetworkResponse.Error -> {
                    SelectGenreUiState.Error(allGenres.errorMessage)
                }
            }
        }
    }
        .onStart {
            emit(SelectGenreUiState.Loading)
        }
        .catch {
            emit(SelectGenreUiState.Error(it.message.toString()))
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SelectGenreUiState.Loading
        )

    fun handleIntent(intent: SelectGenreUiIntent) {
        when (intent) {
            is SelectGenreUiIntent.ReloadData -> {
                updateGenres.update { !it }
            }

            is SelectGenreUiIntent.ToggleSelectedGenre -> {
                toggleGenreSelection(intent.genreId)
            }
        }
    }

    private fun toggleGenreSelection(genreId: Int) {
        viewModelScope.launch {
            genreRepository.toggleGenreSelection(genreId)
        }
    }
}