package com.alexeyyuditsky.weatherapp.weather.data

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.widget.WeatherWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val fetchWeatherRepository: FetchWeatherRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            fetchWeatherRepository.fetchWeather()
            WeatherWidget().updateAll(applicationContext)
        } catch (e: DomainException) {
            fetchWeatherRepository.saveException(e)
        }
        return Result.success()
    }
}