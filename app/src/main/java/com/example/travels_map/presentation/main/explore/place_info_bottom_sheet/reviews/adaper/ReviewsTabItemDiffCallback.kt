package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.adaper

import androidx.recyclerview.widget.DiffUtil

object ReviewsTabItemDiffCallback : DiffUtil.ItemCallback<ReviewsTabItem>() {

    override fun areItemsTheSame(oldItem: ReviewsTabItem, newItem: ReviewsTabItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ReviewsTabItem, newItem: ReviewsTabItem): Boolean {
        return oldItem == newItem
    }
}