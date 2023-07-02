package com.example.travels_map.presentation.main.group.add_participant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travels_map.domain.interactors.LoadGroupKeyInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Provider

class AddParticipantViewModel(
    private val loadGroupKeyInteractor: LoadGroupKeyInteractor,
) : ViewModel() {

    private val jobLoad: CompletableJob = SupervisorJob()

    private val _loadingStateFlow = MutableStateFlow(true)
    val loadingStateFlow = _loadingStateFlow.asStateFlow()

    val groupKeyFlow: SharedFlow<String> = flow {
        loadGroupKeyInteractor.run().let { result ->
            result
                .onFailure {  }
                .onSuccess { emit(it) }

            _loadingStateFlow.emit(false)
        }
    }.shareIn(
        CoroutineScope(Dispatchers.IO + jobLoad),
        SharingStarted.WhileSubscribed(),
    )

    override fun onCleared() {
        super.onCleared()
        jobLoad.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class AddParticipantViewModelFactory @Inject constructor(
        private val loadGroupKeyInteractor: Provider<LoadGroupKeyInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddParticipantViewModel(loadGroupKeyInteractor.get()) as T
        }
    }
}