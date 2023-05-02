package com.lolo.io.cafefinder.domain

import com.lolo.io.cafefinder.domain.model.POI
import com.lolo.io.cafefinder.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCafesNearBy(lat: Double, lon: Double): Flow<Resource<List<POI>>>
}