package net.larntech.movies.ui.new_movie

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.larntech.movies.model.movies.favourites.FavouriteMoviesResponse
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.model.movies.new_movies.NewMovieResponse
import net.larntech.movies.network.api.ApiClient
import net.larntech.movies.util.Resource
import net.larntech.movies.util.RetrofitErrorUtil

class NewMovieRepository {

    private val apiService = ApiClient.apiServices
    suspend fun getNewMovies(api_key: String, title: String,sortBy:String?): Resource<NewMovieResponse> {
        return  withContext(Dispatchers.IO) {
            try {
                val newMovies = apiService.getNewMovies(api_key,title,sortBy)
                Resource.success(newMovies)
            } catch (e: Exception) {
                Log.e("error ", "===> "+e.localizedMessage)
                Resource.error(RetrofitErrorUtil.serverException(e))
            }
        }
    }




}