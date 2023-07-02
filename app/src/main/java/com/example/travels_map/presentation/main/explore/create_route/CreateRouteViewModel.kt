package com.example.travels_map.presentation.main.explore.create_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.interactors.CreateRouteInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class CreateRouteViewModel(
    private val createRouteInteractor: CreateRouteInteractor,
) : ViewModel() {

    private val jobSave: CompletableJob = SupervisorJob()

    fun createRoute(name: String, navigationCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobSave) {
            createRouteInteractor.run(name)
                .onFailure {  }
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
    class CreateRouteViewModelFactory @Inject constructor(
        private val createRouteInteractor: Provider<CreateRouteInteractor>
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateRouteViewModel(createRouteInteractor.get()) as T
        }
    }
}