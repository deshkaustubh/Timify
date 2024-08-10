package com.streamliners.timify.ui.main

sealed class Screen(
    val route: String
) {
    data object Chat: Screen("Chat")
    data object PieChart: Screen("PieChart")
}