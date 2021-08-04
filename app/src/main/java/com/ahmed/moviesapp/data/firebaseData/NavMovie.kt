package com.ahmed.moviesapp.data.firebaseData

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "MovieTable")
data class NavMovie(

    @PrimaryKey
    val movieId: String,
    var movie: Movie,
    val userId: String,
)

data class Movie(
    var movieId: String = "",
    var userId: String = "",
    var title: String = "",
    var count: Int = 1
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "movieId" to movieId,
            "userId" to userId,
            "title" to title,
            "count" to count,
        )
    }
}
