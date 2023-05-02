package com.lolo.io.cafefinder.ui.mobile.navigation

import android.location.Location
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lolo.io.cafefinder.domain.model.POI
import com.lolo.io.cafefinder.ui.mobile.CafeListComposable

@Composable
fun MobileNavigation(
    onItemClicked: (poi: POI) -> Unit,
    allPermissionsGranted: Boolean,
    withUserLocation: (block: (Location?) -> Unit) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CafeListScreen.route) {
        composable(Screen.CafeListScreen.route) {
            CafeListComposable(onItemClicked, withUserLocation, allPermissionsGranted)
        }
    }
}

sealed class Screen(val route: String) {
    object CafeListScreen : Screen("measure_screen")
}