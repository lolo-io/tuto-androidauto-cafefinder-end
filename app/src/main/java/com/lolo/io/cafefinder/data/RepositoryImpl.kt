package com.lolo.io.cafefinder.data

import com.lolo.io.cafefinder.domain.API
import com.lolo.io.cafefinder.domain.Repository
import com.lolo.io.cafefinder.domain.model.POI
import com.lolo.io.cafefinder.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(private val api: API): Repository {
    override suspend fun getCafesNearBy(lat: Double, lon: Double): Flow<Resource<List<POI>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val resp = api.getCafesNearBy(lat, lon)
                val pois = resp.features.map {
                    POI(
                        name = it.properties.name,
                        street = it.properties.street,
                        type= it.properties.osm_value.replace("_", " ").replaceFirstChar { it.uppercase() },
                        lat = it.geometry.coordinates[0],
                        lon = it.geometry.coordinates[1]
                    )
                }
                emit(Resource.Success(pois))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown Error"))
            }
        }
    }
}