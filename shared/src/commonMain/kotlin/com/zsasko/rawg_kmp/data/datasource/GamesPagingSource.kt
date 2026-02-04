package com.zsasko.rawg_kmp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg_kmp.api.ApiService
import com.zsasko.rawg_kmp.data.db.SelectedGenre
import kotlinx.io.IOException

class GamesPagingSource(
    private val apiService: ApiService,
    private val genres: List<SelectedGenre>
) : PagingSource<Int, GameResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameResponseItem> {
        return try {
            val pageIndex = params.key ?: 1
            val selectedGenres = genres.map { it.genreId }.joinToString(",")
            val responseData = apiService.getGames(selectedGenres, pageIndex.toString())
            LoadResult.Page(
                data = responseData.results,
                prevKey = null,
                nextKey = if (responseData.next == null) null else pageIndex + 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GameResponseItem>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}