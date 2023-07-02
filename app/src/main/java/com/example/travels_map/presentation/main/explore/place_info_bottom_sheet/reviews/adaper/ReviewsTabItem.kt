package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.adaper

import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.entities.Review

sealed class ReviewsTabItem {
    object RateAndReviewItem : ReviewsTabItem()

    class RatingItem(val place: Place) : ReviewsTabItem()
    class ReviewItem(val review: Review) : ReviewsTabItem()
}