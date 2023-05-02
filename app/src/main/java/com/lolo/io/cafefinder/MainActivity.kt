package com.lolo.io.cafefinder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.location.LocationServices
import com.lolo.io.cafefinder.data.util.withUserLocation
import com.lolo.io.cafefinder.domain.Repository
import com.lolo.io.cafefinder.domain.model.POI
import com.lolo.io.cafefinder.ui.mobile.navigation.MobileNavigation
import com.lolo.io.cafefinder.ui.theme.CafeFinderTheme
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var allPermissionsGranted by mutableStateOf(false)

        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
                if (perms.all { it.value }) {
                    allPermissionsGranted = true
                }
            }

        setContent {
            CafeFinderTheme {
                LaunchedEffect(true) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MobileNavigation(
                        onItemClicked = { poi ->
                            openGoogleMaps(poi)
                        },
                        withUserLocation = {
                            withUserLocation(this, it)
                        },
                        allPermissionsGranted = allPermissionsGranted
                    )
                }
            }
        }
    }
}


fun MainActivity.openGoogleMaps(poi: POI) {
    val gmmIntentUri = Uri.parse("geo:${poi.lon},${poi.lat}?q=" + Uri.encode(poi.name))
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(packageManager)?.let {
        startActivity(mapIntent)
    }
}