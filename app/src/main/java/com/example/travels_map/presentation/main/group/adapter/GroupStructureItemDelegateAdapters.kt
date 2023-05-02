package com.example.travels_map.presentation.main.group.adapter

import com.example.travels_map.R
import com.example.travels_map.databinding.HeaderCardLayoutBinding
import com.example.travels_map.databinding.ModelContentCardLayoutBinding
import com.example.travels_map.databinding.TextCardLayoutBinding
import com.example.travels_map.databinding.TextIconCardLayoutBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun headerAdapterDelegate() = adapterDelegateViewBinding<GroupStructureItem.HeaderItem, GroupStructureItem, HeaderCardLayoutBinding>(
    { layoutInflater, parent -> HeaderCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.root.apply {
            text = item.group.name
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_cloud_circle_96, 0, 0, 0)
        }
    }
}

fun actionAdapterDelegate(onLayoutClick: (GroupStructureItem.ActionItem) -> Unit) = adapterDelegateViewBinding<GroupStructureItem.ActionItem, GroupStructureItem, TextIconCardLayoutBinding>(
    { layoutInflater, parent -> TextIconCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.root.apply {
            text = getString(item.attributes.title)
            setCompoundDrawablesWithIntrinsicBounds(item.attributes.icon, 0, 0, 0)

            setOnClickListener { onLayoutClick(item) }
        }
    }
}

fun participantCountDelegate() = adapterDelegateViewBinding<GroupStructureItem.ParticipantCountItem, GroupStructureItem, TextCardLayoutBinding>(
    { layoutInflater, parent -> TextCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.root.apply {
            text = resources.getQuantityString(
                R.plurals.group_number_of_participants,
                item.count,
                item.count,
            )
        }
    }
}

fun participantDelegate() = adapterDelegateViewBinding<GroupStructureItem.ParticipantItem, GroupStructureItem, ModelContentCardLayoutBinding>(
    { layoutInflater, parent -> ModelContentCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.apply {
            textViewTitle.text = item.user.username
            textViewSubtitle.text = item.user.fullName

            imageViewAvatar.setImageResource(R.drawable.ic_baseline_person_24)
        }
    }
}