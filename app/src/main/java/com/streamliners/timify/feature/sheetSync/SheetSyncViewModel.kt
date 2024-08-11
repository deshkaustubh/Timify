package com.streamliners.timify.feature.sheetSync

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.showMessageDialog
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import com.streamliners.timify.android.helper.GoogleOAuthTokensFetcher
import com.streamliners.timify.android.helper.SheetsHelper
import com.streamliners.timify.data.local.LocalRepo
import com.streamliners.timify.domain.model.SheetSyncState
import com.streamliners.timify.other.ext.tokens

class SheetSyncViewModel(
    private val googleOAuthTokensFetcher: GoogleOAuthTokensFetcher,
    private val localRepo: LocalRepo
): BaseViewModel() {

    val state = taskStateOf<SheetSyncState>()

    lateinit var sheetsHelper: SheetsHelper

    fun start() {
        execute(false) {
            state.update(localRepo.getSheetSyncState())
            initSheets()
        }
    }

    fun onGoogleSignIn(account: GoogleSignInAccount) {
        execute {
            val tokens = googleOAuthTokensFetcher.getOAuthTokens(account.serverAuthCode!!)

            val sheetSyncState = SheetSyncState.Active(
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
            showMessageDialog("Done!", "Sheet created with id = $id")
        }
    }

    private fun initSheets() {
        if (!this@SheetSyncViewModel::sheetsHelper.isInitialized) {
            val sheetSyncState = state.value()
            if (sheetSyncState is SheetSyncState.Active) {
                sheetsHelper = SheetsHelper(sheetSyncState.tokens())
            }
        }
    }

}