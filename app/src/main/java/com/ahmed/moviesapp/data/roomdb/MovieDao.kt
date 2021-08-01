package com.ahmed.moviesapp.data.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieTable")
    fun getAllMovies(): Flow<List<NavMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(navMovie: NavMovie)

    @Query("DELETE FROM MovieTable")
    suspend fun deleteAll()

}