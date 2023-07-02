package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.adaper

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ReviewsTabItemDelegationAdapter(
    onRatingChanged: (Float) -> Unit,
) : AsyncListDifferDelegationAdapter<ReviewsTabItem>(
    ReviewsTabItemDiffCallback,
    ratingAdapterDelegate(),
    rateAndReviewAdapterDelegate(onRatingChanged),
    reviewAdapterDelegate(),
)