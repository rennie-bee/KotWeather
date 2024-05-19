package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse (
    @SerializedName("coord") val coord: Coord,
    @SerializedName("weather") val weather: List<WeatherItem>,
    @SerializedName("main") val main: Main,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("name") val name: String
)


//TODO
// Create data class CurrentWeatherResponse (Refer to API Response)
// Hint: Refer to Wind Data Class