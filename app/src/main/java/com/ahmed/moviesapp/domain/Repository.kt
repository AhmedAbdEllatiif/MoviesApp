package com.ahmed.moviesapp.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ahmed.moviesapp.api.MoviesApi
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.data.MoviesPagingSource
import javax.inject.Inject

interface Repository {
    fun getMovies(page: Int): LiveData<PagingData<MovieItem>>


}