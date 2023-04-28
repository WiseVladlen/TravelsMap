package com.example.travels_map.presentation.main.group.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.interactors.CreateGroupInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class CreateGroupViewModel(private val createGroupInteractor: CreateGroupInteractor) : ViewModel() {

    private val jobCreate: CompletableJob = SupervisorJob()

    fun createGroup(name: String, navigationCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobCreate) {
            createGroupInteractor.run(name)
            withContext(Dispatchers.Main) {
                navigationCallback()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobCreate.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class CreateGroupViewModelFactory @Inject constructor(
        private val createGroupInteractor: Provider<CreateGroupInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateGroupViewModel(createGroupInteractor.get()) as T
        }
    }
}