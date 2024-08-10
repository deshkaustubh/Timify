package com.streamliners.timify.feature.pieChart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.pickers.date.DatePickerDialog
import com.streamliners.pickers.date.ShowDatePicker
import com.streamliners.utils.DateTimeUtils.Format.Companion.DATE_MONTH_YEAR_2
import ir.mahozad.android.PieChart

@Composable
fun PieChartScreen(
    navController: NavController,
    viewModel: PieChartViewModel,
    showDatePicker: ShowDatePicker
) {
    val dateState = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }

    TitleBarScaffold(
        title = "Pie Chart",
        navigateUp = {
            navController.navigateUp()
        }
    ) { paddingValues  ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker(
                            DatePickerDialog.Params(
                                format = DATE_MONTH_YEAR_2,
                                prefill = dateState.value,
                                onPicked = { date ->
                                    dateState.value = date
                                }
                            )
                        )
                    },
                value = dateState.value ?: "",
                onValueChange = {},
                readOnly = false,
                enabled = false,
                label = { Text(text = "Date") }
            )

            viewModel.slices.whenLoaded { slices ->
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        PieChart(context).apply {
                            this.slices = slices
                        }
                    },
                    update = { }
                )
            }
        }
    }
}