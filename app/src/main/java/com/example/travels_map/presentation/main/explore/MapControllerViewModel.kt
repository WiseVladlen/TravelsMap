package com.example.travels_map.presentation.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class MapControllerType {
    DEFAULT_CONTROLLER,
    ROUTE_CONFIGURATION_CONTROLLER,
}

class MapControllerViewModel : ViewModel() {

    private val _activatedController = MutableStateFlow(MapControllerType.DEFAULT_CONTROLLER)
    val activatedController = _activatedController.asStateFlow()

    fun activateRouteConfigurationController() {
        viewModelScope.launch {
            _activatedController.emit(MapControllerType.ROUTE_CONFIGURATION_CONTROLLER)
        }
    }

    fun deactivateRouteConfigurationController() {
        viewModelScope.launch {
            _activatedController.emit(MapControllerType.DEFAULT_CONTROLLER)
        }
    }
}