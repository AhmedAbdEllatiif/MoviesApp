package com.ahmed.moviesapp.data.roomdb

import androidx.room.*
import com.ahmed.moviesapp.data.firebaseData.Movie
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieTable")
    fun getAllMovies(): Flow<List<NavMovie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(navMovie: NavMovie)

    @Query("SELECT COUNT() FROM MovieTable WHERE movieId = :movieId")
    suspend fun count(movieId: String): Int

    @Query("UPDATE MovieTable SET movie = :movie WHERE movieId = :movieId")
    suspend fun updateCount(movie: Movie, movieId:String)

    @Query("SELECT * FROM MovieTable WHERE movieId=:movieId ")
    suspend fun loadSingleMovie(movieId: String) : NavMovie?

    @Query("DELETE FROM MovieTable")
    suspend fun deleteAll()

}