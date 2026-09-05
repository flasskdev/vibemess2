package com.flasskdev.vibe.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Backspace
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.ui.theme.luminanceIsDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PASSCODE_LENGTH = 4

@Composable
fun PasscodeAuthScreen(
    userPreferences: UserPreferences,
    onSuccess: () -> Unit,
    onLogout: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val context = androidx.compose.ui.platform.LocalContext.current

    androidx.activity.compose.BackHandler(enabled = true) {
        (context as? android.app.Activity)?.moveTaskToBack(true)
    }

    val savedPin = userPreferences.passcode

    if (savedPin == null) {
        // Fallback in case navigated here but no pin is set
        LaunchedEffect(Unit) {
            onSuccess()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Ambient wash keeps the lock screen from looking like a flat grey wall
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            PasscodeContent(
                title = strings.passcodeEnterTitle,
                subtitle = strings.passcodeEnterSubtitle,
                errorText = if (isError) strings.passcodeWrongCode else null,
                showLockBadge = true,
                pin = pin,
                isError = isError,
                onPinChange = { newPin ->
                    if (isError) isError = false
                    pin = newPin
                    if (pin.length == PASSCODE_LENGTH) {
                        if (pin == savedPin) {
                            onSuccess()
                        } else {
                            isError = true
                            scope.launch {
                                delay(600)
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
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = strings.btnLogout,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
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
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val hasPasscode = userPreferences.passcode != null

    val handleBack = {
        if (step != Step.INFO) {
            step = Step.INFO
            pin = ""
            firstPin = ""
            isError = false
        } else {
            onBack()
        }
    }

    androidx.activity.compose.BackHandler(enabled = true) {
        handleBack()
    }

    val title = when (step) {
        Step.INFO -> ""
        Step.ENTER_CURRENT -> strings.passcodeEnterCurrentTitle
        Step.ENTER_NEW -> strings.passcodeCreateTitle
        Step.CONFIRM_NEW -> strings.passcodeRepeatTitle
    }
    // Error copy depends on the step: a wrong current code and a mismatched repeat are
    // two different mistakes and used to share one silent red flash.
    val errorText = when {
        !isError -> null
        step == Step.ENTER_CURRENT -> strings.passcodeWrongCode
        else -> strings.passcodeMismatch
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                IconButton(onClick = { handleBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = strings.backBtn,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (hasPasscode && step == Step.ENTER_NEW) {
                    TextButton(onClick = { showRemoveDialog = true }) {
                        Text(
                            text = strings.passcodeDisableShort,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                if (step == Step.INFO) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PasscodeLockBadge(size = 96.dp, iconSize = 44.dp, locked = hasPasscode)

                        Spacer(modifier = Modifier.height(26.dp))

                        Text(
                            text = strings.passcodeInfoTitle,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = strings.passcodeInfoText,
                            fontSize = 15.sp,
                            lineHeight = 21.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(44.dp))

                        if (hasPasscode) {
                            Button(
                                onClick = { step = Step.ENTER_CURRENT },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = strings.passcodeChangeBtn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = { showRemoveDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = strings.passcodeDisableBtn,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        } else {
                            Button(
                                onClick = { step = Step.ENTER_NEW },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = strings.passcodeEnableBtn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                } else {
                    PasscodeContent(
                        title = title,
                        subtitle = strings.passcodeEnterSubtitle,
                        errorText = errorText,
                        showLockBadge = false,
                        pin = pin,
                        isError = isError,
                        onPinChange = { newPin ->
                            if (isError) isError = false
                            pin = newPin

                            if (pin.length == PASSCODE_LENGTH) {
                                when (step) {
                                    Step.ENTER_CURRENT -> {
                                        if (pin == userPreferences.passcode) {
                                            step = Step.ENTER_NEW
                                            pin = ""
                                        } else {
                                            isError = true
                                            scope.launch {
                                                delay(600)
                                                pin = ""
                                                isError = false
                                            }
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
                                                delay(600)
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
    }

    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.error.copy(alpha = 0.14f),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LockOpen,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            title = {
                Text(
                    text = strings.passcodeRemoveTitle,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            text = {
                Text(
                    text = strings.passcodeRemoveText,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    userPreferences.passcode = null
                    showRemoveDialog = false
                    onBack()
                }) {
                    Text(
                        text = strings.passcodeDisableShort,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text(
                        text = strings.cancelBtn,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

private enum class Step {
    INFO, ENTER_CURRENT, ENTER_NEW, CONFIRM_NEW
}

/** Gradient squircle holding the lock glyph, shared by the auth and setup screens. */
@Composable
private fun PasscodeLockBadge(
    size: androidx.compose.ui.unit.Dp,
    iconSize: androidx.compose.ui.unit.Dp,
    locked: Boolean = true
) {
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier.size(size + 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = 0.24f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(size / 3))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.22f),
                            primary.copy(alpha = 0.10f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.5f),
                            primary.copy(alpha = 0.08f)
                        )
                    ),
                    shape = RoundedCornerShape(size / 3)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (locked) Icons.Rounded.Lock else Icons.Rounded.LockOpen,
                contentDescription = strings.a11yPasscodeLock,
                modifier = Modifier.size(iconSize),
                tint = primary
            )
        }
    }
}

@Composable
private fun PasscodeContent(
    title: String,
    subtitle: String?,
    errorText: String?,
    showLockBadge: Boolean,
    pin: String,
    isError: Boolean,
    onPinChange: (String) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    // Wrong code shakes the dot row instead of only tinting it red: motion is read much
    // faster than colour, especially on the lock screen.
    val shake = remember { Animatable(0f) }
    LaunchedEffect(isError) {
        if (isError) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            shake.snapTo(0f)
            shake.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0f at 0 using FastOutSlowInEasing
                    -14f at 60 using FastOutSlowInEasing
                    12f at 130 using FastOutSlowInEasing
                    -8f at 200 using FastOutSlowInEasing
                    4f at 280 using FastOutSlowInEasing
                    0f at 400
                }
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showLockBadge) {
            PasscodeLockBadge(size = 72.dp, iconSize = 32.dp)
            Spacer(modifier = Modifier.height(20.dp))
        }

        Text(
            text = title,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
        )

        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── PIN dots ──
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.graphicsLayer { translationX = shake.value }
        ) {
            for (i in 0 until PASSCODE_LENGTH) {
                val isFilled = i < pin.length
                val dotSize by animateDpAsState(
                    targetValue = if (isFilled) 18.dp else 14.dp,
                    animationSpec = spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium),
                    label = "pinDot_$i"
                )
                val dotColor = when {
                    isError -> MaterialTheme.colorScheme.error
                    isFilled -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.18f)
                }
                Box(
                    modifier = Modifier
                        .size(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .clip(CircleShape)
                            .background(dotColor)
                            .then(
                                if (isFilled && !isError) {
                                    Modifier.border(
                                        width = 4.dp,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                                        shape = CircleShape
                                    )
                                } else Modifier
                            )
                    )
                }
            }
        }

        // ── Error line (reserved height so the keypad never jumps) ──
        Box(
            modifier = Modifier
                .height(30.dp)
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            this@Column.AnimatedVisibility(
                visible = errorText != null,
                enter = fadeIn(tween(140)),
                exit = fadeOut(tween(140))
            ) {
                Text(
                    text = errorText.orEmpty(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        val buttonSpacing = 22.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9")
            ).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    row.forEach { digit ->
                        NumButton(digit) {
                            if (pin.length < PASSCODE_LENGTH) {
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onPinChange(pin + digit)
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(74.dp)) // Empty space for alignment
                NumButton("0") {
                    if (pin.length < PASSCODE_LENGTH) {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onPinChange(pin + "0")
                    }
                }

                val backspaceEnabled = pin.isNotEmpty()
                val backspaceAlpha by animateFloatAsState(
                    targetValue = if (backspaceEnabled) 1f else 0.3f,
                    animationSpec = tween(160),
                    label = "backspaceAlpha"
                )
                val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
                Box(
                    modifier = Modifier
                        .size(74.dp)
                        .clip(CircleShape)
                        .clickable(
                            enabled = backspaceEnabled,
                            onClickLabel = strings.a11yPasscodeBackspace
                        ) {
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onPinChange(pin.dropLast(1))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Backspace,
                        contentDescription = strings.a11yPasscodeBackspace,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = backspaceAlpha),
                        modifier = Modifier.size(24.dp)
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
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDark = MaterialTheme.colorScheme.background.luminanceIsDark()

    // Physical key feel: the button dips and brightens under the finger
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 700f),
        label = "keyScale_$number"
    )

    Box(
        modifier = Modifier
            .size(74.dp)
            .scale(pressScale)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    colors = if (isPressed) {
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.26f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                        )
                    } else {
                        listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (isDark) 0.55f else 0.75f),
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (isDark) 0.35f else 0.55f)
                        )
                    }
                )
            )
            .border(
                width = 0.7.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f),
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClickLabel = strings.a11yPasscodeDigit(number),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 31.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}