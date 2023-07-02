package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview.adapter

import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.entities.Review

sealed class OverviewTabItem {
    object RateAndReviewItem : OverviewTabItem()

    class PlaceCoordinatesItem(val place: Place) : OverviewTabItem()
    class RatingItem(val place: Place) : OverviewTabItem()
    class ReviewItem(val review: Review) : OverviewTabItem()
}