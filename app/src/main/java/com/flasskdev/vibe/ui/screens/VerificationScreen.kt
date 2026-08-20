package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun VerificationScreen(email: String, webSocket: VibeWebSocket, onVerified: (Int, Boolean) -> Unit) {
    var otpCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val strings = LocalVibeStrings.current

    val listener = remember(strings) {
        object : VibeWebSocketListener {
            override fun onAuthResponse(message: VibeMessage) {
                scope.launch {
                    if (message.type == "verify_code_result") {
                        isLoading = false
                        if (message.success == true) {
                            val userId = message.user_id ?: 0
                            val isNewUser = message.is_new_user == true
                            onVerified(userId, isNewUser)
                        } else {
                            errorMessage = message.message ?: strings.codeInvalid
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

        val strings = LocalVibeStrings.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = strings.verificationTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = strings.verificationSubtitle(email),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            OtpInput(
                value = otpCode,
                onValueChange = { otpCode = it; errorMessage = null }
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = VibeError,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            VibeButton(
                text = if (isLoading) strings.verifyLoading else strings.verifyBtn,
                onClick = {
                    isLoading = true
                    webSocket.verifyCode(email, otpCode)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && otpCode.length == 6
            )
        }
    }
}