package com.zsasko.rawg_kmp.data.domain.repository

import androidx.paging.PagingData
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg_kmp.data.db.SelectedGenre
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGamesStream(
        selectedGenresFlow: Flow<List<SelectedGenre>>,
        pageSize: Int,
        enablePlaceHolders: Boolean,
        prefetchDistance: Int,
        initialLoadSize: Int,
        maxCacheSize: Int
    ): Flow<PagingData<GameResponseItem>>

    suspend fun getGameDetails(gameId: Int): NetworkResponse<GameDetailsResponse>

}