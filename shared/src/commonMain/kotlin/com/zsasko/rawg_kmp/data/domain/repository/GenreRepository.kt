package com.zsasko.rawg_kmp.data.domain.repository

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.zsasko.rawg.data.model.GenreResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg_kmp.data.db.SelectedGenre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    fun getGenres(): Flow<NetworkResponse<GenreResponse>>
    fun getSelectedGenresFlow(): Flow<List<SelectedGenre>>
    suspend fun getSelectedGenres(): List<SelectedGenre>

    @NativeCoroutines
    suspend fun toggleGenreSelection(genreId: Int)

    @NativeCoroutines
    suspend fun hasSelectedGenres(): Boolean
}