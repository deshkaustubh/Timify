package com.streamliners.timify.feature.pieChart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    showDatePicker: ShowDatePicker,
    ) {

    val dob = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getPieChartData()
    }

    TitleBarScaffold(
        title = "Pie Chart",
        navigateUp = {navController.navigateUp()}) { paddingValues  ->

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
                                prefill = dob.value,
                                onPicked = { date ->
                                    dob.value = date
                                }
                            )
                        )
                    },
                value = dob.value ?: "",
                onValueChange = {},
                readOnly = false,
                enabled = false,
                label = { Text(text = "Date of Birth") }
            )

            viewModel.slice.whenLoaded { slice ->
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        PieChart(context).apply {
                            slices = slice
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

}