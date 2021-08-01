package com.ahmed.moviesapp.di

import android.app.Application
import androidx.room.Room
import com.ahmed.moviesapp.data.repositories.RoomRepositoryImpl
import com.ahmed.moviesapp.data.roomdb.MovieDao
import com.ahmed.moviesapp.data.roomdb.MovieDatabase
import com.ahmed.moviesapp.domain.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {


    @Provides
    @Singleton
    fun provideDatabase(app: Application) : MovieDatabase =
        Room.databaseBuilder(app,MovieDatabase::class.java,"Movie_Database_name")
            .build()

    @Provides
    @Singleton
    fun provideMovieDao(db:MovieDatabase): MovieDao = db.movieDao()

    @Provides
    @Singleton
    fun provideRoomRepository(movieDao:MovieDao): RoomRepository = RoomRepositoryImpl(movieDao)


}