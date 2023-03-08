package net.larntech.movies.ui.new_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.util.Resource
import net.larntech.movies.util.Status

class NewMovieDetailsViewModel : ViewModel() {

    var mainActivityRepository = NewMovieDetailsRepository()

    private var _addFavourite = MutableLiveData<Resource<MoviesItem>>()
    val addFavourite: LiveData<Resource<MoviesItem>>
        get() = _addFavourite

    fun addFavourite(moviesItem: MoviesItem) {
        _addFavourite.postValue(Resource.loading("Processing request ... "))
        viewModelScope.launch {
            val result = mainActivityRepository.addFavourite(moviesItem)
            when (result.status) {
                Status.ERROR -> {
                    _addFavourite.postValue(Resource.error(result.message!!))
                }
                Status.SUCCESS -> {
                    _addFavourite.postValue(Resource.success(result.data))
                }
                else -> {}
            }
        }
    }


}