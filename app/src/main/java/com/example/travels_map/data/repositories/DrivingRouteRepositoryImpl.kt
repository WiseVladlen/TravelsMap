package com.example.travels_map.data.repositories

import android.content.Context
import com.example.travels_map.R
import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.domain.entities.Route
import com.example.travels_map.domain.models.BuildingRouteData
import com.example.travels_map.domain.repositories.IDrivingRouteRepository
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.coroutines.suspendFind
import com.parse.coroutines.suspendSave
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DrivingRouteRepositoryImpl @Inject constructor(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val buildingRouteToParseRouteMapperData: IEntityMapper<BuildingRouteData, ParseObject>,
    private val parseObjectToRouteMapper: IEntityMapper<ParseObject, Route>,
) : IDrivingRouteRepository {

    private var drivingSession: DrivingSession? = null

    private lateinit var drivingRoute: DrivingRoute

    private var requestPoints: List<RequestPoint> = listOf()

    private val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()

    private val _buildingDrivingRouteFlow = MutableSharedFlow<Result<DrivingRoute?>>(0, 1, BufferOverflow.DROP_OLDEST)

    private val _drivingRouteListFlow = MutableSharedFlow<Result<List<Route>>>(0, 1, BufferOverflow.DROP_OLDEST)

    override val buildingDrivingRouteFlow = _buildingDrivingRouteFlow.asSharedFlow()

    override val drivingRouteListFlow: SharedFlow<Result<List<Route>>> = _drivingRouteListFlow.asSharedFlow()

    override suspend fun requestDrivingRouteList() {
        val result = runCatching {
            return@runCatching ParseQuery<ParseObject>(Route.CLASS_NAME).suspendFind().map { parseRoute ->
                parseObjectToRouteMapper.mapEntity(parseRoute)
            }
        }

        _drivingRouteListFlow.emit(result)
    }

    override suspend fun create(name: String): Result<Nothing?> {
        return runCatching {
            val buildingRouteData = BuildingRouteData(
                name = name,
                wayPoints = drivingRoute.geometry.points,
                requestPoints = requestPoints.map { it.point },
            )

            buildingRouteToParseRouteMapperData.mapEntity(buildingRouteData).suspendSave()

            requestDrivingRouteList()

            return@runCatching null
        }
    }

    override fun onDrivingRoutes(routeList: MutableList<DrivingRoute>) {
        drivingRoute = routeList.minBy { route ->
            route.metadata.weight.distance.value + route.metadata.weight.time.value
        }

        coroutineScope.launch {
            _buildingDrivingRouteFlow.emit(Result.success(drivingRoute))
        }
    }

    override fun onDrivingRoutesError(error: Error) {
        var errorMessage = context.getString(R.string.driving_route_error_message_unknown)

        if (error is RemoteError) {
            errorMessage = context.getString(R.string.driving_route_error_message_remote)
        } else if (error is NetworkError) {
            errorMessage = context.getString(R.string.driving_route_error_message_network)
        }

        coroutineScope.launch {
            _buildingDrivingRouteFlow.emit(Result.failure(Exception(errorMessage)))
        }
    }

    override suspend fun requestDrivingRoute(points: List<Point>) {
        if (points.size < 2) {
            coroutineScope.launch {
                _buildingDrivingRouteFlow.emit(Result.success(null))
            }
            return
        }

        requestPoints = points.map { point ->
            RequestPoint(point, RequestPointType.WAYPOINT, null)
        }

        drivingSession = drivingRouter.requestRoutes(requestPoints, DrivingOptions(), VehicleOptions(), this)
    }

    override fun cancelDrivingSession() {
        drivingSession?.cancel()
    }
}