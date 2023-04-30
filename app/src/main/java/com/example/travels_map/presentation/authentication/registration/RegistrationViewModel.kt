package com.example.travels_map.presentation.authentication.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.interactors.SignUpInteractor
import com.example.travels_map.domain.models.UserRegistrationData
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

private val TAG = RegistrationViewModel::class.simpleName

class RegistrationViewModel(private val signUpInteractor: SignUpInteractor) : ViewModel() {

    private val job: CompletableJob = SupervisorJob()

    fun signUp(username: String, fullName: String, password: String, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + job) {
            signUpInteractor.run(UserRegistrationData(username, fullName, password))
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
    class RegistrationViewModelFactory @Inject constructor(
        private val signUpInteractor: Provider<SignUpInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegistrationViewModel(signUpInteractor.get()) as T
        }
    }
}