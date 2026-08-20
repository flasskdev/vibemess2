package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.data.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PasscodeAuthScreen(
    userPreferences: UserPreferences,
    onSuccess: () -> Unit,
    onLogout: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    val savedPin = userPreferences.passcode

    if (savedPin == null) {
        // Fallback in case navigated here but no pin is set
        LaunchedEffect(Unit) {
            onSuccess()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PasscodeContent(
            title = "Введите код-пароль",
            pin = pin,
            isError = isError,
            onPinChange = { newPin ->
                if (isError) isError = false
                pin = newPin
                if (pin.length == 4) {
                    if (pin == savedPin) {
                        onSuccess()
                    } else {
                        isError = true
                        scope.launch {
                            delay(500)
                            pin = ""
                            isError = false
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))
        
        TextButton(
            onClick = onLogout,
            modifier = Modifier.padding(bottom = 32.dp).align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Выйти из аккаунта",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun PasscodeSetupScreen(
    userPreferences: UserPreferences,
    onBack: () -> Unit
) {
    var step by remember { mutableStateOf(Step.INFO) }
    var pin by remember { mutableStateOf("") }
    var firstPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var showRemoveDialog by remember { mutableStateOf(false) }

    val title = when (step) {
        Step.INFO -> ""
        Step.ENTER_CURRENT -> "Введите текущий код-пароль"
        Step.ENTER_NEW -> "Придумайте код-пароль"
        Step.CONFIRM_NEW -> "Повторите код-пароль"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (userPreferences.passcode != null && step == Step.ENTER_NEW) {
                TextButton(onClick = { showRemoveDialog = true }) {
                    Text("Отключить", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (step == Step.INFO) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Вход по коду",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Код-пароль дополнительно защитит ваши данные. При открытии приложения потребуется ввести установленный код-пароль.",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    if (userPreferences.passcode != null) {
                        Button(
                            onClick = { step = Step.ENTER_CURRENT },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        ) {
                            Text("Изменить код-пароль", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showRemoveDialog = true },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Отключить код-пароль", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    } else {
                        Button(
                            onClick = { step = Step.ENTER_NEW },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        ) {
                            Text("Включить код-пароль", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            } else {
                PasscodeContent(
                    title = title,
                    pin = pin,
                    isError = isError,
                    onPinChange = { newPin ->
                        if (isError) isError = false
                        pin = newPin
                        
                        if (pin.length == 4) {
                            when (step) {
                                Step.ENTER_CURRENT -> {
                                    if (pin == userPreferences.passcode) {
                                        step = Step.ENTER_NEW
                                        pin = ""
                                    } else {
                                        isError = true
                                        scope.launch { delay(500); pin = ""; isError = false }
                                    }
                                }
                                Step.ENTER_NEW -> {
                                    firstPin = pin
                                    step = Step.CONFIRM_NEW
                                    pin = ""
                                }
                                Step.CONFIRM_NEW -> {
                                    if (pin == firstPin) {
                                        userPreferences.passcode = pin
                                        onBack()
                                    } else {
                                        isError = true
                                        scope.launch { 
                                            delay(500)
                                            pin = ""
                                            firstPin = ""
                                            step = Step.ENTER_NEW
                                            isError = false 
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                )
            }
        }
    }

    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Отключить код-пароль?", color = MaterialTheme.colorScheme.onBackground) },
            text = { Text("Код-пароль будет удален.", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)) },
            confirmButton = {
                TextButton(onClick = {
                    userPreferences.passcode = null
                    showRemoveDialog = false
                    onBack()
                }) {
                    Text("Отключить", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Отмена", color = MaterialTheme.colorScheme.onBackground)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

private enum class Step {
    INFO, ENTER_CURRENT, ENTER_NEW, CONFIRM_NEW
}

@Composable
private fun PasscodeContent(
    title: String,
    pin: String,
    isError: Boolean,
    onPinChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (i in 0 until 4) {
                val isFilled = i < pin.length
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isError -> MaterialTheme.colorScheme.error
                                isFilled -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                            }
                        )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(64.dp))
        
        val buttonSpacing = 24.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                NumButton("1") { if (pin.length < 4) onPinChange(pin + "1") }
                NumButton("2") { if (pin.length < 4) onPinChange(pin + "2") }
                NumButton("3") { if (pin.length < 4) onPinChange(pin + "3") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                NumButton("4") { if (pin.length < 4) onPinChange(pin + "4") }
                NumButton("5") { if (pin.length < 4) onPinChange(pin + "5") }
                NumButton("6") { if (pin.length < 4) onPinChange(pin + "6") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                NumButton("7") { if (pin.length < 4) onPinChange(pin + "7") }
                NumButton("8") { if (pin.length < 4) onPinChange(pin + "8") }
                NumButton("9") { if (pin.length < 4) onPinChange(pin + "9") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                Box(modifier = Modifier.size(72.dp)) // Empty space for alignment
                NumButton("0") { if (pin.length < 4) onPinChange(pin + "0") }
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .clickable { if (pin.isNotEmpty()) onPinChange(pin.dropLast(1)) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Backspace,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun NumButton(
    number: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
