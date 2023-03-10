package net.larntech.movies.model.weather

import com.google.gson.annotations.SerializedName

data class Condition(@SerializedName("code")
                     val code: Int = 0,
                     @SerializedName("icon")
                     val icon: String = "",
                     @SerializedName("text")
                     val text: String = "")