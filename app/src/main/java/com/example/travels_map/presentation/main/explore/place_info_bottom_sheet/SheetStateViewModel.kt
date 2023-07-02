package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet

import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SheetStateViewModel : ViewModel() {

    var state = BottomSheetBehavior.STATE_COLLAPSED
        private set

    fun saveState(newState: Int) {
        if (newState != BottomSheetBehavior.STATE_HIDDEN) {
            state = newState
        }
    }
}