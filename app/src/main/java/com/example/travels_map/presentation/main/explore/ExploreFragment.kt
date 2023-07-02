package com.example.travels_map.presentation.main.explore

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.BuildConfig
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentExploreBinding
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionClosePlaceInfoBottomSheetFragment
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionOpenManageLabelBottomSheetFragment
import com.example.travels_map.presentation.main.explore.map_controller.MapController
import com.example.travels_map.presentation.main.explore.map_controller.OnMapClickListener
import com.example.travels_map.presentation.main.explore.map_controller.OnRouteConfigurationListener
import com.example.travels_map.utils.launchWhenCreated
import com.example.travels_map.utils.location.LocationManagerUtil.isLocationPermissionsGranted
import com.example.travels_map.utils.location.LocationManagerUtil.requestLocationPermissions
import com.example.travels_map.utils.location.LocationManagerUtil.subscribeForLocationUpdates
import com.example.travels_map.utils.map.updateCameraPosition
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    @Inject
    lateinit var exploreViewModelFactory: ExploreViewModel.ExploreViewModelFactory
    private val viewModel by navGraphViewModels<ExploreViewModel>(R.id.nav_graph) { exploreViewModelFactory }

    private val mapControllerViewModel by viewModels<MapControllerViewModel>()

    private val binding by viewBinding(FragmentExploreBinding::bind)

    private lateinit var locationManager: LocationManager

    private val locationListener = object : LocationListener {
        override fun onLocationUpdated(newLocation: Location) {
            viewModel.updateUserLocation(newLocation)
        }

        override fun onLocationStatusUpdated(p0: LocationStatus) {}
    }

    private lateinit var mapView: MapView

    private lateinit var mapController: MapController

    private val onChildBackStackChangedListener = OnBackStackChangedListener {
        if (childFragmentManager.backStackEntryCount == 0) {
            mapController.clearActiveObject()
            viewModel.clearPoint()
        }
    }

    private val mapClickListener = object : OnMapClickListener {
        override fun onMapClick() {
            actionClosePlaceInfoBottomSheetFragment()
        }

        override fun onMapLongClick(place: Place) {
            viewModel.savePoint(place.coordinates)
            actionOpenManageLabelBottomSheetFragment(place)
        }

        override fun onMapObjectClick(place: Place) {
            viewModel.savePoint(place.coordinates)
            actionOpenManageLabelBottomSheetFragment(place)
        }

        override fun onGeoObjectClick(place: Place) {
            viewModel.savePoint(place.coordinates)
            actionOpenManageLabelBottomSheetFragment(place)
        }
    }

    private val routeConfigurationListener = object : OnRouteConfigurationListener {
        override fun addPoint(point: Point) {
            viewModel.apply {
                addDrivingRoutePoint(point)
                requestDrivingRoute()
            }
        }

        override fun removePoint(point: Point) {
            viewModel.apply {
                removeDrivingRoutePoint(point)
                requestDrivingRoute()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(BuildConfig.YANDEX_API_KEY)
        MapKitFactory.initialize(context)

        locationManager = MapKitFactory.getInstance().createLocationManager().apply {
            subscribeForLocationUpdates(locationListener)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
        childFragmentManager.addOnBackStackChangedListener(onChildBackStackChangedListener)
    }

    override fun onStop() {
        viewModel.saveCameraPosition(mapView.map.cameraPosition)
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
        childFragmentManager.removeOnBackStackChangedListener(onChildBackStackChangedListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearDrivingRoutePoints()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.unsubscribe(locationListener)
    }

    private fun initializeView() {
        setupMapView()
        setupMapController()
        observeDataChanges()
        setupOnClickListeners()
    }

    private fun setupMapView() {
        mapView = binding.mapView.apply {
            map.move(viewModel.cameraPosition, Animation(Animation.Type.SMOOTH, 0f), null)
        }
    }

    private fun setupMapController() {
        mapController = MapController(mapView.map, mapClickListener, routeConfigurationListener)

        mapControllerViewModel.activatedController.onEach { controller ->
            when (controller) {
                MapControllerType.DEFAULT_CONTROLLER -> {
                    viewModel.clearDrivingRoutePoints()
                    mapController.activateDefaultController(viewModel.selectedPoint)
                }
                MapControllerType.ROUTE_CONFIGURATION_CONTROLLER -> {
                    mapController.activateRouteConfigurationMode(viewModel.selectedPoint)
                }
            }
        }.launchWhenCreated(this)
    }

    private fun observeDataChanges() {
        viewModel.routeListFlow.onEach { result ->
            mapController.addRouteList(result)
        }.launchWhenCreated(this)

        viewModel.groupParticipantListFlow.onEach { result ->
            mapController.addUserList(result)
        }.launchWhenCreated(this)

        viewModel.placeListFlow.onEach { result ->
            mapController.addPlaceList(result)
        }.launchWhenCreated(this)

        viewModel.drivingRouteFlow.onEach { route ->
            mapController.drawDrivingRoute(route)
        }.launchWhenCreated(this)
    }

    private fun setupOnClickListeners() {
        binding.determineUserLocationFab.setOnClickListener {
            if (!requireContext().isLocationPermissionsGranted()) {
                requireActivity().requestLocationPermissions()
            }

            viewModel.user?.location?.let { point ->
                mapView.map.updateCameraPosition(point)
            }
        }
    }
}