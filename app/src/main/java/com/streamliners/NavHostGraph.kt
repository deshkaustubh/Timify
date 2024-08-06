package com.streamliners

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.streamliners.base.ext.koinBaseViewModel
import com.streamliners.timify.MainActivity
import com.streamliners.timify.feature.chat.ChatScreen
import com.streamliners.timify.feature.chat.VoiceToTextScreen

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
    }

}