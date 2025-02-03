package com.streamliners.timify.feature.sheetSync

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.streamliners.base.BaseViewModel
import com.streamliners.base.exception.failure
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.hideLoader
import com.streamliners.base.ext.showLoader
import com.streamliners.base.ext.showToast
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import com.streamliners.timify.android.helper.GoogleOAuthTokensFetcher
import com.streamliners.timify.android.helper.SheetsHelper
import com.streamliners.timify.data.local.LocalRepo
import com.streamliners.timify.data.local.dao.CustomAttributeDao
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.domain.model.SheetSyncState
import com.streamliners.timify.domain.model.SheetSyncState.Linked
import com.streamliners.timify.other.ext.parseAsCustomAttributeList
import com.streamliners.timify.other.ext.parseAsTaskInfoList
import com.streamliners.timify.other.ext.toRows
import com.streamliners.timify.other.ext.tokens

class SheetSyncViewModel(
    private val googleOAuthTokensFetcher: GoogleOAuthTokensFetcher,
    private val localRepo: LocalRepo,
    private val taskInfoDao: TaskInfoDao,
    private val customAttributeDao: CustomAttributeDao
): BaseViewModel() {

    val state = taskStateOf<SheetSyncState>()

    private lateinit var sheetsHelper: SheetsHelper

    val localRowsCount = taskStateOf<Int>()

    fun start() {
        execute(false) {
            state.update(localRepo.getSheetSyncState())
            updateLocalRowsCount()
            initSheets()
        }
    }

    fun onGoogleSignIn(account: GoogleSignInAccount) {
        execute {
            val tokens = googleOAuthTokensFetcher.getOAuthTokens(account.serverAuthCode!!)

            val sheetSyncState = SheetSyncState.SignedIn(
                account.displayName!!, account.email!!, tokens
            )

            state.update(sheetSyncState)
            localRepo.setSheetSyncState(sheetSyncState)
            initSheets()
        }
    }

    fun createNewSheet() {
        execute {
            val id = sheetsHelper.createNewSheet()
            linkExistingSheet(id)
            showToast("Sheet created!")
        }
    }

    fun linkExistingSheet(id: String) {
        execute {
            val signedInState = (state.value() as? SheetSyncState.SignedIn) ?: error("Not SignedIn state")
            val newState = Linked(signedInState, id)
            state.update(newState)
            localRepo.setSheetSyncState(newState)
        }
    }

    fun push() {
        execute(false) {
            val tasks = taskInfoDao.getAll()

            if (tasks.isEmpty()) failure("Nothing to push! Add some data first using chat.")

            showLoader()

            val data = tasks.toRows()
            sheetsHelper.overwrite(sheetId(), rows = data)

            showToast("Pushed successfully!")
            hideLoader()
        }
    }

    fun pull() {
        execute {
            val rows = sheetsHelper.readAllRows(sheetId())

            // Clear old data
            taskInfoDao.clear()
            customAttributeDao.clear()

            // Add new Tasks
            taskInfoDao.addAll(rows.parseAsTaskInfoList())

            // Get first task id to correctly set CustomAttribute.taskId
            val firstTaskId = taskInfoDao.getFirstId()
            customAttributeDao.addAll(parseAsCustomAttributeList(rows, firstTaskId))

            updateLocalRowsCount()
            showToast("Pulled successfully!")
        }
    }

    fun unlinkSheet() {
        execute {
            val linkedState = (state.value() as? Linked) ?: error("Not Linked state")
            val newState = SheetSyncState.SignedIn.fromLinkedState(linkedState)
            state.update(newState)
            localRepo.setSheetSyncState(newState)
        }
    }

    private fun updateLocalRowsCount() {
        execute(false) {
            localRowsCount.update(taskInfoDao.getTotalRowCount())
        }
    }

    private fun sheetId() = (state.value() as? Linked)?.sheetId ?: error("Not Linked State")

    private fun initSheets() {
        if (!this@SheetSyncViewModel::sheetsHelper.isInitialized) {
            val sheetSyncState = state.value()
            if (sheetSyncState is SheetSyncState.SignedIn) {
                sheetsHelper = SheetsHelper(sheetSyncState.tokens())
            }
        }
    }

}