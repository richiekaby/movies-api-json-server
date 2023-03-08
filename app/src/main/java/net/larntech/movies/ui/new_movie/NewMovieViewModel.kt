package net.larntech.movies.ui.new_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.model.movies.new_movies.NewMovieResponse
import net.larntech.movies.model.movies.new_movies.ResultsItem
import net.larntech.movies.util.Resource
import net.larntech.movies.util.Status

class NewMovieViewModel : ViewModel() {

    var mainActivityRepository = NewMovieRepository()

    private var _allNewMovies = MutableLiveData<Resource<NewMovieResponse>>()
    val allNewMovies: LiveData<Resource<NewMovieResponse>>
        get() = _allNewMovies

    fun getAllNewMovies(api_key: String, title: String,sortBy: String?) {
        _allNewMovies.postValue(Resource.loading("Processing request ... "))
        viewModelScope.launch {
            val result = mainActivityRepository.getNewMovies(api_key,title,sortBy)
            when (result.status) {
                Status.ERROR -> {
                    _allNewMovies.postValue(Resource.error(result.message!!))
                }
                Status.SUCCESS -> {
                    _allNewMovies.postValue(Resource.success(result.data))
                }
                else -> {}
            }
        }
    }





}