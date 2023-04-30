package com.example.travels_map.presentation.main.account.edit_profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.interactors.EditUserProfileInteractor
import com.example.travels_map.domain.interactors.LoadUserFlowInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

private val TAG = EditProfileViewModel::class.simpleName

class EditProfileViewModel(
    private val loadUserFlowInteractor: LoadUserFlowInteractor,
    private val editUserProfileInteractor: EditUserProfileInteractor,
) : ViewModel() {

    private val jobUpdate: CompletableJob = SupervisorJob()

    val userFlow = flow {
        loadUserFlowInteractor.run().first()
            .onFailure { Log.e(TAG, "${it.message}") }
            .onSuccess { user -> emit(user) }
    }.shareIn(
        CoroutineScope(Dispatchers.IO),
        SharingStarted.WhileSubscribed(),
    )

    fun editProfile(username: String, fullName: String, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobUpdate) {
            if (username.isNotBlank() && fullName.isNotBlank()) {
                editUserProfileInteractor.run(username, fullName)
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
    class EditProfileViewModelFactory @Inject constructor(
        private val loadUserFlowInteractor: Provider<LoadUserFlowInteractor>,
        private val editUserProfileInteractor: Provider<EditUserProfileInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditProfileViewModel(
                loadUserFlowInteractor.get(),
                editUserProfileInteractor.get(),
            ) as T
        }
    }
}