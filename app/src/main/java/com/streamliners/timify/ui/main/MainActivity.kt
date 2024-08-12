package com.streamliners.timify.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.streamliners.base.BaseActivity
import com.streamliners.base.uiEvent.UiEventDialogs
import com.streamliners.timify.BuildConfig
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.ui.theme.TimifyTheme
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity()  {

    override var buildType: String = BuildConfig.BUILD_TYPE

    val taskInfoDao by inject<TaskInfoDao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TimifyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHostGraph(navController)
                    UiEventDialogs()
                }
            }
        }
    }
}

