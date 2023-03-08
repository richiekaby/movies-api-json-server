package net.larntech.movies.network.services

import net.larntech.movies.model.movies.favourites.FavouriteMoviesResponse
import net.larntech.movies.model.movies.favourites.MoviesItem
import retrofit2.http.*


interface ApiServices {

    @GET("movies")
    suspend fun getFavouriteMovies() : List<MoviesItem>

    @PUT("movies/{id}")
    suspend fun updateFavourite(@Path("id") id: Int, @Body moviesItem: MoviesItem) : MoviesItem


}