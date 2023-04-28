package com.example.travels_map.presentation.main.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.common.Result
import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.interactors.LeaveGroupInteractor
import com.example.travels_map.domain.interactors.LoadGroupStateInteractor
import com.example.travels_map.domain.interactors.LoadGroupInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class GroupViewModel(
    private val leaveGroupInteractor: LeaveGroupInteractor,
    private val loadGroupStateInteractor: LoadGroupStateInteractor,
    private val loadGroupInteractor: LoadGroupInteractor,
) : ViewModel() {

    private val jobLoad: CompletableJob = SupervisorJob()
    private val jobLeave: CompletableJob = SupervisorJob()

    private val _loadingStateFlow = MutableStateFlow(true)
    val loadingStateFlow = _loadingStateFlow.asStateFlow()

    private val _errorStateFlow = MutableStateFlow(false)
    val errorStateFlow = _errorStateFlow.asStateFlow()

    private val _groupFlow = MutableSharedFlow<Group>(1, 1, BufferOverflow.DROP_OLDEST)
    val groupFlow = _groupFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO + jobLoad) {
            loadGroupStateInteractor.run()
                .onStart { loadGroupInteractor.run() }
                .collect { result ->
                    when (result) {
                        is Result.Error -> _errorStateFlow.emit(true)
                        is Result.Success -> {
                            _groupFlow.emit(result.data)
                            _errorStateFlow.emit(false)
                        }
                    }
                    _loadingStateFlow.emit(false)
                }
        }
    }

    fun leaveGroup() {
        viewModelScope.launch(Dispatchers.IO + jobLeave) {
            leaveGroupInteractor.run()
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobLoad.cancel()
        jobLeave.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class GroupViewModelFactory @Inject constructor(
        private val leaveGroupInteractor: Provider<LeaveGroupInteractor>,
        private val loadGroupStateInteractor: Provider<LoadGroupStateInteractor>,
        private val loadGroupInteractor: Provider<LoadGroupInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroupViewModel(
                leaveGroupInteractor.get(),
                loadGroupStateInteractor.get(),
                loadGroupInteractor.get(),
            ) as T
        }
    }
}