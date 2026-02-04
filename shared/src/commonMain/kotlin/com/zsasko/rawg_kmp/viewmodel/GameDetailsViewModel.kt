package com.zsasko.rawg_kmp.viewmodel


import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.domain.usecase.GetGameDetailsUseCase
import com.zsasko.rawg_kmp.data.intents.GamesDetailsUiIntent
import com.zsasko.rawg_kmp.data.state.GameDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class GameDetailsViewModel(
    val gameId: Int, val getGameDetailsUseCase: GetGameDetailsUseCase
) : ViewModel() {

    private val _gameDetailsUiState =
        MutableStateFlow<GameDetailsUiState>(GameDetailsUiState.Loading())

    @NativeCoroutinesState
    val gameDetailsUiState: StateFlow<GameDetailsUiState> = _gameDetailsUiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            GameDetailsUiState.Loading()
        )

    init {
        loadData(gameId)
    }

    fun handleIntent(intent: GamesDetailsUiIntent) {
        if (intent is GamesDetailsUiIntent.LoadGameDetails) {
            loadData(gameId)
        }
    }

    private fun loadData(gameId: Int) {
        viewModelScope.launch {
            _gameDetailsUiState.value = GameDetailsUiState.Loading()
            val gameDetailsResponse = getGameDetailsUseCase.getGameDetails(gameId)
            when (gameDetailsResponse) {
                is NetworkResponse.Success -> {
                    _gameDetailsUiState.value =
                        GameDetailsUiState.Loaded(gameDetailsResponse.data)
                }

                is NetworkResponse.Error -> {
                    _gameDetailsUiState.value =
                        GameDetailsUiState.Error(gameDetailsResponse.errorMessage)
                }
            }
        }
    }
}