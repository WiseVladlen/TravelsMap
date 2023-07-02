package com.example.travels_map.presentation.main.explore.route_configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.interactors.CancelDrivingSessionInteractor
import com.example.travels_map.domain.interactors.LoadDrivingRouteFlowInteractor
import com.yandex.mapkit.directions.driving.DrivingRoute
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class RouteConfigurationViewModel(
    private val loadDrivingRouteFlowInteractor: LoadDrivingRouteFlowInteractor,
    private val cancelDrivingSessionInteractor: CancelDrivingSessionInteractor,
) : ViewModel() {

    private val jobLoad: CompletableJob = SupervisorJob()

    private val _drivingRouteFlow = MutableSharedFlow<DrivingRoute?>()
    val drivingRouteFlow = _drivingRouteFlow.asSharedFlow()

    fun cancelDrivingSession() = cancelDrivingSessionInteractor.run()

    init {
        viewModelScope.launch(Dispatchers.IO + jobLoad) {
            loadDrivingRouteFlowInteractor.run().collect { result ->
                result
                    .onFailure { }
                    .onSuccess { _drivingRouteFlow.emit(it) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobLoad.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class RouteConfigurationViewModelFactory @Inject constructor(
        private val loadDrivingRouteFlowInteractor: Provider<LoadDrivingRouteFlowInteractor>,
        private val cancelDrivingSessionInteractor: Provider<CancelDrivingSessionInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RouteConfigurationViewModel(
                loadDrivingRouteFlowInteractor.get(),
                cancelDrivingSessionInteractor.get(),
            ) as T
        }
    }
}