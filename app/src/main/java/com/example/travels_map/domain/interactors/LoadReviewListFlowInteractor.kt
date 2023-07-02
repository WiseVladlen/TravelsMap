package com.example.travels_map.domain.interactors

import com.example.travels_map.domain.repositories.IReviewRepository
import javax.inject.Inject

class LoadReviewListFlowInteractor @Inject constructor(private val reviewRepository: IReviewRepository) {

    fun run() = reviewRepository.reviewListFlow
}