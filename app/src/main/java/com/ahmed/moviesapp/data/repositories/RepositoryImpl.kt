package com.ahmed.moviesapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ahmed.moviesapp.api.MoviesApi
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.data.MoviesPagingSource
import com.ahmed.moviesapp.domain.Repository

class RepositoryImpl constructor(
    private val api: MoviesApi,
    val errorData: MutableLiveData<Throwable>
): Repository {


    override fun  getMovies(page: Int): LiveData<PagingData<MovieItem>> =  Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = false,
        ),

        pagingSourceFactory = { MoviesPagingSource(api, page,errorData) }
    ).liveData
}