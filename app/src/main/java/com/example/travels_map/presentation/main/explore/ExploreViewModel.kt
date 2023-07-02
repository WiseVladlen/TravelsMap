package com.example.travels_map.presentation.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.entities.Route
import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.interactors.LoadDrivingRouteFlowInteractor
import com.example.travels_map.domain.interactors.LoadPlaceListFlowInteractor
import com.example.travels_map.domain.interactors.LoadDrivingRouteListFlowInteractor
import com.example.travels_map.domain.interactors.LoadParticipantsLocationFlow
import com.example.travels_map.domain.interactors.RequestDrivingRouteInteractor
import com.example.travels_map.domain.interactors.RequestPlaceListInteractor
import com.example.travels_map.domain.interactors.RequestDrivingRouteListFlowInteractor
import com.example.travels_map.domain.interactors.RequestParticipantsLocationInteractor
import com.example.travels_map.domain.interactors.UpdateUserLocationInteractor
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class ExploreViewModel(
    private val loadDrivingRouteFlowInteractor: LoadDrivingRouteFlowInteractor,
    private val loadDrivingRouteListFlowInteractor: LoadDrivingRouteListFlowInteractor,
    private val loadPlaceListFlowInteractor: LoadPlaceListFlowInteractor,
    private val loadParticipantsLocationFlow: LoadParticipantsLocationFlow,
    private val requestDrivingRouteInteractor: RequestDrivingRouteInteractor,
    private val requestDrivingRouteListFlowInteractor: RequestDrivingRouteListFlowInteractor,
    private val requestPlaceListInteractor: RequestPlaceListInteractor,
    private val requestParticipantsLocationInteractor: RequestParticipantsLocationInteractor,
    private val updateUserLocationInteractor: UpdateUserLocationInteractor,
) : ViewModel() {

    private val jobLoadDrivingRoute: CompletableJob = SupervisorJob()
    private val jobLoadRouteList: CompletableJob = SupervisorJob()
    private val jobLoadPlaceLabelList: CompletableJob = SupervisorJob()
    private val jobLoadParticipantsLocation: CompletableJob = SupervisorJob()

    private val _drivingRouteFlow = MutableSharedFlow<DrivingRoute>(1, 1, BufferOverflow.DROP_OLDEST)
    val drivingRouteFlow = _drivingRouteFlow.asSharedFlow()

    private val _routeListFlow = MutableSharedFlow<List<Route>>(1, 1, BufferOverflow.DROP_OLDEST)
    val routeListFlow = _routeListFlow.asSharedFlow()

    private val _placeListFlow = MutableSharedFlow<List<Place>>(1, 1, BufferOverflow.DROP_OLDEST)
    val placeListFlow = _placeListFlow.asSharedFlow()

    private val _participantsLocationFlow = MutableSharedFlow<List<User>>(1, 1, BufferOverflow.DROP_OLDEST)
    val participantsLocationFlow = _participantsLocationFlow.asSharedFlow()

    private val drivingRoutePoints = mutableListOf<Point>()

    fun addDrivingRoutePoint(point: Point) = drivingRoutePoints.add(point)

    fun removeDrivingRoutePoint(point: Point) = drivingRoutePoints.remove(point)

    fun clearDrivingRoutePoints() = drivingRoutePoints.clear()

    var cameraPosition: CameraPosition = CameraPosition(Point(55.733330, 37.587649), 14f, 0f, 0f)
        private set

    var selectedPoint: Point? = null
        private set

    init {
        loadDrivingRoute()
        loadRouteListFlow()
        loadPlaceListFlow()
        loadParticipantsLocationFlow()
    }

    fun savePoint(point: Point) {
        selectedPoint = point
    }

    fun clearPoint() {
        selectedPoint = null
    }

    fun saveCameraPosition(position: CameraPosition) {
        cameraPosition = position
    }

    fun requestDrivingRoute() {
        viewModelScope.launch {
            requestDrivingRouteInteractor.run(drivingRoutePoints)
        }
    }

    fun updateUserLocation(location: com.yandex.mapkit.location.Location) {
        viewModelScope.launch {
            updateUserLocationInteractor.run(location)
                .onFailure {  }
                .onSuccess { requestParticipantsLocationInteractor.run() }
        }
    }

    private fun loadDrivingRoute() {
        viewModelScope.launch(Dispatchers.IO + jobLoadDrivingRoute) {
            loadDrivingRouteFlowInteractor.run()
                .collect { result ->
                    result
                        .onFailure {  }
                        .onSuccess { route ->
                            if (route != null) {
                                _drivingRouteFlow.emit(route)
                            }
                        }
                }
        }
    }

    private fun loadRouteListFlow() {
        viewModelScope.launch(Dispatchers.IO + jobLoadRouteList) {
            loadDrivingRouteListFlowInteractor.run()
                .onSubscription { requestDrivingRouteListFlowInteractor.run() }
                .collect { result ->
                    result
                        .onFailure {  }
                        .onSuccess { _routeListFlow.emit(it) }
                }
        }
    }

    private fun loadPlaceListFlow() {
        viewModelScope.launch(Dispatchers.IO + jobLoadPlaceLabelList) {
            loadPlaceListFlowInteractor.run()
                .onSubscription { requestPlaceListInteractor.run() }
                .collect { result ->
                    result
                        .onFailure { _placeListFlow.emit(listOf()) }
                        .onSuccess { _placeListFlow.emit(it) }
                }
        }
    }

    private fun loadParticipantsLocationFlow() {
        viewModelScope.launch(Dispatchers.IO + jobLoadParticipantsLocation) {
            loadParticipantsLocationFlow.run()
                .onSubscription { requestParticipantsLocationInteractor.run() }
                .collect { result ->
                    result
                        .onFailure {  }
                        .onSuccess { _participantsLocationFlow.emit(it) }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobLoadDrivingRoute.cancel()
        jobLoadRouteList.cancel()
        jobLoadPlaceLabelList.cancel()
        jobLoadParticipantsLocation.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class ExploreViewModelFactory @Inject constructor(
        private val loadDrivingRouteFlowInteractor: Provider<LoadDrivingRouteFlowInteractor>,
        private val loadDrivingRouteListFlowInteractor: Provider<LoadDrivingRouteListFlowInteractor>,
        private val loadPlaceListFlowInteractor: Provider<LoadPlaceListFlowInteractor>,
        private val loadParticipantsLocationFlow: Provider<LoadParticipantsLocationFlow>,
        private val requestDrivingRouteInteractor: Provider<RequestDrivingRouteInteractor>,
        private val requestDrivingRouteListFlowInteractor: Provider<RequestDrivingRouteListFlowInteractor>,
        private val requestPlaceListInteractor: Provider<RequestPlaceListInteractor>,
        private val requestParticipantsLocationInteractor: Provider<RequestParticipantsLocationInteractor>,
        private val updateUserLocationInteractor: Provider<UpdateUserLocationInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ExploreViewModel(
                loadDrivingRouteFlowInteractor.get(),
                loadDrivingRouteListFlowInteractor.get(),
                loadPlaceListFlowInteractor.get(),
                loadParticipantsLocationFlow.get(),
                requestDrivingRouteInteractor.get(),
                requestDrivingRouteListFlowInteractor.get(),
                requestPlaceListInteractor.get(),
                requestParticipantsLocationInteractor.get(),
                updateUserLocationInteractor.get(),
            ) as T
        }
    }
}