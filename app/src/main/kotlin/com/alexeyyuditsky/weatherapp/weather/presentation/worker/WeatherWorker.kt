package com.alexeyyuditsky.weatherapp.weather.presentation.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.weather.domain.RefreshWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.SaveWeatherExceptionUseCase
import com.alexeyyuditsky.weatherapp.widget.WeatherWidget
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val refreshWeatherUseCase: RefreshWeatherUseCase,
    private val saveWeatherExceptionUseCase: Lazy<SaveWeatherExceptionUseCase>,
) : CoroutineWorker(applicationContext, workerParameters) {

    override suspend fun doWork(): Result {
        try {
            refreshWeatherUseCase.invoke()
            WeatherWidget().updateAll(applicationContext)
        } catch (exception: DomainException) {
            saveWeatherExceptionUseCase.get().invoke(exception = exception)
        }
        return Result.success()
    }
}