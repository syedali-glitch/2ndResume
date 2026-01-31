package com.resumearchitect

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ResumeArchitectApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging in debug builds
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Initialize crash handler
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.e(throwable, "Uncaught exception in thread: ${thread.name}")
            // In production, you would send this to crash analytics
        }
    }
}
