package com.ahmed.moviesapp.domain

import com.ahmed.moviesapp.data.firebaseData.Movie
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import kotlinx.coroutines.flow.Flow

interface RoomRepository
 {

    /**
     * @return all movies from roomDb
     * */
    fun getAllMovies(): Flow<List<NavMovie>>


    /**
     * To insert new navMovie or update the count of exists one
     * */
    suspend fun insertOrUpdate(navMovie: NavMovie)

    /**
     * To fetch a single navMovie from the roomDb by it's id
     * */
    suspend fun loadSingleMovie(movieId:String):NavMovie?

    /**
     * To insert
     * @param navMovie
     * */
    suspend fun insert(navMovie: NavMovie)

    /**
     * To update the count
     * */
    suspend fun updateCount(movie: Movie)

    /**
     * To delete all movies from roomDb
     * */
    suspend fun deleteAllMovies()

}