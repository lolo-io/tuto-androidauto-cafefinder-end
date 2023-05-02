package com.lolo.io.cafefinder.domain

import com.lolo.io.cafefinder.domain.dto.KomootDTO

interface API {
    suspend fun getCafesNearBy(lat: Double, lon: Double): KomootDTO
}