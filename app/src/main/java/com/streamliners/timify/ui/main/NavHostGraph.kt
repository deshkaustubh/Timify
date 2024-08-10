package com.streamliners.timify.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.streamliners.base.ext.koinBaseViewModel
import com.streamliners.timify.ui.main.MainActivity
import com.streamliners.timify.feature.chat.ChatScreen
import com.streamliners.timify.feature.pieChart.PieChartScreen
import com.streamliners.pickers.date.showDatePickerDialog

@Composable
fun MainActivity.NavHostGraph(
    navController: NavHostController
){

    NavHost(
        startDestination = "ChatScreen",
        navController = navController
    ){

        composable("ChatScreen"){
            ChatScreen(
                navController = navController,
                viewModel = koinBaseViewModel()
            )
        }

        composable("PieChartScreen"){
            PieChartScreen(
                navController = navController,
                viewModel = koinBaseViewModel(),
                showDatePicker = ::showDatePickerDialog)
        }
    }

}