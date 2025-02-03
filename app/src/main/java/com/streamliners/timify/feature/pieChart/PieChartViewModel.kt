package com.streamliners.timify.feature.pieChart

import androidx.compose.runtime.mutableStateListOf
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
import kotlinx.coroutines.launch

class PieChartViewModel(
    private val taskInfoDao: TaskInfoDao,
    private val customAttributeDao: CustomAttributeDao
) : BaseViewModel() {

    val listOfSpinner = mutableListOf<String>()

    val listOfTaskInfoState = mutableStateListOf<TaskInfo>()

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
            onRefreshed()
        }
    }

    private fun spinnerValue() {
        listOfSpinner.add("Task Name")
        listOfSpinner.addAll(customAttributeDao.getDistinctKeys())
    }


    fun onRefreshed() {
        viewModelScope.launch {
            val listOfTaskInfo = taskInfoDao.getList(currentDate.value()).toMutableList()

            val groupedTaskInfo = listOfTaskInfo
                .groupBy { it.name }
                .map { (name, tasks) ->
                    TaskInfo(
                        id = tasks.first().id,  // Use the ID of the first task (or another strategy if needed)
                        date = tasks.first().date,
                        startTime = tasks.first().startTime,
                        endTime = tasks.first().endTime,
                        durationInMins = tasks.sumOf { it.durationInMins },  // Sum the durations
                        name = name
                    )
                }.toMutableList()

            listOfTaskInfoState.clear()
            listOfTaskInfoState.addAll(groupedTaskInfo)

            val customAttributes = customAttributeDao.getAll()

            val trueFalseMap = customAttributes
                .filter { it.key == state.value.value}
                .associate { it.taskId to it.value.toBoolean() }

            groupedTaskInfo.sortWith(compareByDescending<TaskInfo> {
                trueFalseMap[it.id] ?: false
            })

            val totalMins = groupedTaskInfo.sumOf { it.durationInMins }

            // TODO: Color needs to be different instead random


            val slicesList = groupedTaskInfo.map { task ->

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