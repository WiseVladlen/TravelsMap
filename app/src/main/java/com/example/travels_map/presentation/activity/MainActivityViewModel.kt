package com.example.travels_map.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travels_map.domain.interactors.LoadCurrentUserInteractor
import javax.inject.Inject
import javax.inject.Provider

class MainActivityViewModel(
    private val loadCurrentUserInteractor: LoadCurrentUserInteractor,
) : ViewModel() {

    val user get() = loadCurrentUserInteractor.run()

    @Suppress("UNCHECKED_CAST")
    class MainActivityViewModelFactory @Inject constructor(
        private val loadCurrentUserInteractor: Provider<LoadCurrentUserInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel(loadCurrentUserInteractor.get()) as T
        }
    }
}