package com.lolo.io.cafefinder.ui.car

import android.content.Intent
import android.net.Uri
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarColor
import androidx.car.app.model.CarIcon
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import com.lolo.io.cafefinder.domain.model.POI
import org.koin.core.component.KoinComponent

class CarCafeDetailScreen(carContext: CarContext, val poi: POI) : Screen(carContext), KoinComponent {
    override fun onGetTemplate(): Template {
        return PaneTemplate.Builder(
            Pane.Builder()
                .addRow(Row.Builder()
                    .setTitle(poi.name)
                    .addText(poi.street ?: "")
                    .addText(poi.type ?: "")
                    .setImage(CarIcon.APP_ICON)
                    .build())
                .addAction(
                    Action.Builder()
                        .setTitle("Navigate")
                        .setOnClickListener {

                            val intent = Intent().apply {
                                action = CarContext.ACTION_NAVIGATE
                                data = Uri.parse("geo:${poi.lon},${poi.lat}")
                            }
                            carContext.startCarApp(intent)
                            screenManager.pop()
                        }
                        .setBackgroundColor(CarColor.GREEN)
                        .build()
                )
                .addAction(
                    Action.Builder()
                        .setTitle("Back")
                        .setOnClickListener {
                            screenManager.pop()
                        }
                        .build()
                )
                .build()
        )
            .setTitle(poi.name)
            .setHeaderAction(Action.BACK)
            .build()
    }
}