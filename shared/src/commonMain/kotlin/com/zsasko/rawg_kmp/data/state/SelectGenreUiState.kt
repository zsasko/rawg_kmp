package com.zsasko.rawg_kmp.data.state

import com.zsasko.rawg_kmp.data.response.GenreResponseItemChecked


sealed class SelectGenreUiState {
    object Loading : SelectGenreUiState()
    data class Success(var genres: List<GenreResponseItemChecked>) : SelectGenreUiState()
    data class Error(val errorMessage: String) : SelectGenreUiState()
}