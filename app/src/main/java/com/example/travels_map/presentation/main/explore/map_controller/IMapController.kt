package com.example.travels_map.presentation.main.explore.map_controller

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener

interface IMapController : InputListener, GeoObjectTapListener, MapObjectTapListener {
    override fun onMapTap(map: Map, point: Point)
    override fun onMapLongTap(map: Map, point: Point)

    override fun onObjectTap(event: GeoObjectTapEvent): Boolean
    override fun onMapObjectTap(mapObject: MapObject, clickPoint: Point): Boolean

    fun getPlaceMarkCollections(): List<MapObjectCollection>
    fun getPolylineCollection(): MapObjectCollection

    fun onActivate(point: Point)
    fun onDeactivate()
}