package com.streamliners.timify.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.sqlite.db.SimpleSQLiteQuery
import com.streamliners.base.BaseActivity
import com.streamliners.base.ext.showMessageDialog
import com.streamliners.base.uiEvent.UiEventDialogs
import com.streamliners.timify.BuildConfig
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.ui.theme.TimifyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import javax.inject.Inject

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

        execute {
            val x = taskInfoDao.rawQueryAsIntList(
                SimpleSQLiteQuery(
                    """
                        SELECT
                          SUM(CASE WHEN date >= '2024/08/05' AND date <= '2024/08/11' THEN durationInMins ELSE 0 END) AS week4,
                          SUM(CASE WHEN date >= '2024/07/29' AND date <= '2024/08/04' THEN durationInMins ELSE 0 END) AS week3,
                          SUM(CASE WHEN date >= '2024/07/22' AND date <= '2024/07/28' THEN durationInMins ELSE 0 END) AS week2,
                          SUM(CASE WHEN date >= '2024/07/15' AND date <= '2024/07/21' THEN durationInMins ELSE 0 END) AS week1
                        FROM TasksInfo
                        WHERE name = 'Alpha Project';
                    """.trimIndent()
                )
            )

            showMessageDialog("Result", "$x")
        }
    }
}

