package com.lolo.io.cafefinder.data.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun withUserLocation(context: Context, block: (location: Location?) -> Unit) {
    LocationServices.getFusedLocationProviderClient(context).lastLocation
        .addOnSuccessListener { location: Location? ->
            block(location)
        }
}