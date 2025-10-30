package com.aimusicplayer.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AIMusicPlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
