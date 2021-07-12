package com.ahmed.moviesapp.api

import com.ahmed.moviesapp.data.MoviesPage
import retrofit2.http.GET

interface MoviesApi {

    companion object{
        const val BASE_URL:String = "https://api.themoviedb.org/3/"
        private const val API_KEY:String = "95d3c67ed78abad8eb45b58f51e1334d"
    }



    @GET("discover/movie?api_key=$API_KEY")
     suspend fun getMostPopularMovies(): MoviesPage




}