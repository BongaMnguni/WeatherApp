package com.bongamnguni.weather.restApi.ForecastWeather
import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)