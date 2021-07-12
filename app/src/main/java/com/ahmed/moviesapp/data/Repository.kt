package com.ahmed.moviesapp.data

import com.ahmed.moviesapp.api.MoviesApi
import javax.inject.Inject

class Repository
@Inject constructor(
    private val api: MoviesApi
) {


    suspend fun getMovies() = api.getMostPopularMovies()

}