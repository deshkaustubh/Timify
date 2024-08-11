package com.streamliners.timify.feature.pieChart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.streamliners.base.exception.log
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.textInput.TextInputLayoutReadOnly
import com.streamliners.compose.comp.textInput.state.nullableValue
import com.streamliners.compose.comp.textInput.state.update
import com.streamliners.pickers.date.DatePickerDialog
import com.streamliners.pickers.date.ShowDatePicker
import com.streamliners.timify.BuildConfig
import com.streamliners.utils.DateTimeUtils.Format.Companion.DATE_MONTH_YEAR_1
import ir.mahozad.android.PieChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PieChartScreen(
    navController: NavController,
    viewModel: PieChartViewModel,
    showDatePicker: ShowDatePicker
) {
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

            TextInputLayoutReadOnly(
                state = viewModel.currentDate
            ) {
                showDatePicker(
                    DatePickerDialog.Params(
                        format = DATE_MONTH_YEAR_1,
                        prefill = viewModel.currentDate.nullableValue(),
                        onPicked = { date ->
                            viewModel.currentDate.update(date)
                            viewModel.onDateChanged()
                        }
                    )
                )
            }

            viewModel.slices.whenLoaded { slices ->
                if(slices.size > 0){
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize(),
                        factory = { context ->
                            PieChart(context).apply {
                                this.slices = slices
                            }
                        },
                        update = {
                            it.slices = slices
                            log("Pie Chart Updated", "UPDATE",
                                false,
                                BuildConfig.BUILD_TYPE)
                        }
                    ) 
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No-Data Found",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold)
                    }
                }
                
            }
        }
    }
}