package net.larntech.movies.model.movies.favourites

import com.google.gson.annotations.SerializedName

data class ConfItem(@SerializedName("city")
                    val city: String = "")