package com.zsasko.rawg_kmp.data.state

import com.zsasko.rawg.data.model.GameDetailsResponse


sealed class GameDetailsUiState() {
    data class Loaded(val data: GameDetailsResponse) :
        GameDetailsUiState()

    class Loading() : GameDetailsUiState()
    data class Error(val errorMessage: String? = null) : GameDetailsUiState()

}
