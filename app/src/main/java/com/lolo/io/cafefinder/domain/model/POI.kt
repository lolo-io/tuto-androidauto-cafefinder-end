package com.lolo.io.cafefinder.domain.model

data class POI(
    val name: String, val street: String? = "Temp Street",
    val type: String? = "Café",
    val lon: Double,
    val lat: Double
)
