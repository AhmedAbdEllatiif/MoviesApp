package com.ahmed.moviesapp.domain

import com.ahmed.moviesapp.data.firebaseData.NavMovie
import kotlinx.coroutines.flow.Flow

interface RoomRepository
 {


    /**
     * @return all movies from roomDb
     * */
    fun getAllMovies(): Flow<List<NavMovie>>

    /**
     * To insert
     * @param navMovie
     * */
    suspend fun insert(navMovie: NavMovie)


    /**
     * To delete all movies from roomDb
     * */
    suspend fun deleteAllMovies()

}