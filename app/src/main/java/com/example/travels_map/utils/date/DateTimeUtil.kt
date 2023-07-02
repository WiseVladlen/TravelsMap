package com.example.travels_map.utils.date

import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import java.util.Date
import java.util.concurrent.TimeUnit

private val resources = TravelsMapApplication.INSTANCE.resources

fun getTimeAgo(date: Date): String {
    val diffInMillis = Date().time - date.time

    return when {
        diffInMillis <= TimeGranularity.SECONDS.toMillis() -> {
            resources.getString(R.string.time_just_now)
        }

        diffInMillis < TimeGranularity.MINUTES.toMillis() -> {
            TimeUnit.MILLISECONDS.toSeconds(diffInMillis).let { time ->
                resources.getQuantityString(R.plurals.time_seconds_ago, time.toInt(), time)
            }
        }

        diffInMillis < TimeGranularity.HOURS.toMillis() -> {
            TimeUnit.MILLISECONDS.toMinutes(diffInMillis).let { time ->
                resources.getQuantityString(R.plurals.time_minutes_ago, time.toInt(), time)
            }
        }

        diffInMillis < TimeGranularity.DAYS.toMillis() -> {
            TimeUnit.MILLISECONDS.toHours(diffInMillis).let { time ->
                resources.getQuantityString(R.plurals.time_hours_ago, time.toInt(), time)
            }
        }

        diffInMillis < TimeGranularity.WEEKS.toMillis() -> {
            TimeUnit.MILLISECONDS.toDays(diffInMillis).let { time ->
                resources.getQuantityString(R.plurals.time_days_ago, time.toInt(), time)
            }
        }

        diffInMillis < TimeGranularity.MONTHS.toMillis() -> {
            (TimeUnit.MILLISECONDS.toDays(diffInMillis) / 7).let { time ->
                resources.getQuantityString(R.plurals.time_weeks_ago, time.toInt(), time)
            }
        }

        diffInMillis < TimeGranularity.YEARS.toMillis() -> {
            (TimeUnit.MILLISECONDS.toDays(diffInMillis) / 30).let { time ->
                resources.getQuantityString(R.plurals.time_months_ago, time.toInt(), time)
            }
        }

        else -> {
            (TimeUnit.MILLISECONDS.toDays(diffInMillis) / 365).let { time ->
                resources.getQuantityString(R.plurals.time_years_ago, time.toInt(), time)
            }
        }
    }
}