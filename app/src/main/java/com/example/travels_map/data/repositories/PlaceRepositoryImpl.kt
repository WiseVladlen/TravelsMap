package com.example.travels_map.data.repositories

import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.repositories.IFeedbackRepository
import com.example.travels_map.domain.repositories.IPlaceRepository
import com.parse.ParseGeoPoint
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.coroutines.getById
import com.parse.coroutines.suspendFind
import com.parse.coroutines.suspendSave
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.lang.Exception
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val feedbackRepository: IFeedbackRepository,
    private val parseObjectToPlaceMapper: IEntityMapper<ParseObject, Place>,
    private val placeToParsePlaceMapper: IEntityMapper<Place, ParseObject>,
) : IPlaceRepository {

    private val _placeFlow = MutableSharedFlow<Result<Place>>(1, 1, BufferOverflow.DROP_OLDEST)

    override val placeFlow: SharedFlow<Result<Place>> = _placeFlow.asSharedFlow()

    private val _placeListFlow = MutableSharedFlow<Result<List<Place>>>(1, 1, BufferOverflow.DROP_OLDEST)

    override val placeListFlow: SharedFlow<Result<List<Place>>> = _placeListFlow.asSharedFlow()

    override suspend fun save(place: Place): Result<Nothing?> {
        return if (place.id == null) {
            create(place)
        } else {
            update(place)
        }
    }

    private suspend fun create(place: Place): Result<Nothing?> {
        return runCatching {
            val parsePlace = placeToParsePlaceMapper.mapEntity(place).apply {
                getRelation<ParseObject>(Place.KEY_FEEDBACK).add(feedbackRepository.create())
                suspendSave()
            }

            feedbackRepository.requestFeedbackData(parsePlace)

            requestPlace(parsePlace)
            requestPlaceList()

            return@runCatching null
        }
    }

    private suspend fun update(place: Place): Result<Nothing?> {
        return runCatching {
            if (place.id == null || place.name == null) throw Exception()

            val parsePlace = ParseQuery<ParseObject>(Place.CLASS_NAME).getById(place.id).apply {
                put(Place.KEY_NAME, place.name)
                suspendSave()
            }

            feedbackRepository.requestFeedbackData(parsePlace)

            requestPlace(parsePlace)
            requestPlaceList()

            return@runCatching null
        }
    }

    override suspend fun requestPlace(place: Place) {
        val result = runCatching {
            val query = with(ParseQuery<ParseObject>(Place.CLASS_NAME)) {
                whereEqualTo(Place.KEY_COORDINATES, ParseGeoPoint(place.coordinates.latitude, place.coordinates.longitude))
            }

            return@runCatching when (val parsePlace = query.suspendFind().firstOrNull()) {
                null -> place
                else -> {
                    feedbackRepository.requestFeedbackData(parsePlace)

                    parseObjectToPlaceMapper.mapEntity(parsePlace)
                }
            }
        }

        _placeFlow.emit(result)
    }

    override suspend fun requestPlaceList() {
        val result = runCatching {
            val list = with(ParseQuery<ParseObject>(Place.CLASS_NAME)) {
                whereEqualTo(Place.KEY_IS_CUSTOM_OBJECT, true)
                return@with suspendFind()
            }

            return@runCatching list.map { parseObjectToPlaceMapper.mapEntity(it) }
        }

        _placeListFlow.emit(result)
    }

    private suspend fun requestPlace(parsePlace: ParseObject) {
        val result = runCatching {
            return@runCatching parseObjectToPlaceMapper.mapEntity(parsePlace)
        }

        _placeFlow.emit(result)
    }
}