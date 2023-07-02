package com.example.travels_map.presentation.main.explore.manage_place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.interactors.SavePlaceInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class ManagePlaceViewModel(private val savePlaceInteractor: SavePlaceInteractor) : ViewModel() {

    private val jobSave: CompletableJob = SupervisorJob()

    fun savePlace(place: Place, navigationCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobSave) {
            savePlaceInteractor.run(place)
                .onFailure { }
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        navigationCallback()
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobSave.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class ManagePlaceViewModelFactory @Inject constructor(
        private val savePlaceInteractor: Provider<SavePlaceInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ManagePlaceViewModel(savePlaceInteractor.get()) as T
        }
    }
}