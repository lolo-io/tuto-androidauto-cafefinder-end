package com.lolo.io.cafefinder.domain.dto

data class Properties(
    val city: String,
    val country: String,
    val countrycode: String,
    val county: String,
    val district: String,
    val extent: List<Double>,
    val locality: String,
    val name: String,
    val osm_id: Long,
    val osm_key: String,
    val osm_type: String,
    val osm_value: String,
    val postcode: String,
    val state: String,
    val street: String,
    val type: String
)