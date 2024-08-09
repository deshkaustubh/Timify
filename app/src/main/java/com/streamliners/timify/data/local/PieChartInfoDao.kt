package com.streamliners.timify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streamliners.timify.domain.PieChartInfo


@Dao
interface PieChartInfoDao {

    @Query("SELECT * FROM PieChartInfo WHERE DATE = :date")
    suspend fun getPieChartInfo(date: String): List<PieChartInfo>

    @Query("DELETE FROM PIECHARTINFO")
    suspend fun deletePieChartInfo()

    @Insert
    suspend fun addPieChartInfo(pieChartInfo: PieChartInfo)

}