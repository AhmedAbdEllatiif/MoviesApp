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

    const val BASE_IMAGE_URL_W300:String = "BASE_IMAGE_URL_W300"
    const val BASE_IMAGE_URL_W500:String = "BASE_IMAGE_URL_500"
    const val MovieDetailTitles:String = "MovieDetailTitles"


    @Provides
    @Singleton
    @Named(BASE_IMAGE_URL_W300)
    fun provideBaseImageUrlW300():String = "http://image.tmdb.org/t/p/w300"

    @Provides
    @Singleton
    @Named(BASE_IMAGE_URL_W500)
    fun provideBaseImageUrlW500():String = "http://image.tmdb.org/t/p/w500"


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient:OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(MoviesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun provideMoviesApi(retrofit:Retrofit): MoviesApi =
        retrofit.create(MoviesApi::class.java)

    @Provides
    @Singleton
    fun provideThrowable(): MutableLiveData<Throwable> = MutableLiveData()



    @Provides
    @Singleton
    fun provideMoviesAdapter(@Named(BASE_IMAGE_URL_W300) baseImageUrl:String): MoviesAdapter = MoviesAdapter(baseImageUrl)


    @Provides
    @Singleton
    @Named(MovieDetailTitles)
    fun provideMovieDetailTitles ():List<String> = listOf("Overview","Rate","Release Date")

    @Provides
    @Singleton
    fun provideMovieDetailsAdapter(@Named(MovieDetailTitles) titles:List<String>): MovieDetailsAdapter = MovieDetailsAdapter(titles)

}