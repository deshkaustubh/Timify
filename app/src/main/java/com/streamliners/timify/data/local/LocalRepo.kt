package com.streamliners.timify.data.local

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.streamliners.timify.android.helper.DataStoreUtil
import com.streamliners.timify.android.helper.SealedTypeAdapterFactory
import com.streamliners.timify.domain.model.SheetSyncState

class LocalRepo(
    private val dataStoreUtil: DataStoreUtil
) {

    companion object {
        private const val KEY_SHEET_SYNC_STATE = "sheetSyncState"
    }

    suspend fun setSheetSyncState(state: SheetSyncState) {
        dataStoreUtil.setData(KEY_SHEET_SYNC_STATE, state, getSealedClassSupportingGson())
    }

    suspend fun getSheetSyncState(): SheetSyncState {
        return dataStoreUtil.getData<SheetSyncState>(
            KEY_SHEET_SYNC_STATE,
            getSealedClassSupportingGson()
        ) ?: SheetSyncState.None
    }

    private fun getSealedClassSupportingGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(SealedTypeAdapterFactory.of(SheetSyncState::class))
            .create()
    }

}