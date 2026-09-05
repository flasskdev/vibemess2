package com.flasskdev.vibe.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.ui.components.VibeToast
import com.flasskdev.vibe.ui.theme.LocalVibeStrings
import com.flasskdev.vibe.ui.theme.VibeStrings
import com.flasskdev.vibe.ui.theme.VibeTopGlow
import com.flasskdev.vibe.ui.theme.luminanceIsDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class TwoFactorStep {
    STATUS,
    ENTER_CURRENT,
    ENTER_NEW,
    CONFIRM_NEW,
    ENTER_HINT
}

private enum class CurrentPasswordTarget {
    CHANGE_PASSWORD,
    CHANGE_HINT,
    DISABLE
}

@Composable
fun TwoFactorSettingsContent(
    userPreferences: UserPreferences,
    onBack: () -> Unit
) {
    val strings = LocalVibeStrings.current
    val isDark = MaterialTheme.colorScheme.background.luminanceIsDark()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var step by remember { mutableStateOf(TwoFactorStep.STATUS) }
    var currentTarget by remember { mutableStateOf(CurrentPasswordTarget.CHANGE_PASSWORD) }

    var currentPasswordInput by remember { mutableStateOf("") }
    var newPasswordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }
    var hintInput by remember { mutableStateOf(userPreferences.twoFactorHint.orEmpty()) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var showDisableDialog by remember { mutableStateOf(false) }

    val hasTwoFactor = userPreferences.twoFactorPassword != null

    fun resetInputs() {
        currentPasswordInput = ""
        newPasswordInput = ""
        confirmPasswordInput = ""
        errorMessage = null
    }

    fun handleBack() {
        if (step != TwoFactorStep.STATUS) {
            step = TwoFactorStep.STATUS
            resetInputs()
            keyboardController?.hide()
        } else {
            onBack()
        }
    }

    BackHandler(enabled = true) {
        handleBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        VibeTopGlow(height = 380.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 8.dp)
        ) {
            // ─── TOP BAR ───
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 16.dp, bottom = 8.dp)
            ) {
                IconButton(onClick = { handleBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = strings.backBtn,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = strings.twoFactorTitle,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // ─── SCROLLABLE CONTENT ───
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(12.dp))

                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        fadeIn(tween(200)) togetherWith fadeOut(tween(150))
                    },
                    label = "two_factor_step"
                ) { currentStep ->
                    when (currentStep) {
                        TwoFactorStep.STATUS -> {
                            TwoFactorStatusContent(
                                isEnabled = hasTwoFactor,
                                currentHint = userPreferences.twoFactorHint,
                                strings = strings,
                                isDark = isDark,
                                onSetPassword = {
                                    resetInputs()
                                    step = TwoFactorStep.ENTER_NEW
                                },
                                onChangePassword = {
                                    resetInputs()
                                    currentTarget = CurrentPasswordTarget.CHANGE_PASSWORD
                                    step = TwoFactorStep.ENTER_CURRENT
                                },
                                onChangeHint = {
                                    resetInputs()
                                    hintInput = userPreferences.twoFactorHint.orEmpty()
                                    currentTarget = CurrentPasswordTarget.CHANGE_HINT
                                    step = TwoFactorStep.ENTER_CURRENT
                                },
                                onDisableClick = {
                                    resetInputs()
                                    showDisableDialog = true
                                }
                            )
                        }

                        TwoFactorStep.ENTER_CURRENT -> {
                            TwoFactorCurrentPasswordStep(
                                strings = strings,
                                isDark = isDark,
                                password = currentPasswordInput,
                                onPasswordChange = {
                                    currentPasswordInput = it
                                    errorMessage = null
                                },
                                errorMessage = errorMessage,
                                onNext = {
                                    if (currentPasswordInput == userPreferences.twoFactorPassword) {
                                        errorMessage = null
                                        when (currentTarget) {
                                            CurrentPasswordTarget.CHANGE_PASSWORD -> {
                                                step = TwoFactorStep.ENTER_NEW
                                            }
                                            CurrentPasswordTarget.CHANGE_HINT -> {
                                                step = TwoFactorStep.ENTER_HINT
                                            }
                                            CurrentPasswordTarget.DISABLE -> {
                                                userPreferences.twoFactorPassword = null
                                                userPreferences.twoFactorHint = null
                                                step = TwoFactorStep.STATUS
                                                toastMessage = strings.twoFactorSuccessDisabledToast
                                            }
                                        }
                                    } else {
                                        errorMessage = strings.twoFactorPasswordWrong
                                    }
                                }
                            )
                        }

                        TwoFactorStep.ENTER_NEW -> {
                            TwoFactorNewPasswordStep(
                                strings = strings,
                                isDark = isDark,
                                password = newPasswordInput,
                                onPasswordChange = {
                                    newPasswordInput = it
                                    errorMessage = null
                                },
                                errorMessage = errorMessage,
                                onNext = {
                                    if (newPasswordInput.length < 6) {
                                        errorMessage = strings.twoFactorPasswordTooShort
                                    } else {
                                        errorMessage = null
                                        step = TwoFactorStep.CONFIRM_NEW
                                    }
                                }
                            )
                        }

                        TwoFactorStep.CONFIRM_NEW -> {
                            TwoFactorConfirmPasswordStep(
                                strings = strings,
                                isDark = isDark,
                                confirmPassword = confirmPasswordInput,
                                onConfirmPasswordChange = {
                                    confirmPasswordInput = it
                                    errorMessage = null
                                },
                                errorMessage = errorMessage,
                                onNext = {
                                    if (confirmPasswordInput != newPasswordInput) {
                                        errorMessage = strings.twoFactorPasswordMismatch
                                    } else {
                                        errorMessage = null
                                        step = TwoFactorStep.ENTER_HINT
                                    }
                                }
                            )
                        }

                        TwoFactorStep.ENTER_HINT -> {
                            TwoFactorHintStep(
                                strings = strings,
                                isDark = isDark,
                                hint = hintInput,
                                onHintChange = {
                                    hintInput = it
                                    errorMessage = null
                                },
                                passwordToAvoid = newPasswordInput.ifEmpty { userPreferences.twoFactorPassword.orEmpty() },
                                errorMessage = errorMessage,
                                onSave = { finalHint ->
                                    if (newPasswordInput.isNotEmpty()) {
                                        userPreferences.twoFactorPassword = newPasswordInput
                                        userPreferences.twoFactorHint = finalHint.ifBlank { null }
                                        toastMessage = if (hasTwoFactor) strings.twoFactorSuccessChangedToast else strings.twoFactorSuccessSetToast
                                    } else {
                                        userPreferences.twoFactorHint = finalHint.ifBlank { null }
                                        toastMessage = strings.twoFactorSuccessChangedToast
                                    }
                                    step = TwoFactorStep.STATUS
                                    resetInputs()
                                    keyboardController?.hide()
                                }
                            )
                        }
                    }
                }
            }
        }

        // ─── DISABLE CONFIRMATION DIALOG ───
        if (showDisableDialog) {
            TwoFactorDisableDialog(
                strings = strings,
                onConfirm = { inputPassword ->
                    if (inputPassword == userPreferences.twoFactorPassword) {
                        userPreferences.twoFactorPassword = null
                        userPreferences.twoFactorHint = null
                        showDisableDialog = false
                        toastMessage = strings.twoFactorSuccessDisabledToast
                    } else {
                        toastMessage = strings.twoFactorPasswordWrong
                    }
                },
                onDismiss = { showDisableDialog = false }
            )
        }

        // ─── TOAST NOTIFICATION ───
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            VibeToast(
                message = toastMessage.orEmpty(),
                isVisible = toastMessage != null,
                onDismiss = { toastMessage = null },
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
private fun TwoFactorStatusContent(
    isEnabled: Boolean,
    currentHint: String?,
    strings: VibeStrings,
    isDark: Boolean,
    onSetPassword: () -> Unit,
    onChangePassword: () -> Unit,
    onChangeHint: () -> Unit,
    onDisableClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Icon Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = if (isEnabled) {
                                listOf(
                                    Color(0xFF4CAF50).copy(alpha = if (isDark) 0.22f else 0.16f),
                                    Color(0xFF2196F3).copy(alpha = if (isDark) 0.16f else 0.12f)
                                )
                            } else {
                                listOf(
                                    Color(0xFF2196F3).copy(alpha = if (isDark) 0.22f else 0.16f),
                                    Color(0xFF9C27B0).copy(alpha = if (isDark) 0.16f else 0.12f)
                                )
                            }
                        )
                    )
                    .border(
                        width = 0.9.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                if (isEnabled) Color(0xFF4CAF50).copy(alpha = 0.50f) else Color(0xFF2196F3).copy(alpha = 0.50f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    (if (isEnabled) Color(0xFF4CAF50) else Color(0xFF2196F3)).copy(alpha = 0.35f),
                                    (if (isEnabled) Color(0xFF4CAF50) else Color(0xFF2196F3)).copy(alpha = 0.10f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isEnabled) Icons.Rounded.VerifiedUser else Icons.Rounded.Shield,
                        contentDescription = null,
                        tint = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF2196F3),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = if (isEnabled) strings.twoFactorEnabledBadge else strings.twoFactorTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = if (isEnabled) strings.twoFactorEnabledDesc else strings.twoFactorDescription,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                )

                if (isEnabled && !currentHint.isNullOrBlank()) {
                    Spacer(Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.TipsAndUpdates,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = strings.twoFactorCurrentHintPill(currentHint),
                                fontSize = 12.5.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        if (!isEnabled) {
            // Feature bullets
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.72f else 0.94f),
                border = BorderStroke(0.7.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    TwoFactorBulletRow(
                        icon = Icons.Rounded.Security,
                        tint = Color(0xFF2196F3),
                        title = strings.twoFactorBullet1Title,
                        desc = strings.twoFactorBullet1Desc
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 48.dp),
                        thickness = 0.6.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f)
                    )
                    TwoFactorBulletRow(
                        icon = Icons.Rounded.VpnKey,
                        tint = Color(0xFF9C27B0),
                        title = strings.twoFactorBullet2Title,
                        desc = strings.twoFactorBullet2Desc
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 48.dp),
                        thickness = 0.6.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f)
                    )
                    TwoFactorBulletRow(
                        icon = Icons.Rounded.TipsAndUpdates,
                        tint = Color(0xFFFF9800),
                        title = strings.twoFactorBullet3Title,
                        desc = strings.twoFactorBullet3Desc
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = onSetPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = strings.twoFactorSetPasswordBtn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.5.sp
                )
            }
        } else {
            // Actions when enabled
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.72f else 0.94f),
                border = BorderStroke(0.7.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                    TwoFactorActionRow(
                        icon = Icons.Rounded.Key,
                        iconTint = Color(0xFF2196F3),
                        text = strings.twoFactorChangePasswordBtn,
                        onClick = onChangePassword
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 48.dp),
                        thickness = 0.6.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f)
                    )
                    TwoFactorActionRow(
                        icon = Icons.Rounded.TipsAndUpdates,
                        iconTint = Color(0xFFFF9800),
                        text = strings.twoFactorChangeHintBtn,
                        onClick = onChangeHint
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 48.dp),
                        thickness = 0.6.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f)
                    )
                    TwoFactorActionRow(
                        icon = Icons.Rounded.LockOpen,
                        iconTint = Color(0xFFF44336),
                        text = strings.twoFactorDisableBtn,
                        textColor = Color(0xFFF44336),
                        onClick = onDisableClick
                    )
                }
            }
        }
    }
}

