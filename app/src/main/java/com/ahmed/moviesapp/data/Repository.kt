package com.ahmed.moviesapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.ahmed.moviesapp.api.MoviesApi
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class Repository
@Inject constructor(
    private val api: MoviesApi
) {

    fun getMovies(page: Int) = Pager(
        config = PagingConfig(
            pageSize = 5,
            maxSize = 100,
            enablePlaceholders = false,
        ),

        pagingSourceFactory = { MoviesPagingSource(api, page) }
    ).liveData


}