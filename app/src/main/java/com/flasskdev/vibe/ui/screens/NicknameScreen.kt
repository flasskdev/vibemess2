package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.data.VibeMessage
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.data.VibeWebSocketListener
import com.flasskdev.vibe.data.ChatInfo
import com.flasskdev.vibe.data.MessageInfo
import com.flasskdev.vibe.data.UserSearchResult
import com.flasskdev.vibe.ui.components.*
import com.flasskdev.vibe.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun NicknameScreen(email: String, userId: Int, webSocket: VibeWebSocket, onSuccess: () -> Unit) {
    var nickname by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val strings = LocalVibeStrings.current

    val listener = remember(strings) {
        object : VibeWebSocketListener {
            override fun onAuthResponse(message: VibeMessage) {
                scope.launch {
                    if (message.type == "set_nickname_result") {
                        isLoading = false
                        if (message.success == true) {
                            onSuccess()
                        } else {
                            errorMessage = message.message ?: strings.errorSaving
                        }
                    }
                }
            }

            override fun onConnected() {}
            override fun onDisconnected() {
                scope.launch { isLoading = false }
            }
            override fun onError(error: String) {
                scope.launch {
                    errorMessage = error
                    isLoading = false
                }
            }
        }
    }

    DisposableEffect(webSocket) {
        webSocket.addListener(listener)
        onDispose {
            webSocket.removeListener(listener)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VibeBackgroundMesh()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = strings.nicknameTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(48.dp))

            VibeTextField(
                value = nickname,
                onValueChange = { nickname = it; errorMessage = null },
                label = strings.nicknameLabel,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            VibeButton(
                text = if (isLoading) strings.saveLoading else strings.saveBtn,
                onClick = {
                    isLoading = true
                    webSocket.setNickname(email, nickname, if (userId != 0) userId else null)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && nickname.length in 1..32
            )
        }
    }
}