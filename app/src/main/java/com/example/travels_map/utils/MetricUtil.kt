package com.example.travels_map.utils

import android.content.res.Resources
import android.util.DisplayMetrics

object MetricUtil {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}