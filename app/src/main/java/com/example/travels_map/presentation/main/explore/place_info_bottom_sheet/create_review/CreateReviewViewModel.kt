package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.create_review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.interactors.AddFeedbackInteractor
import com.example.travels_map.domain.interactors.RequestPlaceInteractor
import com.example.travels_map.domain.models.FeedbackData
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Provider

class CreateReviewViewModel(
    private val addFeedbackInteractor: AddFeedbackInteractor,
    private val requestPlaceInteractor: RequestPlaceInteractor,
) : ViewModel() {

    private val jobCreate: CompletableJob = SupervisorJob()

    fun createReview(text: String, rating: Int, place: Place, navigationCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobCreate) {
            addFeedbackInteractor.run(FeedbackData(text, rating, Date()), place)
                .onFailure {  }
                .onSuccess {
                    requestPlaceInteractor.run(place)

                    withContext(Dispatchers.Main) {
                        navigationCallback()
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobCreate.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class CreateReviewViewModelFactory @Inject constructor(
        private val addFeedbackInteractor: Provider<AddFeedbackInteractor>,
        private val requestPlaceInteractor: Provider<RequestPlaceInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateReviewViewModel(
                addFeedbackInteractor.get(),
                requestPlaceInteractor.get(),
            ) as T
        }
    }
}