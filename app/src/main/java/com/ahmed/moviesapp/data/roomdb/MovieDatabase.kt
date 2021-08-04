package com.ahmed.moviesapp.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.data.firebaseData.NavMovie


@Database(entities = [NavMovie::class],version = 4,exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
     abstract fun movieDao(): MovieDao
}