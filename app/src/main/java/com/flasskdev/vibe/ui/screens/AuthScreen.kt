package com.flasskdev.vibe.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    webSocket: VibeWebSocket,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    language: String,
    onLanguageToggle: () -> Unit,
    onAuthSuccess: (String) -> Unit
) {
    var isRegister by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val emailPattern = remember { Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$") }
    val isEmailValid = email.isEmpty() || emailPattern.matches(email)

    val listener = remember {
        object : VibeWebSocketListener {
            override fun onAuthResponse(message: VibeMessage) {
                scope.launch {
                    when (message.type) {
                        "check_availability_result" -> {
                            emailError = if (message.email_taken == true) {
                                if (language == "RU") "ЗАНЯТО" else "TAKEN"
                            } else null
                            usernameError = if (message.username_taken == true) {
                                if (language == "RU") "ЗАНЯТО" else "TAKEN"
                            } else null
                        }
                        "register_result" -> {
                            isLoading = false
                            if (message.success == true) {
                                onAuthSuccess(email)
                            } else {
                                generalError = message.message ?: (if (language == "RU") "ОШИБКА РЕГИСТРАЦИИ" else "REGISTRATION FAILED")
                            }
                        }
                        "login_result" -> {
                            isLoading = false
                            if (message.success == true) {
                                onAuthSuccess(email)
                            } else {
                                generalError = message.message ?: (if (language == "RU") "ОШИБКА ВХОДА" else "LOGIN FAILED")
                            }
                        }
                    }
                }
            }

            override fun onConnected() { scope.launch { isConnected = true } }
            override fun onDisconnected() { scope.launch { isConnected = false; isLoading = false } }
            override fun onError(error: String) { scope.launch { generalError = error; isLoading = false } }
        }
    }

    DisposableEffect(webSocket) {
        webSocket.addListener(listener)
        onDispose {
            webSocket.removeListener(listener)
        }
    }

    LaunchedEffect(email, username, isRegister, isConnected) {
        if (isConnected && isRegister) {
            delay(400)
            if (email.isNotEmpty() || username.isNotEmpty()) {
                webSocket.checkAvailability(email, username)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VibeBackgroundMesh()

        // Top Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onThemeToggle,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onLanguageToggle,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), CircleShape)
            ) {
                Text(
                    text = language,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val strings = LocalVibeStrings.current

            Text(
                text = "Vibe",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 56.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-2.5).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isRegister) strings.createAccount else strings.welcomeBack,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(54.dp))

            VibeTextField(
                value = email,
                onValueChange = { email = it; emailError = null; generalError = null },
                label = strings.emailLabel,
                error = if (!isEmailValid) strings.emailInvalidFormat else emailError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = VibePrimary.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = isRegister,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(24.dp))
                    VibeTextField(
                        value = username,
                        onValueChange = { 
                            var newUsername = it
                            // Filter out non-English, non-digit, non-underscore
                            newUsername = newUsername.filter { char -> char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9' || char == '_' }
                            
                            // Prevent starting with digit or underscore
                            while (newUsername.isNotEmpty() && (newUsername.first().isDigit() || newUsername.first() == '_')) {
                                newUsername = newUsername.drop(1)
                            }
                            
                            // Prevent multiple underscores
                            if (newUsername.count { c -> c == '_' } > 1) {
                                val firstIndex = newUsername.indexOf('_')
                                newUsername = newUsername.filterIndexed { index, c -> c != '_' || index == firstIndex }
                            }
                            
                            username = newUsername
                            generalError = null
                            
                            if (username.isNotEmpty() && username.length < 4) {
                                usernameError = strings.usernameMinLength
                            } else {
                                usernameError = null
                            }
                        },
                        label = strings.usernameLabel,
                        error = usernameError,
                        leadingIcon = {
                            Text(
                                text = "@",
                                color = VibePrimary.copy(alpha = 0.6f),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (generalError != null) {
                Text(
                    text = generalError!!,
                    color = VibeError,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            VibeButton(
                text = if (isLoading) strings.saveLoading else strings.continueBtn,
                onClick = {
                    if (isConnected && isEmailValid && email.isNotBlank()) {
                        isLoading = true
                        if (isRegister) {
                            webSocket.requestRegistration(email, username)
                        } else {
                            webSocket.requestLogin(email)
                        }
                    } else if (!isConnected) {
                        webSocket.connect()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotEmpty() && isEmailValid && (!isRegister || (username.length in 4..32)) && emailError == null && usernameError == null
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (isRegister) strings.switchSignIn else strings.switchSignUp,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isRegister = !isRegister
                        emailError = null
                        usernameError = null
                        generalError = null
                    }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "v1.0.6",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
            )
        }
    }
}