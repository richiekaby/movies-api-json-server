package net.larntech.movies.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import net.larntech.movies.R
import net.larntech.movies.config.BaseUrl
import net.larntech.movies.databinding.ActivityMovieDetailsBinding
import net.larntech.movies.databinding.ActivityWeatherBinding
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.model.weather.WeatherDataResponse
import net.larntech.movies.ui.favourite_movie_details.MovieDetailsViewModel
import net.larntech.movies.util.Status

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var weatherDataResponse: WeatherDataResponse
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData(){
        handleViewModel()
        handleCityConf()
        handleClick()
    }

    private fun handleCityConf(){
        viewModel.getConfCity()
    }

    private fun requestWeatherData(city: String){
        binding.edCity.setText(city)
        viewModel.weatherData(BaseUrl.WEATHER_API,city)
    }

    private fun handleClick(){
        binding.btnUpdate.setOnClickListener{
            if(binding.edCity.text != null && binding.edCity.text.toString().isNotEmpty()){
                val city = binding.edCity.text.toString()
                requestWeatherData(city)
            }else{
                showMessage("City Name Required")
            }

        }
    }



    private fun showMessage(message: String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    private fun handleViewModel(){
        viewModel.weatherData.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    handleProgressBar(false)
                    if(it.data != null) {
                        weatherDataResponse = it.data!!
                        showData()
                    }else{
                        showMessage("Unable to fetch weather data ...")
                    }

                }
                Status.ERROR -> {
                    handleProgressBar(false)
                    showMessage(it.message!!)

                }
                Status.LOADING -> {
                    handleProgressBar(true)

                }
                else -> {
                    handleProgressBar(false)

                }
            }
        }


        viewModel.cityConfig.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    handleProgressBar(false)
                    var response = it.data
                    var cityItem = response?.get(0)
                    requestWeatherData(cityItem!!.city)
                }
                Status.ERROR -> {
                    handleProgressBar(false)
                    showMessage(it.message!!)

                }
                Status.LOADING -> {
                    handleProgressBar(true)

                }
                else -> {
                    handleProgressBar(false)

                }
            }
        }

    }

    private fun showData(){
        binding.tvTemperature.text = "Temperature:"+ weatherDataResponse.current.tempC+ " °C"
        binding.tvHumidity.text = "Humidity:"+ weatherDataResponse.current.humidity+ " g/m3"
        binding.tvFeelLike.text = "Feels Like:"+ weatherDataResponse.current.feelslikeC+ " °C"
        binding.tvWindDirection.text = "Wind Direction:"+ weatherDataResponse.current.windDir
        binding.tvWindSpeed.text = "Wind Speed:"+ weatherDataResponse.current.windKph+" km/h"
        binding.tvAtmospheric.text = "Atmospheric Pressure:"+ weatherDataResponse.current.pressureMb+" mb"
        binding.tvPrescipitation.text = "Precipitation:"+ weatherDataResponse.current.precipMm+" mm"
        binding.textWeather.text = weatherDataResponse.current.condition.text
        binding.lastUpdate.text = weatherDataResponse.current.lastUpdated

        updateWeatherIcon(weatherDataResponse.current.condition.icon)
    }

    private fun updateWeatherIcon(url: String){
        var newUrl = url
        if(newUrl.contains("//")){
            newUrl = "https:$url"
        }
        Log.e(" url ", "===>"+newUrl)
        Glide.with(this)
            .load(newUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(ContextCompat.getDrawable(this, R.drawable.baseline_cloud_24))
            .into(binding.imageWeather)
    }

    private fun handleProgressBar(show: Boolean){
        if(show) {
            binding.progressBarInclude.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBarInclude.progressBar.visibility = View.GONE
        }
    }


}