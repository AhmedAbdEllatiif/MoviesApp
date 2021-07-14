package com.ahmed.moviesapp.data.firebaseData


data class NavMovie(
    val movie: Movie,
    val userId: String,
    val movieId: String

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
