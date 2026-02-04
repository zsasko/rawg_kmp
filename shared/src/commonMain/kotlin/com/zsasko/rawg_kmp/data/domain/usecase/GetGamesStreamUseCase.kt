package com.zsasko.rawg.domain.usecase

import androidx.paging.PagingData
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg_kmp.data.db.SelectedGenre
import com.zsasko.rawg_kmp.data.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single


@Single
class GetGamesStreamUseCase(
    private val gameRepository: GameRepository
) {

    fun getGames(
        selectedGenresFlow: Flow<List<SelectedGenre>>
    ): Flow<PagingData<GameResponseItem>> {
        return gameRepository.getGamesStream(
            selectedGenresFlow,
            pageSize = 20,
            enablePlaceHolders = false,
            prefetchDistance = 10,
            initialLoadSize = 20,
            maxCacheSize = 2000
        );
    }

}