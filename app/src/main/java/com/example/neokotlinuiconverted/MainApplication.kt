package com.example.neokotlinuiconverted

import android.app.Application
import com.example.neokotlinuiconverted.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Use Koin Android Logger (Level.ERROR to see errors only, Level.INFO for more verbose)
            androidLogger(Level.INFO)
            // Reference Android context
            androidContext(this@MainApplication)
            // Load modules
            modules(appModule)
        }
    }
}