@Composable
private fun TwoFactorNewPasswordStep(
    strings: VibeStrings,
    isDark: Boolean,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String?,
    onNext: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val strength = remember(password) { calculatePasswordStrength(password) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = strings.twoFactorEnterNewPasswordTitle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = strings.twoFactorEnterNewPasswordSubtitle,
            fontSize = 13.5.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = { Text(strings.twoFactorPasswordFieldLabel) },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { if (password.length >= 6) onNext() }),
            shape = RoundedCornerShape(16.dp),
            isError = errorMessage != null,
            supportingText = errorMessage?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )

        if (password.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            TwoFactorStrengthIndicator(strength = strength, strings = strings)
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onNext,
            enabled = password.length >= 6,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = strings.twoFactorNextBtn,
                fontWeight = FontWeight.Bold,
                fontSize = 15.5.sp
            )
        }
    }
}

@Composable
private fun TwoFactorConfirmPasswordStep(
    strings: VibeStrings,
    isDark: Boolean,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    errorMessage: String?,
    onNext: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircleOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = strings.twoFactorRepeatPasswordTitle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = strings.twoFactorRepeatPasswordSubtitle,
            fontSize = 13.5.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = { Text(strings.twoFactorConfirmFieldLabel) },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { if (confirmPassword.isNotEmpty()) onNext() }),
            shape = RoundedCornerShape(16.dp),
            isError = errorMessage != null,
            supportingText = errorMessage?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onNext,
            enabled = confirmPassword.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = strings.twoFactorNextBtn,
                fontWeight = FontWeight.Bold,
                fontSize = 15.5.sp
            )
        }
    }
}

