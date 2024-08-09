package com.streamliners.timify.feature.pieChart

import com.streamliners.base.BaseViewModel
import com.streamliners.timify.TimifyApp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PieChartViewModel: BaseViewModel() {

    val pieChartInfoDao = TimifyApp.localDB.pieChartInfoDao()

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    val currentDate = LocalDateTime.now().format(formatter)




}