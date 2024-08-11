package com.streamliners.timify.domain.model

import com.streamliners.timify.android.helper.GoogleOAuthTokens

sealed interface SheetSyncState {

    data object None: SheetSyncState

    data class Active(
        val userName: String,
        val userEmail: String,
        val tokens: GoogleOAuthTokens
    ): SheetSyncState

}