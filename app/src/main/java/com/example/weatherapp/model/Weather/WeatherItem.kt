package com.example.weatherapp.model.Weather
import com.google.gson.annotations.SerializedName

data class WeatherItem (
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)


//TODO
// Create data class WeatherItem (Refer to API Response)
// Hint: Refer to Wind Data Class

