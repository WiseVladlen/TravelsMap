package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.Route
import com.example.travels_map.domain.models.BuildingRouteData
import com.parse.ParseGeoPoint
import com.parse.ParseObject
import javax.inject.Inject

class BuildingRouteDataToParseRouteMapper @Inject constructor() : IEntityMapper<BuildingRouteData, ParseObject> {

    override fun mapEntity(entity: BuildingRouteData): ParseObject {
        return ParseObject(Route.CLASS_NAME).apply {
            put(Route.KEY_NAME, entity.name)
            put(Route.KEY_WAY_POINTS, entity.wayPoints.map { ParseGeoPoint(it.latitude, it.longitude) })
            put(Route.KEY_REQUEST_POINTS, entity.requestPoints.map { ParseGeoPoint(it.latitude, it.longitude) })
        }
    }
}