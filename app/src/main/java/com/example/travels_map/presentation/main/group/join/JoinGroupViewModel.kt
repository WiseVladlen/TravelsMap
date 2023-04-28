package com.example.travels_map.presentation.main.group.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.interactors.JoinGroupInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class JoinGroupViewModel(
    private val joinGroupInteractor: JoinGroupInteractor,
) : ViewModel() {

    private val jobUpdate: CompletableJob = SupervisorJob()

    fun joinGroup(query: String, navigationCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobUpdate) {
            joinGroupInteractor.run(query)
            withContext(Dispatchers.Main) {
                navigationCallback()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobUpdate.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class JoinGroupViewModelFactory @Inject constructor(
        private val joinGroupInteractor: Provider<JoinGroupInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return JoinGroupViewModel(joinGroupInteractor.get()) as T
        }
    }
}