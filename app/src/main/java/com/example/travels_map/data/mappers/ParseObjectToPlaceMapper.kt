package com.example.travels_map.data.mappers

import com.example.travels_map.domain.entities.Feedback
import com.example.travels_map.domain.entities.Place
import com.parse.ParseObject
import com.yandex.mapkit.geometry.Point
import javax.inject.Inject

private const val EXCEPTION_NAME_VALUE_WAS_NULL = "Name value was null"
private const val EXCEPTION_COORDINATES_VALUE_WAS_NULL = "Coordinates value was null"

class ParseObjectToPlaceMapper @Inject constructor(
    private val parsePlaceToFeedbackMapper: IEntityMapper<ParseObject, Feedback>,
) : IEntityMapper<ParseObject, Place> {

    override fun mapEntity(entity: ParseObject): Place {
        val name = checkNotNull(entity.getString(Place.KEY_NAME)) {
            EXCEPTION_NAME_VALUE_WAS_NULL
        }

        val coordinates = checkNotNull(entity.getParseGeoPoint(Place.KEY_COORDINATES)) {
            EXCEPTION_COORDINATES_VALUE_WAS_NULL
        }

        return Place(
            id = entity.objectId,
            name = name,
            coordinates = Point(coordinates.latitude, coordinates.longitude),
            feedback = parsePlaceToFeedbackMapper.mapEntity(entity),
            isCustomObject = entity.getBoolean(Place.KEY_IS_CUSTOM_OBJECT)
        )
    }
}