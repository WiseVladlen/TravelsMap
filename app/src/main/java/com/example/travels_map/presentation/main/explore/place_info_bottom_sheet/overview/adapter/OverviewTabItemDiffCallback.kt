package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview.adapter

import androidx.recyclerview.widget.DiffUtil

object OverviewTabItemDiffCallback : DiffUtil.ItemCallback<OverviewTabItem>() {

    override fun areItemsTheSame(oldItem: OverviewTabItem, newItem: OverviewTabItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: OverviewTabItem, newItem: OverviewTabItem): Boolean {
        return oldItem == newItem
    }
}