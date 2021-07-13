package com.ahmed.moviesapp.data

import com.google.gson.annotations.SerializedName

/**
 * MoviesPage is the current page with some result of movies
 * */
data class MoviesPage(
    @SerializedName("results")
    val movieItem: List<MovieItem>,

    @SerializedName("page")
    val current_page: Int = 0,

    @SerializedName("total_pages")
    val pages_count: Int = 0,

    @SerializedName("total_results")
    val total_movies_count: Int = 0
)


/**
 * MoviesList is the result a list movies of every single page
 * */
data class MovieItem(
    val id: Int = 0,

    val original_title: String = "",

    val title: String = "",

    @SerializedName("poster_path")
    val movie_poster: String = "",

    @SerializedName("overview")
    val plot_synopsis: String = "",

    @SerializedName("vote_average")
    val user_rating: Float = 0f,

    val release_date: String = ""
)