package com.ahmed.moviesapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.ahmed.moviesapp.api.MoviesApi
import javax.inject.Inject

class Repository
@Inject constructor(
    private val api: MoviesApi
) {


    suspend fun getMovies() = api.getMostPopularMovies(1)


    fun getMovies(page: Int) = Pager(
        config = PagingConfig(
            pageSize = 5,
            maxSize = 100,
            enablePlaceholders = false,
        ),

        pagingSourceFactory = { MoviesPagingSource(api, page) }
    ).liveData


}