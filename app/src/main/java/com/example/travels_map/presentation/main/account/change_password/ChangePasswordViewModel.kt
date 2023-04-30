package com.example.travels_map.presentation.main.account.change_password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.interactors.ChangeUserPasswordInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

private val TAG = ChangePasswordViewModel::class.simpleName

class ChangePasswordViewModel(
    private val changeUserPasswordInteractor: ChangeUserPasswordInteractor,
) : ViewModel() {

    private val jobUpdate: CompletableJob = SupervisorJob()

    fun changePassword(newPassword: String, newPasswordAgain: String, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobUpdate) {
            if (newPassword.isNotBlank() && newPasswordAgain.isNotBlank() && newPassword == newPasswordAgain) {
                changeUserPasswordInteractor.run(newPassword)
                    .onFailure { Log.e(TAG, "${it.message}") }
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            callback()
                        }
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobUpdate.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class ChangePasswordViewModelFactory @Inject constructor(
        private val changeUserPasswordInteractor: Provider<ChangeUserPasswordInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChangePasswordViewModel(changeUserPasswordInteractor.get()) as T
        }
    }
}