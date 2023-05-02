package com.example.travels_map.presentation.main.group.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.entities.User

sealed class GroupStructureItem {
    class HeaderItem(val group: Group) : GroupStructureItem()
    class ParticipantCountItem(val count: Int) : GroupStructureItem()
    class ParticipantItem(val user: User) : GroupStructureItem()

    sealed class ActionItem(val attributes: Attributes) : GroupStructureItem() {
        class AddParticipant(attributes: Attributes) : ActionItem(attributes)
        class Switch(attributes: Attributes) : ActionItem(attributes)
        class Leave(attributes: Attributes) : ActionItem(attributes)
    }
}

data class Attributes(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
)