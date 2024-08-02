package com.streamliners.timify.di
import com.streamliners.timify.chat.ChatViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {



    single {
        HttpClient(CIO) {
            expectSuccess = true
        }
    }
}

val viewModelModule = module {

    viewModel { ChatViewModel() }

}