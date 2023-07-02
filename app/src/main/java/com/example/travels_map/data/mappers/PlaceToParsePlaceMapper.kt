package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.Place
import com.parse.ParseGeoPoint
import com.parse.ParseObject
import javax.inject.Inject

private const val EXCEPTION_NAME_VALUE_WAS_NULL = "Name value was null"

class PlaceToParsePlaceMapper @Inject constructor() : IEntityMapper<Place, ParseObject> {

    override fun mapEntity(entity: Place): ParseObject {
        val name = checkNotNull(entity.name) {
            EXCEPTION_NAME_VALUE_WAS_NULL
        }

        return ParseObject(Place.CLASS_NAME).apply {
            entity.id?.let { put(Place.KEY_OBJECT_ID, it) }

            put(Place.KEY_NAME, name)
            put(Place.KEY_IS_CUSTOM_OBJECT, entity.isCustomObject)
            put(
                Place.KEY_COORDINATES,
                ParseGeoPoint(entity.coordinates.latitude, entity.coordinates.longitude)
            )
        }
    }
}