package com.bongamnguni.weather.restApi.CurrentWeather


data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)