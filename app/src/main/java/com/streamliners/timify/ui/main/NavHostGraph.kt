package com.streamliners.timify.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.streamliners.base.ext.koinBaseViewModel
import com.streamliners.pickers.date.showDatePickerDialog
import com.streamliners.timify.feature.chat.ChatScreen
import com.streamliners.timify.feature.pieChart.PieChartScreen

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
    }
}