package com.example.travels_map.presentation.main.explore.map_controller

import com.example.travels_map.domain.entities.Place
import com.example.travels_map.utils.map.MapUtil
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import kotlin.math.pow

class DefaultController(
    private val map: Map,
    private val mapClickListener: OnMapClickListener,
) : IMapController {

    private val routeCollection = map.mapObjects.addCollection()
    private val routePointCollection = map.mapObjects.addCollection()

    private val placePointCollection = map.mapObjects.addCollection()
    private val userCollection = map.mapObjects.addCollection()

    private var selectedGeoObject: GeoObject? = null
    private var selectedMapObject: MapObject? = null

    private var temporaryMapObjectIsActivated = false

    override fun onMapTap(map: Map, point: Point) {
        deselectActiveObject()

        mapClickListener.onMapClick()
    }

    override fun onMapLongTap(map: Map, point: Point) {
        deselectActiveObject()

        val place = Place(coordinates = point, isCustomObject = true)

        selectedMapObject = placePointCollection.addPlacemark(
            point,
            ImageProvider.fromBitmap(MapUtil.Place.drawBitmap())
        ).apply {
            userData = place
        }

        temporaryMapObjectIsActivated = true

        mapClickListener.onMapLongClick(place)

        updateCameraPosition(point)
    }

    override fun onObjectTap(event: GeoObjectTapEvent): Boolean {
        val geoObject = event.geoObject
        val metadata = geoObject.metadataContainer.getItem(GeoObjectSelectionMetadata::class.java) ?: return false

        deselectActiveObject()

        val point = geoObject.geometry.first().point ?: return false

        selectedGeoObject = geoObject

        map.selectGeoObject(metadata.id, metadata.layerId)

        mapClickListener.onGeoObjectClick(
            Place(
                name = geoObject.name,
                coordinates = point,
            )
        )

        updateCameraPosition(point)

        return true
    }

    override fun onMapObjectTap(mapObject: MapObject, clickPoint: Point): Boolean {
        deselectActiveObject()

        val place = mapObject.userData as Place

        selectedMapObject = mapObject

        mapClickListener.onMapObjectClick(place)

        updateCameraPosition(place.coordinates)

        return true
    }

    override fun getPlaceMarkCollections(): List<MapObjectCollection> = listOf(routePointCollection, placePointCollection)

    override fun getPolylineCollection(): MapObjectCollection = routeCollection

    override fun onActivate(point: Point) {
        if (!temporaryMapObjectIsActivated) {
            onMapLongTap(map, point)
            temporaryMapObjectIsActivated = true
        }
    }

    override fun onDeactivate() {
        temporaryMapObjectIsActivated = false

        deselectActiveObject()
    }

    private fun deselectActiveObject() {
        selectedGeoObject = selectedGeoObject.run {
            map.deselectGeoObject()
            return@run null
        }

        selectedMapObject = selectedMapObject?.run {
            return@run if (temporaryMapObjectIsActivated) {
                placePointCollection.remove(this)
                temporaryMapObjectIsActivated = false
                null
            } else {
                this
            }
        }
    }

    fun getUserCollection() = userCollection

    fun getPlacePointCollection() = placePointCollection

    fun getRoutePointCollection() = routePointCollection

    fun clearActiveObject() {
        temporaryMapObjectIsActivated = false

        deselectActiveObject()
    }

    private fun updateCameraPosition(point: Point) {
        val duration = 100 / map.cameraPosition.zoom.pow(2)

        map.move(
            CameraPosition(
                point,
                map.cameraPosition.zoom,
                map.cameraPosition.azimuth,
                map.cameraPosition.tilt,
            ),
            Animation(Animation.Type.SMOOTH, duration),
            null,
        )
    }
}