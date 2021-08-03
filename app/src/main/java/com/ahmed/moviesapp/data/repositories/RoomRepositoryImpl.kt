package com.ahmed.moviesapp.data.repositories

import android.util.Log
import com.ahmed.moviesapp.data.firebaseData.Movie
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
            updateCount(loadedMovie.movie)
        } else {
            insert(navMovie)
        }
    }

    /**
     * To fetch a single navMovie from the roomDb by it's id
     * */
    override suspend fun loadSingleMovie(movieId: String): NavMovie? {
        val navMovie = moviesDao.loadSingleMovie(movieId)
        if (navMovie != null) {
            navMovie.movie.count = navMovie.movie.count + 1
            return navMovie
        }
        return null
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
     * @param movie
     * */
    override suspend fun updateCount(movie: Movie) {
        moviesDao.updateCount(movie, movie.movieId)
        Log.d(TAG, "updateCount: ${movie.title}")
    }


    /**
     * To delete all movies from roomDb
     * */
    override suspend fun deleteAllMovies() {
        moviesDao.deleteAll()
    }


    companion object {
        private const val TAG = "RoomRepositoryImpl"
    }
}