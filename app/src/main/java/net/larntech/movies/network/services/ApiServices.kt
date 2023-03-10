package net.larntech.movies.network.services

import net.larntech.movies.model.movies.favourites.ConfItem
import net.larntech.movies.model.movies.favourites.FavouriteMoviesResponse
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.model.movies.new_movies.NewMovieResponse
import net.larntech.movies.model.weather.WeatherDataResponse
import retrofit2.http.*


interface ApiServices {

    @GET("movies")
    suspend fun getFavouriteMovies() : List<MoviesItem>

    @PUT("movies/{id}")
    suspend fun updateFavourite(@Path("id") id: Int, @Body moviesItem: MoviesItem) : MoviesItem

    @GET("https://api.themoviedb.org/3/search/movie")
    suspend fun getNewMovies(@Query("api_key") api_key:String, @Query("query") query:String,@Query("sort_by") sortBy:String?) : NewMovieResponse

    @GET("http://api.weatherapi.com/v1/current.json")
    suspend fun getWeatherData(@Query("key") api_key:String, @Query("q") query:String) : WeatherDataResponse

    @GET("conf")
    suspend fun getCityConf() : List<ConfItem>


    @POST("movies")
    suspend fun addFavourite(@Body moviesItem: MoviesItem) : MoviesItem


}