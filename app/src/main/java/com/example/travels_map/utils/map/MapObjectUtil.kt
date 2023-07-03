package com.example.travels_map.utils.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextPaint
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.utils.MetricUtil

object MapObjectUtil {

    private val context = TravelsMapApplication.INSTANCE

    object Waypoint {

        private const val fullSize = 56
        private const val padding = 8
        private const val center = fullSize / 2f

        private const val size = fullSize - padding
        private const val radius = size / 2f

        private val textRect: Rect = Rect()

        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }

        private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = MetricUtil.convertDpToPixel(1.5f)
            isDither = true
        }

        private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = MetricUtil.convertDpToPixel(12f)
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }

        fun drawBitmap(text: String = String()): Bitmap {
            val bitmap = Bitmap.createBitmap(fullSize, fullSize, Bitmap.Config.ARGB_8888)
            val y = center + getTextHeight(text, textPaint, textRect) / 2

            Canvas(bitmap).apply {
                drawCircle(center, center, radius, backgroundPaint)
                drawCircle(center, center, radius, borderPaint)
                drawText(
                    text,
                    center,
                    y,
                    textPaint,
                )
            }

            return bitmap
        }
    }

    object Place {

        private const val fullSize = 56
        private const val padding = 8
        private const val center = fullSize / 2f

        private const val size = fullSize - padding
        private const val radius = size / 2f

        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColor(R.color.blue_de_france)
            style = Paint.Style.FILL
        }

        private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColor(R.color.black)
            style = Paint.Style.STROKE
            strokeWidth = MetricUtil.convertDpToPixel(1.5f)
            isDither = true
        }

        fun drawBitmap(): Bitmap {
            val bitmap = Bitmap.createBitmap(fullSize, fullSize, Bitmap.Config.ARGB_8888)

            Canvas(bitmap).apply {
                drawCircle(center, center, radius, backgroundPaint)
                drawCircle(center, center, radius, borderPaint)
            }

            return bitmap
        }
    }

    object User {

        private const val fullSize = 56
        private const val padding = 8
        private const val center = fullSize / 2f

        private const val size = fullSize - padding
        private const val radius = size / 2f

        private val textRect: Rect = Rect()

        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColor(R.color.dark_pastel_green)
            style = Paint.Style.FILL
        }

        private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = MetricUtil.convertDpToPixel(1.5f)
            isDither = true
        }

        private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = MetricUtil.convertDpToPixel(12f)
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }

        fun drawBitmap(text: String): Bitmap {
            val bitmap = Bitmap.createBitmap(fullSize, fullSize, Bitmap.Config.ARGB_8888)
            val y = center + getTextHeight(text, textPaint, textRect) / 2

            Canvas(bitmap).apply {
                drawCircle(center, center, radius, backgroundPaint)
                drawCircle(center, center, radius, borderPaint)
                drawText(
                    text,
                    center,
                    y,
                    textPaint,
                )
            }

            return bitmap
        }
    }

    private fun getTextHeight(text: String, paint: Paint, textRect: Rect): Float {
        paint.getTextBounds(text, 0, text.length, textRect)
        return textRect.height().toFloat()
    }
}