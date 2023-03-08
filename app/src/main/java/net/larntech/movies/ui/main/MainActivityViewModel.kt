package net.larntech.movies.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.larntech.movies.model.movies.favourites.FavouriteMoviesResponse
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.util.Resource
import net.larntech.movies.util.Status

class MainActivityViewModel : ViewModel() {

    var mainActivityRepository = MainActivityRepository()

    private var _allFavouriteMovies = MutableLiveData<Resource<List<MoviesItem>>>()
    val allFavouriteMovies: LiveData<Resource<List<MoviesItem>>>
        get() = _allFavouriteMovies

    fun getFavoritesMovies() {
        _allFavouriteMovies.postValue(Resource.loading("Processing request ... "))
        viewModelScope.launch {
            val result = mainActivityRepository.getFavouriteMovies()
            when (result.status) {
                Status.ERROR -> {
                    _allFavouriteMovies.postValue(Resource.error(result.message!!))
                }
                Status.SUCCESS -> {
                    _allFavouriteMovies.postValue(Resource.success(result.data))
                }
                else -> {}
            }
        }
    }


}