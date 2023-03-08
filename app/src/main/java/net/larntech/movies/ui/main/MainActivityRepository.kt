package net.larntech.movies.ui.main

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.larntech.movies.model.movies.favourites.FavouriteMoviesResponse
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.network.api.ApiClient
import net.larntech.movies.util.Resource
import net.larntech.movies.util.RetrofitErrorUtil

class MainActivityRepository {

    private val apiService = ApiClient.apiServices
    suspend fun getFavouriteMovies(): Resource<List<MoviesItem>> {
        return  withContext(Dispatchers.IO) {
            try {
                val favouriteMovies = apiService.getFavouriteMovies()
                Resource.success(favouriteMovies)
            } catch (e: Exception) {
                Log.e("error ", "===> "+e.localizedMessage)
                Resource.error(RetrofitErrorUtil.serverException(e))
            }
        }
    }

}