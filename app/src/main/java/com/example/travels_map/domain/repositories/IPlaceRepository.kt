package com.example.travels_map.domain.repositories

import com.example.travels_map.domain.entities.Place
import kotlinx.coroutines.flow.SharedFlow

interface IPlaceRepository {
    val placeFlow: SharedFlow<Result<Place>>
    val placeListFlow: SharedFlow<Result<List<Place>>>

    suspend fun save(place: Place) : Result<Nothing?>

    suspend fun requestPlace(place: Place)
    suspend fun requestPlaceList()
}