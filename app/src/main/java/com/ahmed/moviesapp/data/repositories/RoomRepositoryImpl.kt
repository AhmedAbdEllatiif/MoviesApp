package com.ahmed.moviesapp.data.repositories

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
     * To insert
     * @param navMovie into roomDb
     * */
    override suspend fun insert(navMovie: NavMovie) {
        moviesDao.insert(navMovie)
    }


    /**
     * To delete all movies from roomDb
     * */
    override suspend fun deleteAllMovies() {
        moviesDao.deleteAll()
    }
}