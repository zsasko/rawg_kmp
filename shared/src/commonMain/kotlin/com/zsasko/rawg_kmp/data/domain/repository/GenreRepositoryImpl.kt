package com.zsasko.rawg_kmp.data.domain.repository

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.zsasko.rawg.data.model.GenreResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg_kmp.api.ApiService
import com.zsasko.rawg_kmp.data.db.SelectedGenre
import com.zsasko.rawg_kmp.data.db.SelectedGenreDao
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class GenreRepositoryImpl(
    private val selectedGenreDao: SelectedGenreDao,
    //private val appDatabase: AppDatabase,
    private val apiService: ApiService,
    private val client: HttpClient,
    private val dispatcher: CoroutineDispatcher
) :
    GenreRepository {

    override fun getGenres(): Flow<NetworkResponse<GenreResponse>> = flow {
        try {
            val genreResponse = apiService.getGenres()
            emit(NetworkResponse.Success(genreResponse))
        } catch (e: Exception) {
            emit(NetworkResponse.Error(e.message.toString()))
        }
    }

    override fun getSelectedGenresFlow(): Flow<List<SelectedGenre>> {
        return selectedGenreDao.getAllFlow()
    }

    override suspend fun getSelectedGenres(): List<SelectedGenre> {
        return selectedGenreDao.getAll()
    }

    override suspend fun toggleGenreSelection(genreId: Int) {
        withContext(dispatcher) {
            selectedGenreDao.toggle(SelectedGenre(genreId))
        }
    }

    @NativeCoroutines
    override suspend fun hasSelectedGenres(): Boolean {
        return selectedGenreDao.getCount() > 0
    }

}