package com.ahmed.moviesapp.di

import androidx.lifecycle.MutableLiveData
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import com.ahmed.moviesapp.Utils
import com.ahmed.moviesapp.api.MoviesApi
import com.ahmed.moviesapp.data.repositories.RepositoryImpl
import com.ahmed.moviesapp.domain.Repository
import com.ahmed.moviesapp.ui.adapters.MoviesAdapter
import com.ahmed.moviesapp.workers.UploadNavigationWorker

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideThrowable(): MutableLiveData<Throwable> = MutableLiveData()

    @Provides
    @Singleton
    fun provideMoviesAdapter(@Named(Utils.BASE_IMAGE_URL_W300) baseImageUrl: String): MoviesAdapter =
        MoviesAdapter(baseImageUrl)

    @Provides
    @Singleton
    fun provideRepository(api: MoviesApi, throwable: MutableLiveData<Throwable>): Repository =
        RepositoryImpl(api, throwable)


    @Provides
    @Singleton
    fun provideUploadWorkerConstraints(): Constraints =
        Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

    @Provides
    @Singleton
    fun provideWorkerRequestBuilder(constraints: Constraints): WorkRequest =
        PeriodicWorkRequestBuilder<UploadNavigationWorker>(
            repeatInterval = PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MILLISECONDS,
            flexTimeInterval = PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
            TimeUnit.MILLISECONDS
        )
            .setInitialDelay(30, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .addTag(Utils.UPLOAD_WORKER_TAG)
            // Additional configuration
            .build()

}