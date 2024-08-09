package com.streamliners.timify.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.ai.client.generativeai.type.asTextOrNull
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.value
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.timify.feature.chat.comp.MessageCard
import com.streamliners.timify.feature.chat.comp.MessagesList

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel
) {
    var prompt by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }


    TitleBarScaffold(
        title = "Timify",
        navigateUp = { navController.navigateUp() },
        actions = {
            IconButton(onClick = {

                viewModel.savePieChartInfoToRoom()
                navController.navigate("PieChartScreen")

            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Analysis")
            }
        }
        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {

            if (uiState is UiState.Loading) {
                Column (
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))

                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                )   {

                   viewModel.data.whenLoaded {
                       MessagesList(data = it)
                   }

                }
            }
                Row(
                    modifier = Modifier
                        .padding(all = 8.dp)
                ) {

                    TextField(
                        value = prompt,
                        label = { Text("Prompt") },
                        onValueChange = { prompt = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Button(
                        onClick = {
                            viewModel.sendPrompt(prompt)
                            prompt = ""
                        },
                        enabled = prompt.isNotEmpty(),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = "Go")
                    }
                }

        }

    }


}

