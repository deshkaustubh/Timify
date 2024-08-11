package com.streamliners.timify.other.ext

import com.streamliners.timify.android.helper.GoogleOAuthTokens
import com.streamliners.timify.domain.model.SheetSyncState

fun SheetSyncState.tokens(): GoogleOAuthTokens {
    return when (this) {
        is SheetSyncState.Linked -> tokens
        is SheetSyncState.SignedIn -> tokens
        SheetSyncState.None -> error("Inactive SheetSyncState")
    }
}