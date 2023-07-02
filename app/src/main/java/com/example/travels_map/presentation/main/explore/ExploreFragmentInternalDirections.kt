package com.example.travels_map.presentation.main.explore

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.travels_map.R
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.presentation.main.explore.manage_place.ManagePlaceFragment
import com.example.travels_map.presentation.main.explore.create_route.CreateRouteFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.PlaceInfoBottomSheetFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.create_review.CreateReviewFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview.OverviewFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.ReviewsFragment
import com.example.travels_map.presentation.main.explore.route_configuration.RouteConfigurationFragment

private const val BACK_STACK_NAME = "Default back stack"

object ExploreFragmentInternalDirections {

    @IdRes
    private val containerViewId: Int = R.id.fragment_container

    fun ExploreFragment.actionOpenManageLabelBottomSheetFragment(place: Place) {
        childFragmentManager.apply {
            if (backStackEntryCount > 0) {
                popBackStack()
            }

            commit {
                add(containerViewId, PlaceInfoBottomSheetFragment.newInstance(place))
                addToBackStack(BACK_STACK_NAME)
                setReorderingAllowed(true)
            }
        }
    }

    fun ExploreFragment.actionClosePlaceInfoBottomSheetFragment() {
        childFragmentManager.popBackStack()
    }

    fun PlaceInfoBottomSheetFragment.actionClosePlaceInfoBottomSheetFragment() {
        parentFragmentManager.popBackStack()
    }

    fun PlaceInfoBottomSheetFragment.actionOpenRouteConfigurationFragment() {
        parentFragmentManager.commit {
            add(containerViewId, RouteConfigurationFragment.newInstance())
            addToBackStack(null)
            setReorderingAllowed(true)
        }
    }

    fun RouteConfigurationFragment.actionCloseFragment() {
        parentFragmentManager.popBackStack()
    }

    fun PlaceInfoBottomSheetFragment.actionOpenManagePlaceFragment() {
        parentFragmentManager.commit {
            add(containerViewId, ManagePlaceFragment.newInstance())
            addToBackStack(null)
            setReorderingAllowed(true)
        }
    }

    fun ManagePlaceFragment.actionCloseFragment() {
        parentFragmentManager.popBackStack()
    }

    fun OverviewFragment.actionOpenCreateReviewFragment(rating: Float) {
        requireParentFragment().actionOpenCreateReviewFragment(rating)
    }

    fun ReviewsFragment.actionOpenCreateReviewFragment(rating: Float) {
        requireParentFragment().actionOpenCreateReviewFragment(rating)
    }

    private fun Fragment.actionOpenCreateReviewFragment(rating: Float) {
        parentFragmentManager.commit {
            add(containerViewId, CreateReviewFragment.newInstance(rating))
            addToBackStack(null)
            setReorderingAllowed(true)
        }
    }

    fun CreateReviewFragment.actionCloseFragment() {
        parentFragmentManager.popBackStack()
    }

    fun RouteConfigurationFragment.actionCreateRouteFragment() {
        parentFragmentManager.commit {
            add(containerViewId, CreateRouteFragment.newInstance())
            addToBackStack(null)
            setReorderingAllowed(true)
        }
    }

    fun CreateRouteFragment.actionCloseFragment() {
        parentFragmentManager.popBackStack()
    }

    fun CreateRouteFragment.actionNavigateToPlaceInfoBottomSheetFragment() {
        parentFragmentManager.popBackStack(BACK_STACK_NAME, 0)
    }
}