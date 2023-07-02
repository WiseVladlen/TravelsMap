package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentPlaceInfoBottomSheetBinding
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionClosePlaceInfoBottomSheetFragment
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionOpenManagePlaceFragment
import com.example.travels_map.presentation.main.explore.MapControllerType
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionOpenRouteConfigurationFragment
import com.example.travels_map.presentation.main.explore.MapControllerViewModel
import com.example.travels_map.presentation.main.explore.SelectedPlaceViewModel
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview.OverviewFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.ReviewsFragment
import com.example.travels_map.utils.disableNestedScrolling
import com.example.travels_map.utils.hide
import com.example.travels_map.utils.launchWhenCreated
import com.example.travels_map.utils.onTouch
import com.example.travels_map.utils.show
import com.example.travels_map.utils.toString
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import javax.inject.Inject

class PlaceInfoBottomSheetFragment : Fragment(R.layout.fragment_place_info_bottom_sheet) {

    @Inject
    lateinit var placeInfoViewModelFactory: PlaceInfoViewModel.ManagePlaceViewModelFactory
    private val viewModel by viewModels<PlaceInfoViewModel> { placeInfoViewModelFactory }

    private val sheetStateViewModel by viewModels<SheetStateViewModel>()
    private val mapControllerViewModel by viewModels<MapControllerViewModel>({ requireParentFragment() })
    private val selectedPlaceViewModel by viewModels<SelectedPlaceViewModel>({ requireParentFragment() })

    private val binding by viewBinding(FragmentPlaceInfoBottomSheetBinding::bind)

    private lateinit var currentPlace: Place

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { bundle ->
            currentPlace = Place(
                coordinates = Point(bundle.getDouble(LATITUDE), bundle.getDouble(LONGITUDE)),
                isCustomObject = bundle.getBoolean(IS_CUSTOM_OBJECT)
            )

            selectedPlaceViewModel.save(currentPlace)
        }
    }

    private fun observePlaceDataChanges() {
        viewModel.placeFlow
            .onSubscription { viewModel.requestPlace(currentPlace) }
            .onEach { place ->
                if (place.id == null) {
                    binding.textViewTitle.text = getString(
                        R.string.place_coordinates_pattern,
                        place.coordinates.latitude.toString(8),
                        place.coordinates.longitude.toString(8)
                    )
                    return@onEach
                }

                binding.apply {
                    ratingLayout.show()
                    tabLayout.show()
                    labelContentPager.show()

                    textViewTitle.text = place.name

                    place.feedback?.let { feedback ->
                        textViewRating.text = feedback.rating.toString(1)
                        ratingBar.rating = feedback.rating
                        textViewNumberOfReviews.text = getString(R.string.rating_number_of_reviews, feedback.number)
                    }

                    selectedPlaceViewModel.save(place)
                }
            }.launchWhenCreated(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        sheetStateViewModel.saveState(BottomSheetBehavior.from(binding.sheet).state)
    }

    private fun initializeView() {
        setupBottomSheet()
        observePlaceDataChanges()
    }

    private fun setupBottomSheet() {
        binding.sheet.doOnLayout {
            observeMapControllerStateChanges()
        }

        val fragments = listOf(
            OverviewFragment.newInstance(),
            ReviewsFragment.newInstance(),
        )

        val placeInfoAdapter = PlaceInfoAdapter(childFragmentManager, lifecycle)

        binding.apply {
            sheet.onTouch(::onBottomSheetTouch)

            labelContentPager.apply {
                disableNestedScrolling()

                adapter = placeInfoAdapter
                isUserInputEnabled = false
            }

            placeInfoAdapter.submitList(fragments)

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        labelContentPager.setCurrentItem(tab.position, false)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            closeButton.setOnClickListener {
                actionClosePlaceInfoBottomSheetFragment()
            }

            routeChip.setOnClickListener {
                sheetStateViewModel.saveState(BottomSheetBehavior.from(binding.sheet).state)
                actionOpenRouteConfigurationFragment()
            }

            labelChip.setOnClickListener {
                actionOpenManagePlaceFragment()
            }
        }
    }

    private fun observeMapControllerStateChanges() {
        mapControllerViewModel.activatedController.onEach { controller ->
            when (controller) {
                MapControllerType.DEFAULT_CONTROLLER -> {
                    binding.sheet.doOnLayout {
                        BottomSheetBehavior.from(it).apply {
                            peekHeight = binding.run {
                                dragHandleView.height + header.height + chipGroup.height
                            }
                            state = sheetStateViewModel.state
                        }
                    }
                }
                MapControllerType.ROUTE_CONFIGURATION_CONTROLLER -> {
                    binding.sheet.doOnLayout {
                        BottomSheetBehavior.from(it).hide()
                    }
                }
            }
        }.launchWhenCreated(this)
    }

    private fun onBottomSheetTouch(v: View, event: MotionEvent) {
        val bottomSheetBehaviour = BottomSheetBehavior.from(v)

        if (event.action != MotionEvent.ACTION_UP) return

        if (bottomSheetBehaviour.state == BottomSheetBehavior.STATE_COLLAPSED || bottomSheetBehaviour.state == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    companion object {
        private const val LATITUDE = "LATITUDE"
        private const val LONGITUDE = "LONGITUDE"
        private const val IS_CUSTOM_OBJECT = "IS_CUSTOM_OBJECT"

        fun newInstance(place: Place) = PlaceInfoBottomSheetFragment().apply {
            arguments = bundleOf(
                LATITUDE to place.coordinates.latitude,
                LONGITUDE to place.coordinates.longitude,
                IS_CUSTOM_OBJECT to place.isCustomObject,
            )
        }
    }
}