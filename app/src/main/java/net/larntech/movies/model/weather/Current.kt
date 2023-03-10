package net.larntech.movies.model.weather

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Current(@SerializedName("feelslike_c")
                   val feelslikeC: Double = 0.0,
                   @SerializedName("uv")
                   val uv: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("last_updated")
                   val lastUpdated: String = "",
                   @SerializedName("feelslike_f")
                   val feelslikeF: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("wind_degree")
                   val windDegree: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("last_updated_epoch")
                   val lastUpdatedEpoch: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("is_day")
                   val isDay: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("precip_in")
                   val precipIn: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("wind_dir")
                   val windDir: String = "",
                   @SerializedName("gust_mph")
                   val gustMph: Double = 0.0,
                   @SerializedName("temp_c")
                   val tempC: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("pressure_in")
                   val pressureIn: Double = 0.0,
                   @SerializedName("gust_kph")
                   val gustKph: Double = 0.0,
                   @SerializedName("temp_f")
                   val tempF: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("precip_mm")
                   val precipMm: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("cloud")
                   val cloud: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("wind_kph")
                   val windKph: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("condition")
                   val condition: Condition,
                   @SerializedName("wind_mph")
                   val windMph: Double = 0.0,
                   @SerializedName("vis_km")
                   val visKm: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("humidity")
                   val humidity: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("pressure_mb")
                   val pressureMb: BigDecimal = BigDecimal.ZERO,
                   @SerializedName("vis_miles")
                   val visMiles: BigDecimal = BigDecimal.ZERO)