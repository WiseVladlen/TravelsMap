package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.Route
import com.parse.ParseGeoPoint
import com.parse.ParseObject
import com.yandex.mapkit.geometry.Point
import javax.inject.Inject

private const val EXCEPTION_NAME_VALUE_WAS_NULL = "Name value was null"
private const val EXCEPTION_REQUEST_POINTS_VALUE_WAS_NULL = "Request points value was null"
private const val EXCEPTION_WAY_POINTS_VALUE_WAS_NULL = "Way points value was null"

class ParseObjectToRouteMapper @Inject constructor() : IEntityMapper<ParseObject, Route> {

    override fun mapEntity(entity: ParseObject): Route {
        val name = checkNotNull(entity.getString(Route.KEY_NAME)) {
            EXCEPTION_NAME_VALUE_WAS_NULL
        }

        val wayPoints = checkNotNull(entity.getList<ParseGeoPoint>(Route.KEY_WAY_POINTS)) {
            EXCEPTION_WAY_POINTS_VALUE_WAS_NULL
        }

        val requestPoints = checkNotNull(entity.getList<ParseGeoPoint>(Route.KEY_REQUEST_POINTS)) {
            EXCEPTION_REQUEST_POINTS_VALUE_WAS_NULL
        }

        return Route(
            id = entity.objectId,
            name = name,
            wayPoints = wayPoints.map { Point(it.latitude, it.longitude) },
            requestPoints = requestPoints.map { Point(it.latitude, it.longitude) }
        )
    }
}