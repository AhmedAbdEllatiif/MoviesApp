package com.ahmed.moviesapp.api

import com.ahmed.moviesapp.data.MoviesPage
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    companion object{
        const val BASE_URL:String = "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL_W300:String = "http://image.tmdb.org/t/p/w300"
        const val BASE_IMAGE_URL_W500:String = "http://image.tmdb.org/t/p/w500"
        private const val API_KEY:String = "95d3c67ed78abad8eb45b58f51e1334d"
    }



    @GET("discover/movie?api_key=$API_KEY")
     suspend fun getMostPopularMovies(
        @Query("page") page:Int
     ): MoviesPage




}