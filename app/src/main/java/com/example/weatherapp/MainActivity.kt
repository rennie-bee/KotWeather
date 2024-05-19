package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp.model.Weather.CurrentWeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var searchButton: Button
    private lateinit var cityEditText: EditText
    private lateinit var weatherInfoLayout: RelativeLayout
    private lateinit var errorTextView: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var cityNameTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var weatherConditionTextView: TextView
    private lateinit var minTempTextView: TextView
    private lateinit var maxTempTextView: TextView
    private lateinit var sunsetTextView: TextView
    private lateinit var sunriseTextView: TextView
    private lateinit var windSpeedTextView: TextView
    private lateinit var pressureTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var updatedTextView: TextView // New TextView for update time

    private val API_KEY = "bd8f219cca5a254c9ec1ce7755918171"
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.btnSearch)
        cityEditText = findViewById(R.id.etCityName)
        weatherInfoLayout = findViewById(R.id.weatherLayout)
        errorTextView = findViewById(R.id.tvErrorMessage)
        progressBar = findViewById(R.id.progressBar)

        cityNameTextView = findViewById(R.id.tvCity)
        temperatureTextView = findViewById(R.id.tvTemperature)
        weatherConditionTextView = findViewById(R.id.tvDescription)
        minTempTextView = findViewById(R.id.tvMinTemp)
        maxTempTextView = findViewById(R.id.tvMaxTemp)
        sunsetTextView = findViewById(R.id.tvSunset)
        sunriseTextView = findViewById(R.id.tvSunrise)
        windSpeedTextView = findViewById(R.id.tvWind)
        pressureTextView = findViewById(R.id.tvPressure)
        humidityTextView = findViewById(R.id.tvHumidity)
        updatedTextView = findViewById(R.id.tvUpdated) // Initialize the new TextView

        searchButton.setOnClickListener {
            val cityName = cityEditText.text.toString()
            if (cityName.isEmpty()) {
                showError("City name cannot be blank")
            } else {
                getWeatherForCity(cityName)
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getWeatherForCity(city: String) {
        if (isNetworkAvailable()) {
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$API_KEY"
//            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$API_KEY"
            fetchWeatherData(url).start()
        } else {
            showError("Please connect to the internet.")
        }
    }

    private fun fetchWeatherData(urlString: String): Thread {
        return Thread {
            try {
                runOnUiThread {
                    progressBar.visibility = View.VISIBLE
                    weatherInfoLayout.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }

                val url = URL(urlString)
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = reader.readText()

                val weatherResponse = Gson().fromJson(result, CurrentWeatherResponse::class.java)
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    if (weatherResponse != null) {
                        updateUIWithWeatherData(weatherResponse)
                    } else {
                        showError("Invalid city name. Please enter a valid city.")
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    Log.e("WeatherApp", "Error fetching weather data", e)
                    showError("Error fetching weather data: ${e.message}. Please try again.")
                }
            }
        }
    }

    private fun updateUIWithWeatherData(weatherResponse: CurrentWeatherResponse) {
        cityNameTextView.text = weatherResponse.name
        temperatureTextView.text = "${weatherResponse.main.temp}°C"
        weatherConditionTextView.text = weatherResponse.weather[0].description
        minTempTextView.text = "Min: ${weatherResponse.main.temp_min}°C"
        maxTempTextView.text = "Max: ${weatherResponse.main.temp_max}°C"
        sunsetTextView.text = "Sunset: ${convertUnixTimeToDate(weatherResponse.sys.sunset)}"
        sunriseTextView.text = "Sunrise: ${convertUnixTimeToDate(weatherResponse.sys.sunrise)}"
        windSpeedTextView.text = "Wind: ${weatherResponse.wind.speed} m/s"
        pressureTextView.text = "Pressure: ${weatherResponse.main.pressure} hPa"
        humidityTextView.text = "Humidity: ${weatherResponse.main.humidity}%"

        updatedTextView.text = "Updated: ${getCurrentTime()}" // Set the updated time

        weatherInfoLayout.visibility = View.VISIBLE
    }

    private fun convertUnixTimeToDate(unixTime: Long): String {
        val date = Date(unixTime * 1000)
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(date)
    }

    private fun getCurrentTime(): String {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(Date())
    }

    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationWeather() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                if (isNetworkAvailable()) {
                    val url = "https://api.openweathermap.org/data/2.5/weather?lat=${it.latitude}&lon=${it.longitude}&units=metric&appid=$API_KEY"
                    fetchWeatherData(url).start()
                } else {
                    showError("Please connect to the internet.")
                }
            } ?: run {
                showError("Unable to determine location. Please enter a city name.")
            }
        }
    }

    private fun requestLocationPermissions() {
        requestPermissionsLauncher.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            getCurrentLocationWeather()
        } else {
            showError("Location permission denied. Please enter a city name.")
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