@Composable
private fun TwoFactorCurrentPasswordStep(
    strings: VibeStrings,
    isDark: Boolean,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String?,
    onNext: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Password,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = strings.twoFactorEnterCurrentPasswordTitle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = strings.twoFactorEnterCurrentPasswordSubtitle,
            fontSize = 13.5.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = { Text(strings.twoFactorCurrentFieldLabel) },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { if (password.isNotEmpty()) onNext() }),
            shape = RoundedCornerShape(16.dp),
            isError = errorMessage != null,
            supportingText = errorMessage?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onNext,
            enabled = password.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = strings.twoFactorNextBtn,
                fontWeight = FontWeight.Bold,
                fontSize = 15.5.sp
            )
        }
    }
}

@Composable
private fun TwoFactorHintStep(
    strings: VibeStrings,
    isDark: Boolean,
    hint: String,
    onHintChange: (String) -> Unit,
    passwordToAvoid: String,
    errorMessage: String?,
    onSave: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val hintTooLong = hint.length > 32
    val hintContainsPassword = passwordToAvoid.isNotBlank() && hint.isNotBlank() && hint.contains(passwordToAvoid, ignoreCase = true)

    val validationError = when {
        errorMessage != null -> errorMessage
        hintTooLong -> strings.twoFactorHintTooLong
        hintContainsPassword -> strings.twoFactorHintContainsPassword
        else -> null
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.TipsAndUpdates,
            contentDescription = null,
            tint = Color(0xFFFF9800),
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = strings.twoFactorHintTitle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = strings.twoFactorHintSubtitle,
            fontSize = 13.5.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = hint,
            onValueChange = { if (it.length <= 40) onHintChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = { Text(strings.twoFactorHintFieldLabel) },
            placeholder = { Text(strings.twoFactorHintPlaceholder) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            isError = validationError != null,
            supportingText = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = validationError ?: strings.twoFactorHintPublicWarning,
                        color = if (validationError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.5.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${hint.length}/32",
                        color = if (hintTooLong) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.5.sp
                    )
                }
            }
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = { onSave(hint.trim()) },
            enabled = !hintTooLong && !hintContainsPassword,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = strings.twoFactorSaveBtn,
                fontWeight = FontWeight.Bold,
                fontSize = 15.5.sp
            )
        }

        if (hint.isBlank()) {
            Spacer(Modifier.height(10.dp))
            TextButton(
                onClick = { onSave("") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = strings.twoFactorSkipBtn,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.5.sp
                )
            }
        }
    }
}

