package com.ahmed.moviesapp.di

import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigator
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import com.ahmed.moviesapp.api.MoviesApi
import com.ahmed.moviesapp.data.repositories.RepositoryImpl
import com.ahmed.moviesapp.domain.Repository
import com.ahmed.moviesapp.ui.adapters.MovieDetailsAdapter
import com.ahmed.moviesapp.ui.adapters.MoviesAdapter
import com.ahmed.moviesapp.workers.UploadNavigationWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideMoviesAdapter(@Named(ApiModule.BASE_IMAGE_URL_W300) baseImageUrl: String): MoviesAdapter =
        MoviesAdapter(baseImageUrl)

    @Provides
    @Singleton
    fun provideRepository(api: MoviesApi, throwable: MutableLiveData<Throwable>): Repository =
        RepositoryImpl(api,throwable)



    @Provides
    @Singleton
    fun provideWorkerRequestBuilder():WorkRequest = PeriodicWorkRequestBuilder<UploadNavigationWorker>(
        repeatInterval = PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
        TimeUnit.MILLISECONDS,

        ).setInitialDelay(30, TimeUnit.SECONDS)
        // Additional configuration
        .build()

}