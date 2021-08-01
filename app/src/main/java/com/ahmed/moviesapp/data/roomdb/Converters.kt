package com.ahmed.moviesapp.data.roomdb

import androidx.room.TypeConverter
import com.ahmed.moviesapp.data.firebaseData.Movie
import org.json.JSONObject

class Converters {

    @TypeConverter
    fun fromMovieToString(movie: Movie): String? {
        return JSONObject().apply {
            put("movieId", movie.movieId)
            put("userId", movie.userId)
            put("title", movie.title)
            put("count", movie.count)
        }.toString()
    }

    @TypeConverter
    fun fromStringToMovie(movie: String): Movie {
        val json = JSONObject(movie)
        return Movie(
            movieId = json.getString("movieId"),
            title = json.getString("title"),
            userId = json.getString("userId"),
            count = json.getInt("count"),
        )

    }
}