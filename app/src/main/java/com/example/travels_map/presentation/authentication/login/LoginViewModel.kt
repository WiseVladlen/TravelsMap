package com.example.travels_map.presentation.authentication.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.interactors.LogInInteractor
import com.example.travels_map.domain.models.UserLoginData
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

private val TAG = LoginViewModel::class.simpleName

class LoginViewModel(private val logInInteractor: LogInInteractor) : ViewModel() {

    private val job: CompletableJob = SupervisorJob()

    fun logIn(username: String, password: String, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + job) {
            logInInteractor.run(UserLoginData(username, password))
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
        job.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class LoginViewModelFactory @Inject constructor(
        private val logInInteractor: Provider<LogInInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(logInInteractor.get()) as T
        }
    }
}