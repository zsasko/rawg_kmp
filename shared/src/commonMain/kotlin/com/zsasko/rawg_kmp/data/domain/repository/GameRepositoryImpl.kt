package com.zsasko.rawg_kmp.data.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg_kmp.api.ApiService
import com.zsasko.rawg_kmp.data.datasource.GamesPagingSource
import com.zsasko.rawg_kmp.data.db.SelectedGenre
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single


@Single
class GameRepositoryImpl(
    private val apiService: ApiService,
    private val client: HttpClient,
    private val dispatcher: CoroutineDispatcher
) :
    GameRepository {

    override suspend fun getGameDetails(gameId: Int): NetworkResponse<GameDetailsResponse> {
        return withContext(dispatcher) {
            try {
                val response = apiService.getGameDetails(gameId)
                NetworkResponse.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse.Error(e.message.toString()) //context.getString(R.string.general_network_error_occurred))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getGamesStream(
        selectedGenresFlow: Flow<List<SelectedGenre>>,
        pageSize: Int,
        enablePlaceHolders: Boolean,
        prefetchDistance: Int,
        initialLoadSize: Int,
        maxCacheSize: Int
    ): Flow<PagingData<GameResponseItem>> {
        return selectedGenresFlow
            .distinctUntilChanged()
            //.filter { it.size > 0 }
            .flatMapLatest { genres ->
                Pager(
                    // Configure how data is loaded by passing additional properties to
                    // PagingConfig, such as prefetchDistance.
                    config = PagingConfig(
                        pageSize = pageSize,
                        enablePlaceholders = enablePlaceHolders,
                        prefetchDistance = prefetchDistance,
                        initialLoadSize = initialLoadSize,
                        maxSize = maxCacheSize
                    ),
                    pagingSourceFactory = {
                        GamesPagingSource(apiService, genres)
                    }
                ).flow
            }
    }
}
