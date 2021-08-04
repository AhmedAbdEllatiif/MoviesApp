package com.ahmed.moviesapp.data.repositories

import android.util.Log
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import com.ahmed.moviesapp.data.roomdb.MovieDao
import com.ahmed.moviesapp.domain.RoomRepository
import kotlinx.coroutines.flow.Flow


class RoomRepositoryImpl(
    private val moviesDao: MovieDao
) : RoomRepository {

    /**
     * @return all movies from roomDb
     * */
    override fun getAllMovies(): Flow<List<NavMovie>> {
        return moviesDao.getAllMovies()
    }

    /**
     * To insert new navMovie or update the count of exists one
     * */
    override suspend fun insertOrUpdate(navMovie: NavMovie) {
        val loadedMovie = loadSingleMovie(navMovie.movieId)
        if (loadedMovie != null) {
            updateCount(loadedMovie)
        } else {
            insert(navMovie)
        }
    }

    /**
     * To fetch a single navMovie from the roomDb by it's id
     * */
    override suspend fun loadSingleMovie(movieId: String): NavMovie? {
        return moviesDao.loadSingleMovie(movieId)

    }


    /**
     * To insert
     * @param navMovie into roomDb
     * */
    override suspend fun insert(navMovie: NavMovie) {
        val value = moviesDao.insert(navMovie)
        Log.d(TAG, "insert: $value")
    }


    /**
     * To update the count of
     * @param navMovie
     * */
    override suspend fun updateCount(navMovie: NavMovie) {
        navMovie.movie.count = navMovie.movie.count + 1
        val movie = navMovie.movie
        moviesDao.updateCount(movie, movie.movieId)
        Log.d(TAG, "updateCount: ${movie.title}")
    }


    /**
     * To delete all movies from roomDb
     * */
    override suspend fun deleteAllMovies() {
        moviesDao.deleteAll()
    }

    override suspend fun delete(navMovie: NavMovie) {
        moviesDao.delete(navMovie)
    }

    companion object {
        private const val TAG = "RoomRepositoryImpl"
    }
}