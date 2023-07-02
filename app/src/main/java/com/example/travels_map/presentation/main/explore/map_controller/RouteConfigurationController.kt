package com.example.travels_map.presentation.main.explore.map_controller

import android.os.Handler
import android.os.Looper
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.utils.map.AlphabetUtil
import com.example.travels_map.utils.map.MapObjectUtil
import com.example.travels_map.utils.map.updateCameraPosition
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider

class RouteConfigurationController(
    private val map: Map,
    private val routeConfigurationListener: OnRouteConfigurationListener,
) : IMapController {

    private val waypointCollection = map.mapObjects.addCollection()
    private val configurableRouteCollection = map.mapObjects.addCollection()

    private val points: MutableList<PlacemarkMapObject> = mutableListOf()

    override fun onMapTap(map: Map, point: Point): Unit = addPoint(point)

    override fun onMapLongTap(map: Map, point: Point): Unit = addPoint(point)

    override fun onObjectTap(event: GeoObjectTapEvent): Boolean {
        val point = event.geoObject.geometry.first().point ?: return false

        checkPoint(point)

        return true
    }

    override fun onMapObjectTap(mapObject: MapObject, clickPoint: Point): Boolean {
        checkPoint((mapObject.userData as Place).coordinates)

        return true
    }

    override fun getPlaceMarkCollections(): List<MapObjectCollection> = listOf(waypointCollection)

    override fun getPolylineCollection(): MapObjectCollection = configurableRouteCollection

    override fun onActivate(point: Point) {
        Handler(Looper.getMainLooper()).postDelayed({
            addPoint(point)
        }, 750L)
    }

    override fun onDeactivate() = clear()

    fun drawRoute(drivingRoute: DrivingRoute) {
        configurableRouteCollection.apply {
            clear()
            addPolyline(drivingRoute.geometry)
        }

        map.updateCameraPosition(drivingRoute.geometry)
    }

    private fun checkPoint(point: Point) {
        if (points.contains(point)) {
            removePoint(point)
        } else {
            addPoint(point)
        }
    }

    private fun addPoint(point: Point) {
        val placeMark = waypointCollection.addPlacemark(
            point,
            ImageProvider.fromBitmap(
                MapObjectUtil.Waypoint.drawBitmap(AlphabetUtil.getAlphabetLetter(points.size))
            ),
        ).apply {
            userData = Place(coordinates = point, isCustomObject = true)
        }
        points.add(placeMark)

        routeConfigurationListener.addPoint(point)
    }

    private fun removePoint(point: Point) {
        val indexedValue = points.withIndex().find {
            it.value.geometry.latitude == point.latitude && it.value.geometry.longitude == point.longitude
        } ?: return

        points.removeAt(indexedValue.index)

        waypointCollection.remove(indexedValue.value)

        for (i in indexedValue.index until points.size) {
            points[i].setIcon(
                ImageProvider.fromBitmap(
                    MapObjectUtil.Waypoint.drawBitmap(AlphabetUtil.getAlphabetLetter(i))
                )
            )
        }

        if (points.size < 2) {
            configurableRouteCollection.clear()
        }

        routeConfigurationListener.removePoint(point)
    }

    private fun List<PlacemarkMapObject>.contains(point: Point): Boolean {
        val obj = find {
            it.geometry.latitude == point.latitude && it.geometry.longitude == point.longitude
        }
        return obj != null
    }

    private fun clear() {
        points.clear()
        waypointCollection.clear()
        configurableRouteCollection.clear()
    }
}