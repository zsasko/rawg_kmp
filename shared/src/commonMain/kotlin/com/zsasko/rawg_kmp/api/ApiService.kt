package com.zsasko.rawg_kmp.api

import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.GameResponse
import com.zsasko.rawg.data.model.GenreResponse
import com.zsasko.rawg_kmp.common.RAWG_API_KEY
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface ApiService {

    // adding key parameter in requests, because in defaultRequest I was not able to make it work - https://ktor.io/docs/client-default-request.html#example
    @GET("api/genres?key=${RAWG_API_KEY}")
    suspend fun getGenres(): GenreResponse

    @GET("api/games?key=${RAWG_API_KEY}")
    suspend fun getGames(
        @Query("genres") genres: String,
        @Query("page") page: String
    ): GameResponse

    @GET("api/games/{id}?key=${RAWG_API_KEY}")
    suspend fun getGameDetails(
        @Path("id") gameId: Int
    ): GameDetailsResponse

}