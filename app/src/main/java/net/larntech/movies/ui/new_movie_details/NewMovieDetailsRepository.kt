package net.larntech.movies.ui.new_movie_details

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.network.api.ApiClient
import net.larntech.movies.util.Resource
import net.larntech.movies.util.RetrofitErrorUtil

class NewMovieDetailsRepository {

    private val apiService = ApiClient.apiServices
    suspend fun addFavourite(moviesItem: MoviesItem): Resource<MoviesItem> {
        return  withContext(Dispatchers.IO) {
            try {
                val favouriteMovies = apiService.addFavourite(moviesItem)
                Resource.success(favouriteMovies)
            } catch (e: Exception) {
                Resource.error(RetrofitErrorUtil.serverException(e))
            }
        }
    }

}