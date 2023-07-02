package com.example.travels_map.presentation.main.explore

import androidx.lifecycle.ViewModel
import com.example.travels_map.domain.entities.Place

class SelectedPlaceViewModel : ViewModel() {

    lateinit var value: Place
        private set

    fun save(place: Place) {
        value = place
    }
}