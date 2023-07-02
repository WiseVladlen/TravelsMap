package com.example.travels_map.presentation.main.explore.map_controller

import com.example.travels_map.domain.entities.Place
import com.example.travels_map.domain.entities.Route
import com.example.travels_map.domain.entities.User
import com.example.travels_map.utils.map.MapObjectUtil
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider

class MapController(
    yandexMap: Map,
    mapClickListener: OnMapClickListener,
    routeConfigurationListener: OnRouteConfigurationListener,
) {

    private val map = yandexMap

    private val defaultController = DefaultController(map, mapClickListener)
    private val routeConfigurationController = RouteConfigurationController(map, routeConfigurationListener)

    private val controllers = listOf(defaultController, routeConfigurationController)

    private var currentController: IMapController? = null

    fun activateDefaultController(selectedPoint: Point?) {
        deactivateMapController(routeConfigurationController)
        activateMapController(defaultController, selectedPoint)
    }

    fun activateRouteConfigurationMode(selectedPoint: Point?) {
        deactivateMapController(defaultController)
        activateMapController(routeConfigurationController, selectedPoint)
    }

    private fun deactivateMapController(controller: IMapController) {
        map.removeInputListener(controller)

        map.removeTapListener(controller)

        controllers.forEach {
            it.getPlaceMarkCollections().forEach { collection ->
                collection.removeTapListener(controller)
            }
        }

        controller.onDeactivate()
    }

    private fun activateMapController(controller: IMapController, selectedPoint: Point?) {
        if (controller != currentController) {
            currentController = controller
        } else {
            return
        }

        map.addInputListener(controller)

        map.addTapListener(controller)

        controllers.forEach {
            it.getPlaceMarkCollections().forEach { collection ->
                collection.addTapListener(controller)
            }
        }

        if (selectedPoint != null) {
            controller.onActivate(selectedPoint)
        }
    }

    fun clearActiveObject() = defaultController.clearActiveObject()

    fun drawDrivingRoute(drivingRoute: DrivingRoute) = routeConfigurationController.drawRoute(drivingRoute)

    fun addPlaceList(list: List<Place>) {
        with(defaultController.getPlacePointCollection()) {
            clear()

            list.forEach { place ->
                addPlacemark(
                    place.coordinates,
                    ImageProvider.fromBitmap(MapObjectUtil.Place.drawBitmap())
                ).apply {
                    userData = place
                }
            }
        }
    }

    fun addUserList(list: List<User>) {
        with(defaultController.getUserCollection()) {
            clear()

            list.forEach { user ->
                addPlacemark(user.location, ImageProvider.fromBitmap(MapObjectUtil.User.drawBitmap(user.username.first().toString())))
            }
        }
    }

    fun addRouteList(list: List<Route>) {
        defaultController.getPolylineCollection().clear()

        list.forEach { route ->
            defaultController.getPolylineCollection().addPolyline(Polyline(route.wayPoints)).apply {
                userData = route
            }

            route.requestPoints.forEach { point ->
                defaultController.getRoutePointCollection().addPlacemark(
                    point,
                    ImageProvider.fromBitmap(MapObjectUtil.Waypoint.drawBitmap())
                ).apply {
                    userData = Place(
                        isCustomObject = true,
                        coordinates = point,
                    )
                }
            }
        }
    }
}