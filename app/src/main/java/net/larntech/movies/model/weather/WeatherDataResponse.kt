package net.larntech.movies.model.weather

import com.google.gson.annotations.SerializedName

data class WeatherDataResponse(@SerializedName("current")
                               val current: Current,
                               @SerializedName("location")
                               val location: Location)