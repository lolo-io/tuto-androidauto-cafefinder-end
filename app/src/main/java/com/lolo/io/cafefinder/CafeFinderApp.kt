package com.lolo.io.cafefinder

import android.app.Application
import com.lolo.io.cafefinder.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CafeFinderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CafeFinderApp)
            modules(appModule)
        }
    }
}