package com.example.travels_map.presentation.main.group.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class GroupStructureItemDelegationAdapter(
    onActionItemClick: (GroupStructureItem.ActionItem) -> Unit,
) : AsyncListDifferDelegationAdapter<GroupStructureItem>(
    GroupStructureItemDiffCallback,
    headerAdapterDelegate(),
    actionAdapterDelegate(onActionItemClick),
    participantCountDelegate(),
    participantDelegate(),
)