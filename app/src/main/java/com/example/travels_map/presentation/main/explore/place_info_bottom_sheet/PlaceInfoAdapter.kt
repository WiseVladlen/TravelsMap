package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PlaceInfoAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var mList: List<Fragment> = listOf()

    fun submitList(list: List<Fragment>) {
        mList = list
    }

    override fun getItemCount(): Int = mList.size

    override fun createFragment(position: Int): Fragment = mList[position]
}