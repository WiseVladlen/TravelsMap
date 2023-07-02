package com.example.travels_map.presentation.main.group.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.interactors.LoadAvailableGroupListInteractor
import com.example.travels_map.domain.interactors.SelectGroupInteractor
import com.example.travels_map.presentation.main.group.select.adapter.GroupItem
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class SelectGroupViewModel(
    private val selectGroupInteractor: SelectGroupInteractor,
    private val loadAvailableGroupListInteractor: LoadAvailableGroupListInteractor,
) : ViewModel() {

    private val jobLoad: CompletableJob = SupervisorJob()
    private val jobUpdate: CompletableJob = SupervisorJob()

    private val _loadingStateFlow = MutableStateFlow(true)
    val loadingStateFlow = _loadingStateFlow.asStateFlow()

    val groupListFlow: SharedFlow<List<GroupItem>> = flow {
        loadAvailableGroupListInteractor.run().let { result ->
            result
                .onFailure { emit(emptyList<GroupItem>()) }
                .onSuccess { groupList ->
                    emit(groupList.map { GroupItem(it) })
                }

            _loadingStateFlow.emit(false)
        }
    }.shareIn(
        CoroutineScope(Dispatchers.IO + jobLoad),
        SharingStarted.WhileSubscribed()
    )

    fun selectGroup(group: Group, navigationCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobUpdate) {
            selectGroupInteractor.run(group)
            withContext(Dispatchers.Main) {
                navigationCallback()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobLoad.cancel()
        jobUpdate.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class SelectGroupViewModelFactory @Inject constructor(
        private val selectGroupInteractor: Provider<SelectGroupInteractor>,
        private val loadAvailableGroupListInteractor: Provider<LoadAvailableGroupListInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SelectGroupViewModel(
                selectGroupInteractor.get(),
                loadAvailableGroupListInteractor.get(),
            ) as T
        }
    }
}