package com.streamliners.timify.feature.pieChart

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.timify.feature.chat.ChatViewModel
import ir.mahozad.android.PieChart

@Composable
fun PieChartScreen(
    navController: NavController,
    viewModel: ChatViewModel
) {
    //val context = LocalContext.current

    TitleBarScaffold(
        title = "Pie Chart",
        navigateUp = {navController.navigateUp()}) { it   ->
        AndroidView(
            modifier = Modifier.fillMaxSize().padding(it)
                .padding(24.dp),
            factory = { context ->
                PieChart(context).apply {
                    slices = listOf(
                        PieChart.Slice(fraction = 0.2f, color = Color.BLUE, label = "ABC"),
                        PieChart.Slice(fraction = 0.4f, color = Color.MAGENTA, label = "DEF"),
                        PieChart.Slice(fraction = 0.3f, color = Color.GREEN, label = "GHI"),
                        PieChart.Slice(fraction = 0.1f, color = Color.CYAN, label = "JKL")
                    )
                }
            },
            update = { view ->
                // View's been inflated or state read in this block has been updated
                // Add logic here if necessary
            }
        )
    }

}