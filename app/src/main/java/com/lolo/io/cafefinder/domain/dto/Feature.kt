package com.lolo.io.cafefinder.domain.dto

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)