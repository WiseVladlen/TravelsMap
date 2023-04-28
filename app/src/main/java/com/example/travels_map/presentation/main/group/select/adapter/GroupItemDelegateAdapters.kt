package com.example.travels_map.presentation.main.group.select.adapter

import com.example.travels_map.R
import com.example.travels_map.databinding.ModelContentCardLayoutBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun groupDelegate(onLayoutClick: (GroupItem) -> Unit) = adapterDelegateViewBinding<GroupItem, GroupItem, ModelContentCardLayoutBinding>(
    { layoutInflater, parent -> ModelContentCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.apply {
            textViewTitle.text = item.group.name
            textViewSubtitle.text = context.resources.getQuantityString(
                R.plurals.group_number_of_participants,
                item.group.participants.size,
                item.group.participants.size,
            )

            imageViewAvatar.setImageResource(R.drawable.ic_baseline_group_24)

            root.setOnClickListener { onLayoutClick(item) }
        }
    }
}