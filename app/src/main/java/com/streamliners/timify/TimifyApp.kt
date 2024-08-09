package com.streamliners.timify

import android.app.Application
import androidx.room.Room
import com.streamliners.timify.data.local.LocalDB
import com.streamliners.timify.di.appModule
import com.streamliners.timify.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TimifyApp: Application() {

    companion object {
        lateinit var localDB: LocalDB
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TimifyApp)
            modules(appModule, viewModelModule)
        }

        localDB = Room.databaseBuilder(
            applicationContext,
            LocalDB::class.java,
            LocalDB.NAME
        ).build()

    }
}