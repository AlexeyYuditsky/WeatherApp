package com.alexeyyuditsky.weatherapp.weather.data

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class WeatherCloud(
    @SerializedName("weather") val weather: List<WeatherInnerCloud>,
    @SerializedName("main") val main: MainCloud,
    @SerializedName("name") val cityName: String,
    @SerializedName("wind") val wind: WindCloud,
    @SerializedName("clouds") val clouds: CloudsCloud,
    @SerializedName("sys") val sys: SysCloud,
    @SerializedName("timezone") val timeZone: Int
) {

    fun details(): Pair<String, String> {
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
        result += "\nSunrise: ${formatUnixTimestampWithOffset(sys.sunrise, timeZone)}"
        result += "\nSunset: ${formatUnixTimestampWithOffset(sys.sunset, timeZone)}"

        return Pair(result, imageUrl)
    }
}

fun formatUnixTimestampWithOffset(
    unixTimestampSeconds: Long,
    timezoneOffsetSeconds: Int,
    pattern: String = "HH:mm",
    locale: Locale = Locale.getDefault()
): String {
    if (unixTimestampSeconds <= 0) return "n/a"
    val utcInstant = Instant.ofEpochSecond(unixTimestampSeconds)
    val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds)
    val zonedDateTimeWithOffset = ZonedDateTime.ofInstant(utcInstant, zoneOffset)
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return zonedDateTimeWithOffset.format(formatter)
}

data class WeatherInnerCloud(
    @SerializedName("icon") val icon: String,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
) {

    fun imageUrl() = "https://openweathermap.org/img/wn/$icon@4x.png"
}

data class MainCloud(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("humidity") val humidity: Float,
)

data class WindCloud(
    @SerializedName("speed") val speed: Float,
)

data class CloudsCloud(
    @SerializedName("all") val percents: Int,
)

data class SysCloud(
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
)