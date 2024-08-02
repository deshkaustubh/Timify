package com.streamliners.timify

import android.app.Application
import com.streamliners.timify.di.appModule
import com.streamliners.timify.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TimifyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TimifyApp)
            modules(appModule, viewModelModule)
        }

    }
}