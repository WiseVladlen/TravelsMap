package com.example.travels_map.presentation.main.group.select.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class GroupItemDelegationAdapter(
    onLayoutClick: (GroupItem) -> Unit,
) : AsyncListDifferDelegationAdapter<GroupItem>(
    GroupItemDiffCallback,
    groupDelegate(onLayoutClick),
)