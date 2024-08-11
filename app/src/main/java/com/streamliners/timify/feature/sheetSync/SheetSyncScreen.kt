package com.streamliners.timify.feature.sheetSync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.timify.domain.model.SheetSyncState.Active
import com.streamliners.timify.domain.model.SheetSyncState.None

typealias GoogleSignInResultHandler = (GoogleSignInAccount) -> Unit

@Composable
fun SheetSyncScreen(
    navController: NavController,
    viewModel: SheetSyncViewModel,
    startGoogleSignIn: (GoogleSignInResultHandler) -> Unit
) {
    LaunchedEffect(key1 = Unit) { viewModel.start() }

    TitleBarScaffold(
        title = "Sheet Sync",
        navigateUp = { navController.navigateUp() }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            viewModel.state.whenLoaded { state ->
                when (state) {
                    is Active -> {
                        Text(
                            text = "Signed in as ${state.userName} (${state.userEmail})\n\nTokens = ${state.tokens}",
                            textAlign = TextAlign.Center
                        )
                    }
                    None -> {
                        Button(
                            onClick = {
                                startGoogleSignIn(
                                    viewModel::onGoogleSignIn
                                )
                            }
                        ) {
                            Text(text = "Sign in")
                        }
                    }
                }
            }
        }
    }
}