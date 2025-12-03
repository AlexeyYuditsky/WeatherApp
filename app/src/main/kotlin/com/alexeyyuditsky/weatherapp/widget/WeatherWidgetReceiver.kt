package com.alexeyyuditsky.weatherapp.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.alexeyyuditsky.weatherapp.MainActivity
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.io.Serializable

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()
}

class WeatherWidget() : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val entryPoint: WidgetEntryPoint = EntryPointAccessors.fromApplication(
            context,
            WidgetEntryPoint::class.java
        )
        val weatherRepository = entryPoint.weatherRepository()
        provideContent {
            GlanceTheme {
                WidgetUiWrapper(weatherRepository)
            }
        }
    }
}

@Composable
private fun WidgetUiWrapper(cacheDataSource: WeatherCacheDataSource) {
    val context = LocalContext.current
    val bitmapState: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val weather = cacheDataSource.widgetWeatherFlow.collectAsState("").value
    val ui = if (weather.isEmpty())
        WidgetWeatherUi("choose\nlocation", "")
    else {
        val parts = weather.split(";")
        val imageLoader = ImageLoader(context)
        val imageUrl: String = parts[0]
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        LaunchedEffect(weather) {
            val drawable = (imageLoader.execute(request) as? SuccessResult)?.drawable
            val bitmap = (drawable as? BitmapDrawable)?.bitmap
            bitmapState.value = bitmap
        }
        WidgetWeatherUi(parts[1], parts[2])
    }
    WidgetUi(ui, bitmapState.value)
}

@Composable
private fun WidgetUi(ui: WidgetWeatherUi, bitmap: Bitmap?) {
    Row(
        modifier = GlanceModifier.background(color = Color(0xFF87CEEB)).fillMaxSize().clickable(
            actionStartActivity<MainActivity>()
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        bitmap?.let {
            Image(
                modifier = GlanceModifier.fillMaxHeight(),
                provider = ImageProvider(bitmap),
                contentDescription = "Weather Icon"
            )
        }
        Column {
            Text(
                text = ui.weather,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = ui.time, style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

data class WidgetWeatherUi(
    val weather: String,
    val time: String,
) : Serializable

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun weatherRepository(): WeatherCacheDataSource
}