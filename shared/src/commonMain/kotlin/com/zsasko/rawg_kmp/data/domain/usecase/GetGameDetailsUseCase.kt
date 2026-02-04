package com.zsasko.rawg.domain.usecase

import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg_kmp.data.domain.repository.GameRepository
import org.koin.core.annotation.Single


@Single
class GetGameDetailsUseCase(
    private val gameRepository: GameRepository
) {

    suspend fun getGameDetails(
        gameId: Int
    ): NetworkResponse<GameDetailsResponse> {
        return gameRepository.getGameDetails(gameId)
    }

}