package com.flasskdev.vibe.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.flasskdev.vibe.ui.theme.*
import kotlinx.coroutines.delay
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid

@Composable
fun VibeBackgroundMesh() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
    )
}

@Composable
fun VibeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "buttonScale")

    Box(
        modifier = modifier
            .scale(scale)
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 6.dp else 0.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = VibePrimary.copy(alpha = 0.25f),
                ambientColor = VibePrimary.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                color = VibePrimary,
                shape = RoundedCornerShape(24.dp)
            )
            .then(
                if (enabled) Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                ) else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .then(if (!enabled) Modifier.background(Color.Black.copy(alpha = 0.35f)) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun VibeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 14.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label.uppercase(),
                color = if (error != null) VibeError else if (isFocused) VibePrimary else androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Black
            )
            if (error != null) {
                Text(
                    text = error,
                    color = VibeError,
                    style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .shadow(
                    elevation = if (isFocused) 0.dp else 3.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = Color.Black.copy(alpha = 0.05f),
                    ambientColor = Color.Black.copy(alpha = 0.03f)
                )
                .background(
                    androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                    RoundedCornerShape(20.dp)
                )
                .border(
                    width = if (isFocused) 1.5.dp else 0.dp,
                    color = if (error != null) VibeError else if (isFocused) VibePrimary else Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 18.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Box(modifier = Modifier.padding(end = 12.dp)) {
                        leadingIcon()
                    }
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    interactionSource = interactionSource,
                    textStyle = TextStyle(
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground, 
                        fontSize = 17.sp, 
                        fontWeight = FontWeight.Normal
                    ),
                    cursorBrush = SolidColor(VibePrimary),
                    keyboardOptions = keyboardOptions,
                    decorationBox = { innerTextField ->
                        innerTextField()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun OtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                    onValueChange(it)
                }
            },
            modifier = Modifier.size(1.dp).focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(color = Color.Transparent)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { 
                    focusRequester.requestFocus()
                    keyboardController?.show() 
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(6) { index ->
                val char = value.getOrNull(index)?.toString() ?: ""
                val isFocused = value.length == index
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .shadow(
                            elevation = if (isFocused) 0.dp else 3.dp,
                            shape = RoundedCornerShape(18.dp),
                            spotColor = Color.Black.copy(alpha = 0.05f),
                            ambientColor = Color.Black.copy(alpha = 0.03f)
                        )
                        .background(
                            androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                            RoundedCornerShape(18.dp)
                        )
                        .border(
                            width = if (isFocused) 2.dp else 0.dp,
                            color = if (isFocused) VibePrimary else Color.Transparent,
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MutableInteractionSource.collectIsPressedAsState(): State<Boolean> {
    val isPressed = remember { mutableStateOf(false) }
    LaunchedEffect(this) {
        interactions.collect { interaction ->
            when (interaction) {
                is androidx.compose.foundation.interaction.PressInteraction.Press -> isPressed.value = true
                is androidx.compose.foundation.interaction.PressInteraction.Release -> isPressed.value = false
                is androidx.compose.foundation.interaction.PressInteraction.Cancel -> isPressed.value = false
            }
        }
    }
    return isPressed
}

@Composable
fun MutableInteractionSource.collectIsFocusedAsState(): State<Boolean> {
    val isFocused = remember { mutableStateOf(false) }
    LaunchedEffect(this) {
        interactions.collect { interaction ->
            when (interaction) {
                is androidx.compose.foundation.interaction.FocusInteraction.Focus -> isFocused.value = true
                is androidx.compose.foundation.interaction.FocusInteraction.Unfocus -> isFocused.value = false
            }
        }
    }
    return isFocused
}

@Composable
fun VibeToast(
    message: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(18.dp),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                )
                .clip(RoundedCornerShape(18.dp))
                .background(androidx.compose.material3.MaterialTheme.colorScheme.inverseSurface)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = androidx.compose.material3.MaterialTheme.colorScheme.inversePrimary,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun parseHexColor(hex: String?): Color? {
    if (hex.isNullOrBlank()) return null
    return try {
        val clean = hex.trim().removePrefix("#")
        when (clean.length) {
            3 -> {
                val r = clean[0].toString().repeat(2).toInt(16)
                val g = clean[1].toString().repeat(2).toInt(16)
                val b = clean[2].toString().repeat(2).toInt(16)
                Color(r, g, b, 255)
            }
            6 -> {
                val colorInt = clean.toLong(16)
                Color(
                    red = ((colorInt shr 16) and 0xFF).toInt(),
                    green = ((colorInt shr 8) and 0xFF).toInt(),
                    blue = (colorInt and 0xFF).toInt(),
                    alpha = 255
                )
            }
            8 -> {
                val colorInt = clean.toLong(16)
                Color(
                    alpha = ((colorInt shr 24) and 0xFF).toInt(),
                    red = ((colorInt shr 16) and 0xFF).toInt(),
                    green = ((colorInt shr 8) and 0xFF).toInt(),
                    blue = (colorInt and 0xFF).toInt()
                )
            }
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

@Composable
fun InlineKeyboard(
    replyMarkup: com.flasskdev.vibe.data.local.ReplyMarkup,
    onButtonClick: (com.flasskdev.vibe.data.local.InlineKeyboardButton) -> Unit,
    modifier: Modifier = Modifier,
    liquidState: io.github.fletchmckee.liquid.LiquidState? = null,
    isInteractionEnabled: Boolean = true,
    pendingCallbackData: String? = null
) {
    val rows = replyMarkup.inlineKeyboard.take(10) // Limit max 10 rows
    if (rows.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        rows.forEach { row ->
            val buttons = row.take(5) // Limit max 5 per row
            if (buttons.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    buttons.forEach { button ->
                        InlineKeyboardButtonItem(
                            button = button,
                            onClick = { onButtonClick(button) },
                            liquidState = liquidState,
                            enabled = isInteractionEnabled,
                            isLoading = pendingCallbackData != null && button.callbackData == pendingCallbackData,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InlineKeyboardButtonItem(
    button: com.flasskdev.vibe.data.local.InlineKeyboardButton,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    liquidState: io.github.fletchmckee.liquid.LiquidState? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val isDark = androidx.compose.material3.MaterialTheme.colorScheme.background == VibeBackgroundDark

    // Custom or default colors
    val parsedBgColor = remember(button.bgColor) { parseHexColor(button.bgColor) }
    val parsedTextColor = remember(button.textColor) { parseHexColor(button.textColor) }

    val defaultTextColor = if (isDark) Color.White else Color(0xFF0F172A)
    val textColor = parsedTextColor ?: defaultTextColor

    val shape = RoundedCornerShape(18.dp)

    // Inline keyboards can appear many times in a lazy chat list. Avoid per-button liquid
    // distortion here: it is expensive during scroll and can delay hit testing on lower-end devices.

    // A calm, high-contrast Material-like surface remains readable over both chat bubbles.
    val defaultSurfaceColor = if (isDark) {
        Color(0xFF34343A).copy(alpha = if (isPressed) 0.96f else 0.90f)
    } else {
        Color.White.copy(alpha = if (isPressed) 0.98f else 0.94f)
    }

    val glassTint = if (parsedBgColor != null) {
        parsedBgColor.copy(alpha = if (isPressed) 0.92f else 0.82f)
    } else {
        defaultSurfaceColor
    }
    val waitingTransition = rememberInfiniteTransition(label = "inlineButtonWaitingPulse")
    val waitingPulse by waitingTransition.animateFloat(
        initialValue = 0.30f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "inlineButtonWaitingPulseAlpha"
    )
    val targetSurfaceColor = when {
        isLoading -> VibePrimary.copy(alpha = 0.52f + (waitingPulse * 0.24f))
        !enabled -> glassTint.copy(alpha = 0.42f)
        else -> glassTint
    }
    val surfaceColor by animateColorAsState(
        targetValue = targetSurfaceColor,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "inlineButtonSurface"
    )
    val waitingBarProgress by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "inlineButtonWaitingBar"
    )
    val displayedTextColor = textColor.copy(alpha = if (enabled) 1f else 0.52f)

    // Luminous glass edge highlight
    val borderBrush = if (parsedBgColor != null) {
        Brush.verticalGradient(
            colors = listOf(
                parsedBgColor.copy(alpha = 0.75f),
                parsedBgColor.copy(alpha = 0.25f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = if (isDark) 0.38f else 0.65f),
                Color.White.copy(alpha = if (isDark) 0.10f else 0.20f)
            )
        )
    }

    // Internal glass specular gloss highlight
    val glossBrush = remember(isDark, parsedBgColor) {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = if (isDark) 0.14f else 0.22f),
                Color.Transparent
            )
        )
    }

    // Material Button owns both measurement and pointer handling, so its visual bounds and
    // touch target are identical even inside a scrolling lazy list.
    androidx.compose.material3.Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        shape = shape,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = surfaceColor,
            contentColor = displayedTextColor,
            disabledContainerColor = surfaceColor,
            disabledContentColor = displayedTextColor
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        interactionSource = interactionSource
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = button.text,
                    color = if (isLoading) Color.White else displayedTextColor,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                if (!button.url.isNullOrBlank()) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        tint = displayedTextColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
            // A quiet progress accent: no spinner and no text replacement.
            if (waitingBarProgress > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(waitingBarProgress)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(VibeWaitingAccent.copy(alpha = 0.42f + (waitingPulse * 0.40f)))
                )
            }
        }
    }
}