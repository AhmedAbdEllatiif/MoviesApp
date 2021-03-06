package com.ahmed.moviesapp.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.ahmed.moviesapp.api.MoviesApi
import retrofit2.HttpException
import java.io.IOException


class MoviesPagingSource(
    private val api: MoviesApi,
    private val queryPage: Int,
    private val errorData: MutableLiveData<Throwable>,
) : PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val requiredPage = params.key ?: queryPage

        return try {
            val response = api.getMostPopularMovies(requiredPage)
            val moviesList = response.movieItem
            LoadResult.Page(
                data = moviesList,
                prevKey = if (requiredPage == STARTING_PAGE_INDEX) null else requiredPage - 1,
                nextKey = if (moviesList.isEmpty()) null else requiredPage + 1
            )
        } catch (ioEx: IOException) {
            errorData.value = ioEx
            LoadResult.Error(ioEx)
        } catch (httpException: HttpException) {
            errorData.value = httpException
            LoadResult.Error(httpException)

        }

    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}