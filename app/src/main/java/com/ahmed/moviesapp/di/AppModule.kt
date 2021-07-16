package com.ahmed.moviesapp.di

import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigator
import com.ahmed.moviesapp.api.MoviesApi
import com.ahmed.moviesapp.ui.adapters.MovieDetailsAdapter
import com.ahmed.moviesapp.ui.adapters.MoviesAdapter
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


    const val MovieDetailTitles:String = "MovieDetailTitles"







    @Provides
    @Singleton
    fun provideThrowable(): MutableLiveData<Throwable> = MutableLiveData()



    @Provides
    @Singleton
    fun provideMoviesAdapter(@Named(ApiModule.BASE_IMAGE_URL_W300) baseImageUrl:String): MoviesAdapter = MoviesAdapter(baseImageUrl)


    @Provides
    @Singleton
    @Named(MovieDetailTitles)
    fun provideMovieDetailTitles ():List<String> = listOf("Overview","Rate","Release Date")

    @Provides
    @Singleton
    fun provideMovieDetailsAdapter(@Named(MovieDetailTitles) titles:List<String>): MovieDetailsAdapter = MovieDetailsAdapter(titles)

}