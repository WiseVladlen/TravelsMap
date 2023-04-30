package com.example.travels_map.presentation.main.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.interactors.FetchUserInteractor
import com.example.travels_map.domain.interactors.LoadUserFlowInteractor
import com.example.travels_map.domain.interactors.LogOutInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

private val TAG = AccountViewModel::class.simpleName

class AccountViewModel(
    private val fetchUserInteractor: FetchUserInteractor,
    private val loadUserFlowInteractor: LoadUserFlowInteractor,
    private val logOutInteractor: LogOutInteractor,
) : ViewModel() {

    private val jobLoad: CompletableJob = SupervisorJob()
    private val jobLogOut: CompletableJob = SupervisorJob()

    private val _userFlow = MutableSharedFlow<User>(1, 1, BufferOverflow.DROP_OLDEST)
    val userFlow = _userFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO + jobLoad) {
            loadUserFlowInteractor.run()
                .onStart { fetchUserInteractor.run() }
                .collect { result ->
                    result
                        .onFailure { Log.e(TAG, "${it.message}") }
                        .onSuccess { user -> _userFlow.emit(user) }
                }
        }
    }

    fun logOut(callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobLogOut) {
            logOutInteractor.run()
                .onFailure { Log.e(TAG, "${it.message}") }
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        callback()
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobLoad.cancel()
        jobLogOut.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class AccountViewModelFactory @Inject constructor(
        private val fetchUserInteractor: Provider<FetchUserInteractor>,
        private val loadUserFlowInteractor: Provider<LoadUserFlowInteractor>,
        private val logOutInteractor: Provider<LogOutInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AccountViewModel(
                fetchUserInteractor.get(),
                loadUserFlowInteractor.get(),
                logOutInteractor.get(),
            ) as T
        }
    }
}