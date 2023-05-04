package com.lolo.io.cafefinder.ui.car

import android.Manifest
import android.annotation.SuppressLint
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarColor
import androidx.car.app.model.CarLocation
import androidx.car.app.model.ItemList
import androidx.car.app.model.Metadata
import androidx.car.app.model.Place
import androidx.car.app.model.PlaceListMapTemplate
import androidx.car.app.model.PlaceMarker
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import com.lolo.io.cafefinder.data.util.withUserLocation
import com.lolo.io.cafefinder.domain.Repository
import com.lolo.io.cafefinder.domain.model.POI
import com.lolo.io.cafefinder.ui.shared.CafeListUiState
import com.lolo.io.cafefinder.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CarCafeListScreen(carContext: CarContext) : Screen(carContext), KoinComponent {

    private val repository: Repository
            by inject()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var uiState = CafeListUiState(
        loading = true
    )

    var hasLocationPermission = false

    init {
        carContext.requestPermissions(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) { granted, _ ->
            if (granted.contains(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                hasLocationPermission = true
                updateCafeList()
            }
        }
    }

    private fun updateCafeList() {
        withUserLocation(carContext) {
            if (it != null) {
                coroutineScope.launch {
                    repository.getCafesNearBy(it.latitude, it.longitude).onEach { res ->
                        uiState = when (res) {
                            is Resource.Error -> {
                                CafeListUiState(
                                    error = "Not handled in this tutorial",
                                )
                            }

                            is Resource.Loading -> {
                                CafeListUiState(
                                    pois = uiState.pois,
                                    loading = true
                                )
                            }

                            is Resource.Success -> {
                                CafeListUiState(
                                    pois = res.data,
                                )
                            }
                        }
                        invalidate()
                    }.launchIn(coroutineScope)
                }
            }
        }
    }

    private fun cafeRow(poi: POI): Row {
        return Row.Builder()
            .setTitle(poi.name)
            .setMetadata(
                Metadata.Builder()
                    .setPlace(
                        Place.Builder(
                            CarLocation.create(
                                poi.lon, poi.lat
                            )
                        ).setMarker(
                            PlaceMarker.Builder().setColor(
                                CarColor.createCustom(
                                    0xFF543a20.toInt(), 0xFF543a20.toInt()
                                )
                            ).build()
                        )
                            .build()
                    ).build()
            )
            .setBrowsable(true)
            .setOnClickListener {
                screenManager.push(CarCafeDetailScreen(carContext, poi))
            }
            .addText(poi.street ?: "")
            .addText(poi.type ?: "")
            .build()
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onGetTemplate(): Template {

        val builder = PlaceListMapTemplate
            .Builder()
            .setTitle("CafeFinder")
            .setHeaderAction(Action.APP_ICON)

        builder.setLoading(uiState.loading)

        if (!uiState.loading) {
            builder.setLoading(false)
            val items = ItemList.Builder()

            uiState.pois.forEach {
                items.addItem(cafeRow(it))
            }

            builder.setItemList(items.build())
        }

        if (hasLocationPermission) {
            builder.setCurrentLocationEnabled(true)
        }

        builder
            .setOnContentRefreshListener {
                updateCafeList()
            }


        return builder.build()
    }
}