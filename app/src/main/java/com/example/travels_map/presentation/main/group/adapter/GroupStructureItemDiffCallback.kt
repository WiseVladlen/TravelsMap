package com.example.travels_map.presentation.main.group.adapter

import androidx.recyclerview.widget.DiffUtil

object GroupStructureItemDiffCallback : DiffUtil.ItemCallback<GroupStructureItem>() {

    override fun areItemsTheSame(oldItem: GroupStructureItem, newItem: GroupStructureItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GroupStructureItem, newItem: GroupStructureItem): Boolean {
        return oldItem == newItem
    }
}