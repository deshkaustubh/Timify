package com.streamliners.timify.android.helper

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.streamliners.timify.android.helper.GoogleSignInHelperState.Active
import com.streamliners.timify.android.helper.GoogleSignInHelperState.Idle

sealed interface GoogleSignInHelperState {
    data object Idle: GoogleSignInHelperState
    data class Active(
        val resultHandler: (GoogleSignInAccount) -> Unit
    ): GoogleSignInHelperState
}

@Composable
fun rememberGoogleSignInHelperState() = remember {
    mutableStateOf<GoogleSignInHelperState>(Idle)
}

fun MutableState<GoogleSignInHelperState>.start(
    resultHandler: (GoogleSignInAccount) -> Unit
) {
    value = Active(resultHandler)
}

@Composable
fun GoogleSignInHelper(
    serverId: String,
    activity: ComponentActivity,
    scopes: List<Scope> = emptyList(),
    state: MutableState<GoogleSignInHelperState>
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data

        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

        (state.value as? Active)?.resultHandler?.let { it(account) }
    }

    LaunchedEffect(key1 = state.value) {
        if (state.value is Active) {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(serverId)
                .requestServerAuthCode(serverId)
                .run {
                    if (scopes.isNotEmpty()) {
                        requestScopes(
                            scopes.first(), *scopes.toTypedArray()
                        )
                    } else this
                }
                .requestEmail()
                .build()

            launcher.launch(
                GoogleSignIn.getClient(activity, options).signInIntent
            )
        }
    }
}