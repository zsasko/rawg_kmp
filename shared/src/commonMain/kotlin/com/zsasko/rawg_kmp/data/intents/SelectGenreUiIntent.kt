package com.zsasko.rawg_kmp.data.intents

sealed class SelectGenreUiIntent {
    data object ReloadData : SelectGenreUiIntent()
    data class ToggleSelectedGenre(val genreId: Int) : SelectGenreUiIntent()
}