package com.example.travels_map.presentation.main.explore.map_controller

import com.example.travels_map.domain.entities.Place

interface OnMapClickListener {
    fun onMapClick()
    fun onMapLongClick(place: Place)
    fun onMapObjectClick(place: Place)
    fun onGeoObjectClick(place: Place)
}