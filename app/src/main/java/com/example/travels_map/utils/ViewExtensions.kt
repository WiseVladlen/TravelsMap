package com.example.travels_map.utils

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun View.onTouch(touch: (view: View, motionEvent: MotionEvent) -> Unit) {
    setOnTouchListener { v, event ->
        touch(v, event)
        v.performClick()
        return@setOnTouchListener true
    }
}

@Suppress("UNUSED_PARAMETER")
fun handleTouch(v: View, event: MotionEvent) = Unit

fun ViewPager2.disableNestedScrolling() {
    (getChildAt(0) as? RecyclerView)?.apply {
        isNestedScrollingEnabled = false
        overScrollMode = View.OVER_SCROLL_NEVER
    }
}

fun <T : View> BottomSheetBehavior<T>.hide() {
    isHideable = true
    state = BottomSheetBehavior.STATE_HIDDEN
    isHideable = false
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}