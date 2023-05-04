package com.lolo.io.cafefinder.data.car

import android.content.Intent
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.lifecycle.DefaultLifecycleObserver
import com.lolo.io.cafefinder.ui.car.CarCafeListScreen

class CarSession : Session(), DefaultLifecycleObserver {
    override fun onCreateScreen(intent: Intent): Screen {
        return CarCafeListScreen(carContext)
    }
}