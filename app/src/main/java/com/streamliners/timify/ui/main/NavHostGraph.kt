package com.streamliners.timify.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.common.api.Scope
import com.streamliners.base.ext.koinBaseViewModel
import com.streamliners.pickers.date.showDatePickerDialog
import com.streamliners.timify.BuildConfig
import com.streamliners.timify.android.helper.GoogleSignInHelper
import com.streamliners.timify.android.helper.rememberGoogleSignInHelperState
import com.streamliners.timify.android.helper.start
import com.streamliners.timify.feature.chat.ChatScreen
import com.streamliners.timify.feature.pieChart.PieChartScreen
import com.streamliners.timify.feature.sheetSync.SheetSyncScreen

@Composable
fun MainActivity.NavHostGraph(
    navController: NavHostController
){
    NavHost(
        startDestination = Screen.Chat.route,
        navController = navController
    ){

        composable(Screen.Chat.route){
            ChatScreen(
                navController = navController,
                viewModel = koinBaseViewModel()
            )
        }

        composable(Screen.PieChart.route){
            PieChartScreen(
                navController = navController,
                viewModel = koinBaseViewModel(),
                showDatePicker = ::showDatePickerDialog
            )
        }

        composable(Screen.SheetSync.route){
            val googleSignInHelperState = rememberGoogleSignInHelperState()

            SheetSyncScreen(
                navController = navController,
                viewModel = koinBaseViewModel(),
                startGoogleSignIn = googleSignInHelperState::start
            )

            GoogleSignInHelper(
                serverId = BuildConfig.serverId,
                activity = this@NavHostGraph,
                state = googleSignInHelperState,
                scopes = listOf(
                    Scope("https://www.googleapis.com/auth/spreadsheets")
                )
            )
        }
    }
}