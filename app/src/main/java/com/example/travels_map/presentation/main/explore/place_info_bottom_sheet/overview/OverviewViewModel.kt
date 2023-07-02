package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.entities.Review
import com.example.travels_map.domain.interactors.LoadPlaceFlowInteractor
import com.example.travels_map.domain.interactors.LoadReviewListFlowInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class OverviewViewModel(
    private val loadReviewListFlowInteractor: LoadReviewListFlowInteractor,
    private val loadPlaceFlowInteractor: LoadPlaceFlowInteractor,
) : ViewModel() {

    private val jobLoad: CompletableJob = SupervisorJob()

    private val _placeFlow = MutableSharedFlow<Place>(1, 1, BufferOverflow.DROP_OLDEST)
    val placeFlow = _placeFlow.asSharedFlow()

    private val _reviewListFlow = MutableSharedFlow<List<Review>>(1, 1, BufferOverflow.DROP_OLDEST)
    val reviewListFlow = _reviewListFlow.asSharedFlow()

    init {
        loadReviewList()
        loadPlace()
    }

    private fun loadReviewList() {
        viewModelScope.launch(Dispatchers.IO + jobLoad) {
            loadReviewListFlowInteractor.run().collect { result ->
                result
                    .onFailure {  }
                    .onSuccess { _reviewListFlow.emit(it) }
            }
        }
    }

    private fun loadPlace() {
        viewModelScope.launch(Dispatchers.IO + jobLoad) {
            loadPlaceFlowInteractor.run()
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
    class OverviewViewModelFactory @Inject constructor(
        private val loadReviewListFlowInteractor: Provider<LoadReviewListFlowInteractor>,
        private val loadPlaceFlowInteractor: Provider<LoadPlaceFlowInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OverviewViewModel(
                loadReviewListFlowInteractor.get(),
                loadPlaceFlowInteractor.get(),
            ) as T
        }
    }
}