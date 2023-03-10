package net.larntech.movies.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.larntech.movies.model.movies.favourites.ConfItem
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.model.weather.WeatherDataResponse
import net.larntech.movies.util.Resource
import net.larntech.movies.util.Status

class WeatherViewModel : ViewModel() {

    var weatherRepository = WeatherRepository()

    private var _weatherData = MutableLiveData<Resource<WeatherDataResponse>>()
    val weatherData: LiveData<Resource<WeatherDataResponse>>
        get() = _weatherData

    private var _cityConfig = MutableLiveData<Resource<List<ConfItem>>>()
    val cityConfig: LiveData<Resource<List<ConfItem>>>
        get() = _cityConfig


    fun weatherData(key: String, city: String) {
        _weatherData.postValue(Resource.loading("Processing request ... "))
        viewModelScope.launch {
            val result = weatherRepository.getWeatherData(key,city)
            when (result.status) {
                Status.ERROR -> {
                    _weatherData.postValue(Resource.error(result.message!!))
                }
                Status.SUCCESS -> {
                    _weatherData.postValue(Resource.success(result.data))
                }
                else -> {}
            }
        }
    }





    fun getConfCity() {
        _cityConfig.postValue(Resource.loading("Processing request ... "))
        viewModelScope.launch {
            val result = weatherRepository.getCityConf()
            when (result.status) {
                Status.ERROR -> {
                    _cityConfig.postValue(Resource.error(result.message!!))
                }
                Status.SUCCESS -> {
                    _cityConfig.postValue(Resource.success(result.data))
                }
                else -> {}
            }
        }
    }



}