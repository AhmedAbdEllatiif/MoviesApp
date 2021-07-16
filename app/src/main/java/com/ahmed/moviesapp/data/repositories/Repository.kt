package com.ahmed.moviesapp.data.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.ahmed.moviesapp.api.MoviesApi
import com.ahmed.moviesapp.data.MoviesPagingSource
import javax.inject.Inject

class Repository
@Inject constructor(
    private val api: MoviesApi,
     val errorData: MutableLiveData<Throwable>
) {


    fun getMovies(page: Int) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = false,
        ),

        pagingSourceFactory = { MoviesPagingSource(api, page,errorData) }
    ).liveData


}