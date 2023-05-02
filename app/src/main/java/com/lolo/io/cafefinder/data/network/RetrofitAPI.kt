package com.lolo.io.cafefinder.data.network

import com.lolo.io.cafefinder.domain.API
import com.lolo.io.cafefinder.domain.dto.KomootDTO
import com.lolo.io.cafefinder.domain.model.POI
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI: API {

    @GET("?q=cafe")
    override suspend fun getCafesNearBy(@Query("lat")lat: Double, @Query("lon")lon: Double): KomootDTO
}