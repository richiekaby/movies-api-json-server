package net.larntech.movies.ui.weather

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.larntech.movies.model.movies.favourites.ConfItem
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.model.weather.WeatherDataResponse
import net.larntech.movies.network.api.ApiClient
import net.larntech.movies.util.Resource
import net.larntech.movies.util.RetrofitErrorUtil

class WeatherRepository {

    private val apiService = ApiClient.apiServices
    suspend fun getWeatherData(key: String,city: String ): Resource<WeatherDataResponse> {
        return  withContext(Dispatchers.IO) {
            try {
                val weatherData = apiService.getWeatherData(key,city)
                Resource.success(weatherData)
            } catch (e: Exception) {
                Resource.error(RetrofitErrorUtil.serverException(e))
            }
        }
    }

    suspend fun getCityConf(): Resource<List<ConfItem>> {
        return  withContext(Dispatchers.IO) {
            try {
                val cities = apiService.getCityConf()
                Resource.success(cities)
            } catch (e: Exception) {
                Resource.error(RetrofitErrorUtil.serverException(e))
            }
        }
    }

}