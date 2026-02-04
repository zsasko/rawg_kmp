package com.zsasko.rawg_kmp.di.modules

//import com.zsasko.rawg_kmp.api.createApiService

import com.zsasko.rawg_kmp.api.ApiService
import com.zsasko.rawg_kmp.api.createApiService
import com.zsasko.rawg_kmp.common.RAWG_BASE_URL
import com.zsasko.rawg_kmp.data.db.SelectedGenreDao
import com.zsasko.rawg_kmp.data.domain.repository.GenreRepository
import com.zsasko.rawg_kmp.data.domain.repository.GenreRepositoryImpl
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class NetworkModule {

    @Single
    fun provideHttpClient(): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("KtorClient: " + message)
                }
            }
        }
        defaultRequest {
            url {
                // adding key parameter in [ApiService.kt] requests,
                // because in defaultRequest I was not able to make it work - https://ktor.io/docs/client-default-request.html#example

                //parametersOf("test", "abc123")
                //parameters.append("token", "abc123")
            }
        }
    }

    @Single
    fun provideKtorfit(client: HttpClient): Ktorfit =
        Ktorfit.Builder()
            .baseUrl(RAWG_BASE_URL)
            .httpClient(client)
            .build()

    @Single
    fun provideApiService(ktorfit: Ktorfit): ApiService {
        return ktorfit.createApiService()
    }

    @Single
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO


    @Single
    fun provideApiService(
        selectedGenreDao: SelectedGenreDao,
        apiService: ApiService,
        client: HttpClient,
        dispa: CoroutineDispatcher
    ): GenreRepository {
        return GenreRepositoryImpl(selectedGenreDao, apiService, client, dispa)
    }

}