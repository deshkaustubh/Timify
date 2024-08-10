package com.streamliners.timify.feature.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.timify.feature.chat.comp.MessagesList
import com.streamliners.timify.ui.main.Screen

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel
) {
    var prompt by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }

    TitleBarScaffold(
        title = "Timify",
        navigateUp = { navController.navigateUp() },
        actions = {
            IconButton(
                onClick = {
                    viewModel.saveTaskInfoToLocal {
                        navController.navigate(Screen.PieChart.route)
                    }
                }
            ) {
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

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                viewModel.data.whenLoaded { data ->
                    MessagesList(data)
                }
            }

            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
            ) {

                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                        .align(Alignment.CenterVertically),
                    value = prompt,
                    label = { Text("Prompt") },
                    onValueChange = { prompt = it }
                )

                Button(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    enabled = prompt.isNotEmpty(),
                    onClick = {
                        viewModel.sendPrompt(prompt) {
                            prompt = ""
                        }
                    }
                ) {
                    Text(text = "Go")
                }
            }
        }
    }
}