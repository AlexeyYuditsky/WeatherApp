package com.alexeyyuditsky.weatherapp.weather.presentation

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.text.format.DateUtils
import java.util.Date
import java.util.Locale
import javax.inject.Inject

interface TimeWrapper {

    fun getHumanReadableTime(timeMillis: Long): String

    class Base @Inject constructor() : TimeWrapper {

        override fun getHumanReadableTime(timeMillis: Long): String {
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(
                timeMillis,
                now,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            ).toString()
            val dateFormat = SimpleDateFormat("HH:mm dd-MMM-yyyy", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault()
            val time = dateFormat.format(Date(timeMillis))
            return "$ago ($time)"
        }
    }
}