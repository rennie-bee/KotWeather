package com.example.weatherapp.model.Weather
import com.google.gson.annotations.SerializedName

data class Main (
    @SerializedName("temp") val temp: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("temp_min") val temp_min: Double,
    @SerializedName("temp_max") val temp_max: Double
)


//TODO
// Create data class Main (Refer to API Response)
// Hint: Refer to Wind Data Class