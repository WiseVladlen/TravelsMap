package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class OverviewTabDelegationAdapter(
    onRatingChanged: (Float) -> Unit,
) : AsyncListDifferDelegationAdapter<OverviewTabItem>(
    OverviewTabItemDiffCallback,
    placeCoordinatesAdapterDelegate(),
    ratingAdapterDelegate(),
    rateAndReviewAdapterDelegate(onRatingChanged),
    reviewAdapterDelegate(),
)