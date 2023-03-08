package net.larntech.movies.model.movies.favourites

import com.google.gson.annotations.SerializedName

data class FavouriteMoviesResponse(@SerializedName("movies")
                                  val movies: List<MoviesItem>?,
                                   @SerializedName("conf")
                                  val conf: List<ConfItem>?)