@Composable
private fun TwoFactorStrengthIndicator(strength: Int, strings: VibeStrings) {
    val label = when (strength) {
        0, 1 -> strings.twoFactorStrengthWeak
        2 -> strings.twoFactorStrengthMedium
        3 -> strings.twoFactorStrengthStrong
        else -> strings.twoFactorStrengthVeryStrong
    }
    val color = when (strength) {
        0, 1 -> Color(0xFFF44336)
        2 -> Color(0xFFFF9800)
        3 -> Color(0xFF4CAF50)
        else -> Color(0xFF00C853)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(4) { i ->
                val filled = i < strength
                val animColor by animateColorAsState(
                    targetValue = if (filled) color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    label = "strength_seg_$i"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(animColor)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TwoFactorBulletRow(
    icon: ImageVector,
    tint: Color,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(tint.copy(alpha = 0.14f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 12.5.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
private fun TwoFactorActionRow(
    icon: ImageVector,
    iconTint: Color,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconTint.copy(alpha = 0.14f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun TwoFactorDisableDialog(
    strings: VibeStrings,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        icon = {
            Icon(
                imageVector = Icons.Rounded.GppBad,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = strings.twoFactorDisableConfirmTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = strings.twoFactorDisableConfirmDesc,
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(strings.twoFactorCurrentFieldLabel) },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(password) },
                enabled = password.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(strings.twoFactorDisableAction)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(strings.cancelBtn)
            }
        }
    )
}

private fun calculatePasswordStrength(password: String): Int {
    if (password.isEmpty()) return 0
    var score = 0
    if (password.length >= 8) score++
    if (password.length >= 12) score++
    if (password.any { it.isDigit() } && password.any { it.isLetter() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    val weak = listOf("password", "123456", "qwerty", "111111", "vibe", "пароль")
    if (weak.any { password.contains(it, ignoreCase = true) }) score = minOf(score, 1)
    return score.coerceIn(0, 4)
}
