package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.interactors.LoadPlaceFlowInteractor
import com.example.travels_map.domain.interactors.RequestPlaceInteractor
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

class PlaceInfoViewModel(
    private val loadPlaceFlowInteractor: LoadPlaceFlowInteractor,
    private val requestPlaceInteractor: RequestPlaceInteractor,
) : ViewModel() {

    private val jobLoad: CompletableJob = SupervisorJob()

    private val _placeFlow = MutableSharedFlow<Place>(0, 1, BufferOverflow.DROP_OLDEST)
    val placeFlow = _placeFlow.asSharedFlow()

    fun requestPlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO + jobLoad) {
            loadPlaceFlowInteractor.run()
                .onSubscription { requestPlaceInteractor.run(place) }
                .collect { result ->
                    result
                        .onFailure {  }
                        .onSuccess { _placeFlow.emit(it) }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobLoad.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class ManagePlaceViewModelFactory @Inject constructor(
        private val loadPlaceFlowInteractor: Provider<LoadPlaceFlowInteractor>,
        private val requestPlaceInteractor: Provider<RequestPlaceInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaceInfoViewModel(
                loadPlaceFlowInteractor.get(),
                requestPlaceInteractor.get(),
            ) as T
        }
    }
}