package com.example.travels_map.presentation.main.group.select.adapter

import androidx.recyclerview.widget.DiffUtil

object GroupItemDiffCallback : DiffUtil.ItemCallback<GroupItem>() {

    override fun areItemsTheSame(oldItem: GroupItem, newItem: GroupItem): Boolean {
        return oldItem.group.id == newItem.group.id
    }

    override fun areContentsTheSame(oldItem: GroupItem, newItem: GroupItem): Boolean {
        return oldItem == newItem
    }
}