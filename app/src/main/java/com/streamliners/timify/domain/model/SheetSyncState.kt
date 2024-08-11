package com.streamliners.timify.domain.model

import com.streamliners.timify.android.helper.GoogleOAuthTokens

sealed interface SheetSyncState {

    data object None: SheetSyncState

    open class SignedIn(
        val userName: String,
        val userEmail: String,
        val tokens: GoogleOAuthTokens
    ): SheetSyncState {
        companion object {
            fun fromLinkedState(linked: Linked): SignedIn {
                return with(linked) { SignedIn(userName, userEmail, tokens) }
            }
        }
    }

    class Linked(
        signedIn: SignedIn,
        val sheetId: String
    ): SignedIn(
        signedIn.userName, signedIn.userEmail, signedIn.tokens
    ), SheetSyncState

}