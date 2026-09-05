package com.flasskdev.vibe.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.flasskdev.vibe.ui.components.VibeToastHost
import com.flasskdev.vibe.ui.theme.*
import kotlin.math.roundToInt

/* ============================================================================
 *  ПУНКТ 9 — ЭКРАНЫ ДВУХФАКТОРНОЙ АУТЕНТИФИКАЦИИ
 * ========================================================================== */

/**
 * Экран второго фактора при входе.
 * Показывается ПОСЛЕ ввода кода из письма, если 2FA включена.
 */
@Composable
fun TwoFactorChallengeScreen(
    hint: String?,
    attemptsLeft: Int?,
    isLoading: Boolean,
    errorMessage: String?,
    onSubmit: (password: String) -> Unit,
    onForgot: () -> Unit,
    onBack: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var hintShown by remember { mutableStateOf(false) }
    val focus = remember { FocusRequester() }
    val glass = glassStyle()

    // Тряска поля при неверном пароле — понятнее любой надписи.
    val shake = remember { Animatable(0f) }
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            shake.animateTo(1f, keyframes {
                durationMillis = 380
                0f at 0; 1f at 60; -1f at 120; 0.6f at 190; -0.4f at 260; 0f at 380
            })
            shake.snapTo(0f)
        }
    }

    LaunchedEffect(Unit) { focus.requestFocus() }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        AuroraBackdrop()

        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(horizontal = VibeSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(VibeRadius.xl))
                    .background(Brush.linearGradient(VibeAuroraGradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Lock, null, tint = Color.White, modifier = Modifier.size(42.dp))
            }

            Spacer(Modifier.height(VibeSpacing.xl))

            Text(
                "Двухфакторная защита",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(VibeSpacing.sm))
            Text(
                "Введите пароль, который вы задали в настройках безопасности",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(Modifier.height(VibeSpacing.xxl))

            OutlinedTextField(
                value = password,
                onValueChange = { if (it.length <= 128) password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus)
                    .graphicsLayer { translationX = shake.value * 16f },
                label = { Text("Пароль") },
                singleLine = true,
                isError = errorMessage != null,
                shape = RoundedCornerShape(VibeRadius.md),
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { if (password.isNotEmpty()) onSubmit(password) }),
                trailingIcon = {
                    IconButton(onClick = { visible = !visible }) {
                        Icon(
                            if (visible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = if (visible) "Скрыть" else "Показать"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibeViolet,
                    focusedLabelColor = VibeViolet,
                    cursorColor = VibeViolet
                )
            )

            /* ---------- подсказка ---------- */
            if (!hint.isNullOrBlank()) {
                Spacer(Modifier.height(VibeSpacing.md))
                // Подсказку не показываем сразу: она нужна, только когда
                // пароль реально забыт, а на экране её видит любой, кто
                // заглянет через плечо.
                AnimatedContent(
                    targetState = hintShown,
                    transitionSpec = { fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically() },
                    label = "hint"
                ) { shown ->
                    if (shown) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(VibeRadius.md))
                                .background(VibeViolet.copy(alpha = 0.10f))
                                .padding(VibeSpacing.md),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Rounded.TipsAndUpdates, null,
                                tint = VibeViolet, modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(VibeSpacing.md))
                            Column {
                                Text(
                                    "Ваша подсказка",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = VibeViolet
                                )
                                Text(
                                    hint,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    } else {
                        TextButton(onClick = { hintShown = true }) {
                            Icon(Icons.Rounded.TipsAndUpdates, null, modifier = Modifier.size(18.dp), tint = VibeViolet)
                            Spacer(Modifier.width(VibeSpacing.sm))
                            Text("Показать подсказку", color = VibeViolet)
                        }
                    }
                }
            }

            AnimatedVisibility(errorMessage != null) {
                Column {
                    Spacer(Modifier.height(VibeSpacing.md))
                    Text(
                        buildString {
                            append(errorMessage.orEmpty())
                            if (attemptsLeft != null && attemptsLeft > 0) append(" · осталось попыток: $attemptsLeft")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.height(VibeSpacing.xl))

            Button(
                onClick = { onSubmit(password) },
                enabled = password.isNotEmpty() && !isLoading,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(VibeRadius.md),
                colors = ButtonDefaults.buttonColors(containerColor = VibeViolet)
            ) {
                if (isLoading) CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                else Text("Войти", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(VibeSpacing.md))

            TextButton(onClick = onForgot) {
                Text("Забыли пароль?", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier.systemBarsPadding().padding(VibeSpacing.sm)
        ) {
            Icon(Icons.Rounded.ArrowBack, "Назад", tint = MaterialTheme.colorScheme.onBackground)
        }

        VibeToastHost(Modifier.align(Alignment.BottomCenter))
    }
}

/**
 * Настройка 2FA: пароль + подтверждение + подсказка (макс. 32 символа).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFactorSetupScreen(
    isEnabled: Boolean,
    currentHint: String?,
    isLoading: Boolean,
    onSave: (password: String, hint: String, currentPassword: String?) -> Unit,
    onDisable: (currentPassword: String) -> Unit,
    onBack: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf(currentHint.orEmpty()) }
    var showDisable by remember { mutableStateOf(false) }
    val glass = glassStyle()

    val strength = remember(password) { passwordStrength(password) }
    val hintTooLong = hint.length > 32
    val hintContainsPassword = password.isNotEmpty() && hint.contains(password, ignoreCase = true)
    val mismatch = confirm.isNotEmpty() && confirm != password

    val canSave = password.length >= 6 && !mismatch && confirm.isNotEmpty() &&
                  !hintTooLong && !hintContainsPassword &&
                  (!isEnabled || currentPassword.isNotEmpty())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (isEnabled) "Изменить пароль" else "Двухфакторная защита") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, "Назад") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScrollCompat()
                .padding(horizontal = VibeSpacing.lg)
                .imePadding(),
        ) {
            InfoCard(glass)

            Spacer(Modifier.height(VibeSpacing.xl))

            if (isEnabled) {
                SecureField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Текущий пароль"
                )
                Spacer(Modifier.height(VibeSpacing.md))
            }

            SecureField(value = password, onValueChange = { password = it }, label = "Новый пароль")

            if (password.isNotEmpty()) {
                Spacer(Modifier.height(VibeSpacing.sm))
                StrengthBar(strength)
            }

            Spacer(Modifier.height(VibeSpacing.md))

            SecureField(
                value = confirm,
                onValueChange = { confirm = it },
                label = "Повторите пароль",
                isError = mismatch,
                supporting = if (mismatch) "Пароли не совпадают" else null
            )

            Spacer(Modifier.height(VibeSpacing.xl))

            /* ---------- подсказка ---------- */
            OutlinedTextField(
                value = hint,
                onValueChange = { if (it.length <= 40) hint = it },   // 40 чтобы показать ошибку, а не обрезать молча
                label = { Text("Подсказка (необязательно)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = hintTooLong || hintContainsPassword,
                shape = RoundedCornerShape(VibeRadius.md),
                supportingText = {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            when {
                                hintContainsPassword -> "Подсказка не должна содержать сам пароль"
                                hintTooLong -> "Слишком длинная"
                                else -> "Увидит любой, кто попытается войти"
                            },
                            color = if (hintTooLong || hintContainsPassword) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            "${hint.length}/32",
                            color = if (hintTooLong) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibeViolet, focusedLabelColor = VibeViolet, cursorColor = VibeViolet
                )
            )

            Spacer(Modifier.height(VibeSpacing.xxl))

            Button(
                onClick = { onSave(password, hint.trim(), currentPassword.takeIf { isEnabled }) },
                enabled = canSave && !isLoading,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(VibeRadius.md),
                colors = ButtonDefaults.buttonColors(containerColor = VibeViolet)
            ) {
                if (isLoading) CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                else Text(if (isEnabled) "Сохранить" else "Включить защиту")
            }

            if (isEnabled) {
                Spacer(Modifier.height(VibeSpacing.md))
                TextButton(
                    onClick = { showDisable = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отключить двухфакторную защиту", color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(Modifier.height(VibeSpacing.section))
        }
    }

    if (showDisable) {
        DisableDialog(
            onConfirm = { pwd -> showDisable = false; onDisable(pwd) },
            onDismiss = { showDisable = false }
        )
    }
}

@Composable
private fun InfoCard(glass: VibeGlassStyle) {
    Row(
        Modifier.fillMaxWidth().vibeCard(glass, VibeRadius.lg).padding(VibeSpacing.lg),
        verticalAlignment = Alignment.Top
    ) {
        Icon(Icons.Rounded.Shield, null, tint = VibeViolet, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(VibeSpacing.md))
        Column {
            Text(
                "Дополнительный пароль",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(VibeSpacing.xs))
            Text(
                "После ввода кода из письма потребуется этот пароль. " +
                "Даже если кто-то получит доступ к вашей почте, войти он не сможет.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SecureField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    supporting: String? = null
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= 128) onValueChange(it) },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError,
        shape = RoundedCornerShape(VibeRadius.md),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        supportingText = supporting?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(if (visible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility, null)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VibeViolet, focusedLabelColor = VibeViolet, cursorColor = VibeViolet
        )
    )
}

@Composable
private fun StrengthBar(strength: Int) {
    val label = when (strength) {
        0, 1 -> "Слабый"
        2 -> "Средний"
        3 -> "Хороший"
        else -> "Отличный"
    }
    val color = when (strength) {
        0, 1 -> VibeError
        2 -> VibeWarning
        3 -> VibeSuccess
        else -> VibeSuccess
    }
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
            repeat(4) { i ->
                val filled = i < strength
                val animColor by animateColorAsState(
                    if (filled) color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    label = "seg$i"
                )
                Box(
                    Modifier
                        .weight(1f).height(4.dp)
                        .clip(RoundedCornerShape(VibeRadius.pill))
                        .background(animColor)
                )
            }
        }
        Spacer(Modifier.height(VibeSpacing.xs))
        Text(label, style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Composable
private fun DisableDialog(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var pwd by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(VibeRadius.lg),
        icon = { Icon(Icons.Rounded.GppBad, null, tint = MaterialTheme.colorScheme.error) },
        title = { Text("Отключить защиту?") },
        text = {
            Column {
                Text("Для входа снова будет достаточно только кода из письма.")
                Spacer(Modifier.height(VibeSpacing.md))
                SecureField(pwd, { pwd = it }, "Текущий пароль")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(pwd) }, enabled = pwd.isNotEmpty()) {
                Text("Отключить", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

@Composable
private fun AuroraBackdrop() {
    val t = rememberInfiniteTransition(label = "aurora")
    val shift by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Reverse),
        label = "shift"
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = VibeAuroraSoft + Color.Transparent,
                    radius = 900f + shift * 240f
                )
            )
    )
}

/** Оценка стойкости 0..4. Без zxcvbn: она бы утянула 1.5 МБ словарей в APK. */
private fun passwordStrength(password: String): Int {
    if (password.isEmpty()) return 0
    var score = 0
    if (password.length >= 8) score++
    if (password.length >= 12) score++
    if (password.any { it.isDigit() } && password.any { it.isLetter() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    // Явно слабые пароли не должны показывать зелёное.
    val weak = listOf("password", "123456", "qwerty", "111111", "vibe", "пароль")
    if (weak.any { password.contains(it, ignoreCase = true) }) score = minOf(score, 1)
    return score.coerceIn(0, 4)
}

@Composable
private fun Modifier.verticalScrollCompat(): Modifier =
    this.then(Modifier.verticalScroll(rememberScrollState()))
