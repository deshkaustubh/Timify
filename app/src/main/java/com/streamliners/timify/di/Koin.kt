package com.streamliners.timify.di

import android.app.Application
import com.streamliners.timify.data.local.LocalDB
import com.streamliners.timify.feature.chat.ChatViewModel
import com.streamliners.timify.feature.pieChart.PieChartViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun Application.koinSetup() {
    startKoin {
        androidLogger()
        androidContext(this@koinSetup)
        modules(appModule, viewModelModule)
    }
}

private val appModule = module {
    single {
        HttpClient(CIO) {
            expectSuccess = true
        }
    }
    single {
        LocalDB.create(androidApplication())
    }
    single {
        val db: LocalDB = get()
        db.chatHistoryDao()
    }
    single {
        val db: LocalDB = get()
        db.taskInfoDao()
    }
}

private val viewModelModule = module {
    viewModel { ChatViewModel(get(), get()) }
    viewModel { PieChartViewModel(get()) }
}