package com.alexeyyuditsky.weatherapp.weather.presentation.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.weather.data.FetchWeatherRepository
import com.alexeyyuditsky.weatherapp.widget.WeatherWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val fetchWeatherRepository: FetchWeatherRepository,
) : CoroutineWorker(applicationContext, workerParameters) {

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