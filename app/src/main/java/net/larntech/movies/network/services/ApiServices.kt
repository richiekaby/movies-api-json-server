package net.larntech.movies.network.services

import net.larntech.movies.model.movies.favourites.FavouriteMoviesResponse
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.model.movies.new_movies.NewMovieResponse
import retrofit2.http.*


interface ApiServices {

    @GET("movies")
    suspend fun getFavouriteMovies() : List<MoviesItem>

    @PUT("movies/{id}")
    suspend fun updateFavourite(@Path("id") id: Int, @Body moviesItem: MoviesItem) : MoviesItem

    @GET("https://api.themoviedb.org/3/search/movie")
    suspend fun getNewMovies(@Query("api_key") api_key:String, @Query("query") query:String,@Query("sort_by") sortBy:String?) : NewMovieResponse

    @POST("movies")
    suspend fun addFavourite(@Body moviesItem: MoviesItem) : MoviesItem


}