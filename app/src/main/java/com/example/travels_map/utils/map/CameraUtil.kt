package com.example.travels_map.utils.map

import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map

const val DEFAULT_ZOOM = 2f
const val NORMAL_ZOOM = 16f

val DEFAULT_POINT = Point(30.0, 90.0)

const val DURATION = 0.75f

/**
 * Updates the camera position relative to the point
 */
fun Map.updateCameraPosition(point: Point) {
    move(
        CameraPosition(
            point,
            NORMAL_ZOOM,
            cameraPosition.azimuth,
            cameraPosition.tilt,
        ),
        Animation(Animation.Type.SMOOTH, DURATION),
        null,
    )
}

/**
 * Updates the camera position relative to the polyline
 */
fun Map.updateCameraPosition(polyline: Polyline) {
    val cameraPosition = cameraPosition(
        Geometry.fromPolyline(polyline),
        cameraPosition.azimuth,
        cameraPosition.tilt,
        null,
    )

    val zoom = cameraPosition.zoom * 0.95f

    move(
        CameraPosition(
            cameraPosition.target,
            zoom,
            cameraPosition.azimuth,
            cameraPosition.tilt
        ),
        Animation(Animation.Type.SMOOTH, DURATION),
        null,
    )
}