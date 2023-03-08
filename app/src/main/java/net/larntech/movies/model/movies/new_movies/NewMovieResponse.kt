package net.larntech.movies.model.movies.new_movies

import com.google.gson.annotations.SerializedName
import net.larntech.movies.model.movies.favourites.MoviesItem

data class NewMovieResponse(@SerializedName("page")
                            val page: Int = 0,
                            @SerializedName("total_pages")
                            val totalPages: Int = 0,
                            @SerializedName("results")
                            val movies: List<MoviesItem>?,
                            @SerializedName("total_results")
                            val totalResults: Int = 0)