package com.lolo.io.cafefinder.ui.shared

import com.lolo.io.cafefinder.domain.model.POI

data class CafeListUiState(
    val pois: List<POI> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)