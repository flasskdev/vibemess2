package com.flasskdev.vibe.ui.circles

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.flasskdev.vibe.ui.theme.*

/**
 * Какой кружок сейчас активен. Один на всё приложение — иначе десять кружков
 * в переписке подняли бы десять ExoPlayer'ов одновременно и уронили бы прокрутку.
 * mutableStateOf, поэтому Compose сам перерисует и старый, и новый кружок.
 */
object ActiveCircle {
    var activeMessageId by mutableStateOf<Int?>(null)

    fun toggle(messageId: Int) {
        activeMessageId = if (activeMessageId == messageId) null else messageId
    }
}

/**
 * Кружочек в ленте сообщений.
 *
 *  - Пока не в фокусе — статичная обложка (ExoPlayer НЕ создаётся).
 *    Это принципиально: 10 кружочков в чате = 10 декодеров = гарантированный
 *    ANR на среднем телефоне. Плеер создаётся только для активного кружка.
 *  - Тап — воспроизведение, повторный тап — пауза.
 *  - Долгий тап по кружку — звук вкл/выкл (первый прогон беззвучный,
 *    как в Telegram, чтобы не пугать в общественном месте).
 *  - Кольцо вокруг показывает прогресс.
 */
@OptIn(UnstableApi::class)
@Composable
fun CircleMessageBubble(
    videoUrl: String,
    thumbUrl: String?,
    durationMs: Long,
    isMine: Boolean,
    isActive: Boolean,
    onActivate: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 208.dp
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var muted by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0f) }
    var playing by remember { mutableStateOf(false) }

    val player = remember(isActive) {
        if (!isActive) null
        else ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            repeatMode = Player.REPEAT_MODE_ONE
            volume = 0f
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(player) {
        onDispose { player?.release() }
    }

    LaunchedEffect(player, muted) { player?.volume = if (muted) 0f else 1f }

    LaunchedEffect(player) {
        val p = player ?: return@LaunchedEffect
        while (true) {
            playing = p.isPlaying
            val total = if (p.duration > 0) p.duration else durationMs
            progress = if (total > 0) (p.currentPosition.toFloat() / total).coerceIn(0f, 1f) else 0f
            kotlinx.coroutines.delay(90)
        }
    }

    Column(
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            /* --- кольцо прогресса --- */
            Canvas(Modifier.fillMaxSize()) {
                val stroke = 3.5.dp.toPx()
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.20f),
                    radius = this.size.minDimension / 2 - stroke / 2,
                    style = Stroke(stroke)
                )
                if (progress > 0f) {
                    drawArc(
                        brush = Brush.sweepGradient(VibeAuroraGradient),
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(stroke, cap = StrokeCap.Round)
                    )
                }
            }

            /* --- содержимое круга --- */
            Box(
                Modifier
                    .size(size - 12.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    // БЫЛО: .clickable { ... }.pointerInputLongPress { ... }
                    // Два pointer-input узла на одном элементе конкурируют:
                    // clickable стоял первым и забирал жест себе, поэтому долгий
                    // тап (включение звука) почти никогда не срабатывал.
                    // Теперь оба жеста в одном детекторе.
                    .pointerInput(isActive, player) {
                        detectTapGestures(
                            onTap = {
                                if (!isActive) onActivate()
                                else player?.let { if (it.isPlaying) it.pause() else it.play() }
                            },
                            onLongPress = { muted = !muted }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (player != null) {
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                useController = false
                                // FILL, а не FIT: иначе в круге появляются
                                // чёрные поля от 16:9 исходника.
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                setShutterBackgroundColor(android.graphics.Color.BLACK)
                            }
                        },
                        update = { it.player = player },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    if (thumbUrl != null) {
                        AsyncImage(
                            model = thumbUrl,
                            contentDescription = "Видеосообщение",
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(Modifier.fillMaxSize().background(Brush.linearGradient(VibeAuroraSoft)))
                    }
                    Box(
                        Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.45f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.PlayArrow, null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                }
            }

            /* --- индикатор звука --- */
            if (isActive) {
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.55f))
                        .clickable { muted = !muted },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (muted) Icons.Rounded.VolumeOff else Icons.Rounded.VolumeUp,
                        contentDescription = if (muted) "Включить звук" else "Выключить звук",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(VibeSpacing.xs))

        Text(
            text = formatDuration(if (isActive && progress > 0) (durationMs * (1 - progress)).toLong() else durationMs),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(RoundedCornerShape(VibeRadius.pill))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                .padding(horizontal = VibeSpacing.sm, vertical = 2.dp)
        )
    }
}

private fun formatDuration(ms: Long): String {
    val total = (ms / 1000).coerceAtLeast(0)
    return "%d:%02d".format(total / 60, total % 60)
}