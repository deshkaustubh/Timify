package com.streamliners.timify.feature.sheetSync

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.ifValidInput
import com.streamliners.timify.domain.model.SheetSyncState.Linked
import com.streamliners.timify.domain.model.SheetSyncState.None
import com.streamliners.timify.domain.model.SheetSyncState.SignedIn

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
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            viewModel.state.whenLoaded { state ->
                when (state) {
                    is Linked -> {
                        val ctx = LocalContext.current

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                val spreadsheetId = state.sheetId
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://docs.google.com/spreadsheets/d/$spreadsheetId")
                                }
                                ctx.startActivity(intent)
                            }
                        ) {
                            Text(text = "Open Sheet")
                            Icon(
                                modifier = Modifier.padding(start = 8.dp),
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = "Open"
                            )
                        }

                        viewModel.localRowsCount.whenLoaded {
                            Text(
                                text = "Local rows count = $it",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = viewModel::push
                            ) {
                                Text(text = "Push")
                                Icon(
                                    modifier = Modifier.padding(start = 8.dp),
                                    imageVector = Icons.Default.Upload,
                                    contentDescription = "Push"
                                )
                            }

                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = viewModel::pull
                            ) {
                                Text(text = "Pull")
                                Icon(
                                    modifier = Modifier.padding(start = 8.dp),
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Pull"
                                )
                            }
                        }

                        Text(
                            text = "Note that push will replace all data in sheet. And pull will replace all data in local.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = viewModel::unlinkSheet
                        ) {
                            Text(text = "Unlink")
                            Icon(
                                modifier = Modifier.padding(start = 8.dp),
                                imageVector = Icons.Default.LinkOff,
                                contentDescription = "Unlink"
                            )
                        }
                    }
                    is SignedIn -> {
                        Text(
                            text = "Signed in as ${state.userName} (${state.userEmail})",
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = viewModel::createNewSheet
                        ) {
                            Text(text = "Create new sheet")
                        }

                        Text(
                            modifier = Modifier.padding(vertical = 12.dp),
                            text = "OR",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Link existing",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        val idInput = remember {
                            mutableStateOf(
                                TextInputState("Spreadsheet id")
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            TextInputLayout(
                                modifier = Modifier.weight(1f),
                                state = idInput
                            )

                            FilledTonalIconButton(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .align(Alignment.CenterVertically),
                                onClick = {
                                    idInput.ifValidInput(viewModel::linkExistingSheet)
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Done, contentDescription = "Ok")
                            }
                        }

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