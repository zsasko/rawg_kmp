package com.zsasko.rawg_kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zsasko.rawg.domain.usecase.GetGamesStreamUseCase
import com.zsasko.rawg_kmp.data.domain.repository.GenreRepository
import com.zsasko.rawg_kmp.data.intents.GamesUiIntent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class GamesViewModel(
    genreRepository: GenreRepository,
    getGamesUseCase: GetGamesStreamUseCase
) :
    ViewModel() {

    private val selectedGenres = genreRepository
        .getSelectedGenresFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    val games = getGamesUseCase
        .getGames(selectedGenres)
        .cachedIn(viewModelScope)


    private val _viewEvent = MutableSharedFlow<GamesUiIntent>()
    val viewEvent = _viewEvent.asSharedFlow()

    fun handleIntent(intent: GamesUiIntent) {
        if (intent is GamesUiIntent.LoadGames) {
            reloadGames()
        }
    }

    private fun reloadGames() {
        viewModelScope.launch {
            _viewEvent.emit(GamesUiIntent.LoadGames)
        }
    }
}