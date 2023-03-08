package net.larntech.movies.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.util.Resource
import net.larntech.movies.util.Status

class MovieDetailsViewModel : ViewModel() {

    var mainActivityRepository = MovieDetailsRepository()

    private var _updateFavourite = MutableLiveData<Resource<MoviesItem>>()
    val updateFavourite: LiveData<Resource<MoviesItem>>
        get() = _updateFavourite

    fun updateFavourite(moviesItem: MoviesItem) {
        _updateFavourite.postValue(Resource.loading("Processing request ... "))
        viewModelScope.launch {
            val result = mainActivityRepository.updateFavourite(moviesItem)
            when (result.status) {
                Status.ERROR -> {
                    _updateFavourite.postValue(Resource.error(result.message!!))
                }
                Status.SUCCESS -> {
                    _updateFavourite.postValue(Resource.success(result.data))
                }
                else -> {}
            }
        }
    }


}