package com.streamliners.timify.feature.pieChart

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import co.yml.charts.ui.piechart.models.PieChartData
import com.streamliners.base.BaseViewModel
import com.streamliners.base.exception.log
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.value
import com.streamliners.timify.BuildConfig
import com.streamliners.timify.data.local.dao.CustomAttributeDao
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.domain.model.CustomAttribute
import com.streamliners.timify.domain.model.TaskInfo
import com.streamliners.timify.ui.theme.listOfColors
import com.streamliners.timify.ui.theme.listOfGreenColorShades
import com.streamliners.timify.ui.theme.listOfRedColorShades
import com.streamliners.utils.DateTimeUtils.Format
import com.streamliners.utils.DateTimeUtils.formatTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PieChartViewModel(
    private val taskInfoDao: TaskInfoDao,
    private val customAttributeDao: CustomAttributeDao
) : BaseViewModel() {

    val listOfSpinner = mutableListOf<String>()

    val currentDate = mutableStateOf(
        TextInputState("Date", value = formatTime(Format("yyyy/MM/dd")))
    )

    val state = mutableStateOf(
        TextInputState("Group By")
    )


    val slices = taskStateOf<List<PieChartData.Slice>>()

    fun start() {
        execute {
            spinnerValue()
            onDateChanged()
        }
    }

    private fun spinnerValue() {
        listOfSpinner.add("Task Name")
        listOfSpinner.addAll(customAttributeDao.getDistinctKeys())
    }


    fun onDateChanged() {
        viewModelScope.launch {
            val listOfTaskInfo = taskInfoDao.getList(currentDate.value()).toMutableList()

            val totalMins = listOfTaskInfo.sumOf { it.durationInMins }

            // TODO: Color needs to be different instead random

            val slicesList = listOfTaskInfo.map { task ->
                PieChartData.Slice(
                    value = task.durationInMins / totalMins.toFloat(),
                    color = getTaskColor(task),
                    label = task.name
                )
            }

            log("slices : '$slicesList'", "pieChartDebug", buildType = BuildConfig.BUILD_TYPE)

            slices.update(slicesList)
        }
    }

    private suspend fun getTaskColor(task: TaskInfo): Color {

            val customAttributes = customAttributeDao.getCustomAttributesByTaskId(taskId = task.id)
            return if (state.value.label == "Group By" && state.value().isEmpty() || state.value() == "Task Name") listOfColors.random()
            else {
                if (isStateKeyExists(customAttributes)) {
                    listOfGreenColorShades.random()
                } else {
                    listOfRedColorShades.random()
                }
            }

    }

    private fun isStateKeyExists(customAttributes: List<CustomAttribute>): Boolean {
        return customAttributes.any {
            it.key == state.value() && it.value == "TRUE"
        }
    }

}