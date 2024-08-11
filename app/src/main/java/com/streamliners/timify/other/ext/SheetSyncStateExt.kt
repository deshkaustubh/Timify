package com.streamliners.timify.other.ext

import com.streamliners.timify.android.helper.GoogleOAuthTokens
import com.streamliners.timify.domain.model.SheetSyncState

fun SheetSyncState.tokens(): GoogleOAuthTokens {
    return when (this) {
        is SheetSyncState.Active -> tokens
        SheetSyncState.None -> error("Inactive SheetSyncState")
    }
}