package com.example.neokotlinui

import android.app.Application
import com.example.neokotlinui.di.allAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Use DEBUG for development (change to ERROR in production)
            androidLogger(Level.DEBUG)
            // Provide the Android context
            androidContext(this@MainApplication)
            // Load all the modules
            modules(allAppModules)
        }
    }
}
