package com.mapbox.examples.androidauto.car.navigation

import androidx.car.app.model.CarColor
import androidx.car.app.model.DateTimeWithZone
import androidx.car.app.navigation.model.TravelEstimate
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class CarNavigationEtaMapper(
    private val carDistanceFormatter: CarDistanceFormatter,
    private val tripProgressApi: MapboxTripProgressApi,
) {

    fun from(routeProgress: RouteProgress): TravelEstimate {
        val result = tripProgressApi.getTripProgress(routeProgress)
        val distance = carDistanceFormatter.carDistance(result.distanceRemaining)
        val zonedDateTime = DateTimeWithZone.create(result.estimatedTimeToArrival, TimeZone.getDefault())
        return TravelEstimate.Builder(distance, zonedDateTime)
            .setRemainingTimeSeconds(result.currentLegTimeRemaining.toLong() + TimeUnit.MINUTES.toSeconds(1) / 2)
            .setRemainingTimeColor(CarColor.GREEN)
            .build()
    }
}
