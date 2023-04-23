package com.example.travels_map.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travels_map.domain.interactors.LoadCurrentUserSessionInteractor
import javax.inject.Inject
import javax.inject.Provider

class MainActivityViewModel(
    loadCurrentUserSessionInteractor: LoadCurrentUserSessionInteractor,
) : ViewModel() {

    val currentUserStateFlow = loadCurrentUserSessionInteractor.run().isNotNullStateFlow

    @Suppress("UNCHECKED_CAST")
    class MainActivityViewModelFactory @Inject constructor(
        private val loadCurrentUserSessionInteractor: Provider<LoadCurrentUserSessionInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel(loadCurrentUserSessionInteractor.get()) as T
        }
    }
}