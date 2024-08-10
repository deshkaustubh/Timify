package com.streamliners.timify

import android.app.Application
import com.streamliners.timify.di.koinSetup

class TimifyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        koinSetup()
    }
}