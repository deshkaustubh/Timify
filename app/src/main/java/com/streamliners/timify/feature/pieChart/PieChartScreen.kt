package com.streamliners.timify.feature.pieChart

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.timify.feature.chat.ChatViewModel
import ir.mahozad.android.PieChart

@Composable
fun PieChartScreen(
    navController: NavController,
    viewModel: PieChartViewModel
) {


    LaunchedEffect(key1 = Unit) {
        viewModel.getPieChartData()
    }

    TitleBarScaffold(
        title = "Pie Chart",
        navigateUp = {navController.navigateUp()}) { it   ->
        viewModel.slice.whenLoaded { s ->
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                factory = { context ->
                    PieChart(context).apply {
                        slices = s
                    }
                },
                update = { view ->
                    // View's been inflated or state read in this block has been updated
                    // Add logic here if necessary
                }
            )
        }

    }

}