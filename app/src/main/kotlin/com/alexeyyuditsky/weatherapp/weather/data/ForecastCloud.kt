package com.alexeyyuditsky.weatherapp.weather.data

import com.google.gson.annotations.SerializedName

data class ForecastCloud(
    @SerializedName("list") val list: List<WeatherForecastCloud>,
    @SerializedName("city") val city: ForecastCityCloud
)

data class ForecastCityCloud(
    @SerializedName("timezone") val timezone: Int
)

data class WeatherForecastCloud(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("main") val main: MainCloud,
    @SerializedName("weather") val weather: List<WeatherInnerCloud>,
    @SerializedName("clouds") val clouds: CloudsCloud,
    @SerializedName("wind") val wind: WindCloud,
    @SerializedName("dt_txt") val dateText: String
) {

    fun details(timeZone: Int): Pair<String, String> {
        var imageUrl = ""
        var result = ""

        result += "Temperature: ${main.temperature}°C"
        weather.firstOrNull()?.let { weatherFirst ->
            imageUrl = weatherFirst.imageUrl()
            result += "\n${weatherFirst.main} (${weatherFirst.description})"
        }
        result += "\nHumidity: ${main.humidity}%"
        result += "\nWind speed: ${wind.speed} m/s"
        result += "\nClouds: ${clouds.percents}%"
        result += "\nTime: ${
            formatUnixTimestampWithOffset(
                timestamp,
                timeZone,
                pattern = "dd MMM HH:mm"
            )
        }"

        return Pair(result, imageUrl)
    }
}