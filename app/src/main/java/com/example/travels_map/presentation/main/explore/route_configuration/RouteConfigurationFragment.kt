package com.example.travels_map.presentation.main.explore.route_configuration

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentRouteConfigurationBinding
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionCloseFragment
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionCreateRouteFragment
import com.example.travels_map.presentation.main.explore.MapControllerViewModel
import com.example.travels_map.utils.handleTouch
import com.example.travels_map.utils.hide
import com.example.travels_map.utils.launchWhenCreated
import com.example.travels_map.utils.onTouch
import com.example.travels_map.utils.show
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class RouteConfigurationFragment : Fragment(R.layout.fragment_route_configuration) {

    private val binding by viewBinding(FragmentRouteConfigurationBinding::bind)

    @Inject
    lateinit var routeConfigurationViewModelFactory: RouteConfigurationViewModel.RouteConfigurationViewModelFactory
    private val viewModel by viewModels<RouteConfigurationViewModel> { routeConfigurationViewModelFactory }

    private val mapControllerViewModel by viewModels<MapControllerViewModel>({ requireParentFragment() })

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapControllerViewModel.activateRouteConfigurationController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.cancelDrivingSession()
        mapControllerViewModel.deactivateRouteConfigurationController()
    }

    private fun initializeView() {
        setupTouchListeners()
        observeDataChanges()
        setupOnClickListeners()
    }

    private fun setupTouchListeners() {
        binding.root.onTouch(::handleTouch)
    }

    private fun observeDataChanges() {
        viewModel.drivingRouteFlow.onEach { route ->
            if (route == null) {
                return@onEach binding.subhead.hide()
            }

            binding.apply {
                subhead.show()

                textViewTotalTripTime.text = getString(
                    R.string.route_configuration_total_trip_time,
                    route.metadata.weight.time.text,
                )

                textViewTotalTripDistance.text = getString(
                    R.string.route_configuration_total_trip_distance,
                    route.metadata.weight.distance.text,
                )
            }
        }.launchWhenCreated(this)
    }

    private fun setupOnClickListeners() {
        binding.apply {
            backButton.setOnClickListener {
                actionCloseFragment()
            }

            doneButton.setOnClickListener {
                actionCreateRouteFragment()
            }
        }
    }

    companion object {
        fun newInstance() = RouteConfigurationFragment()
    }
}