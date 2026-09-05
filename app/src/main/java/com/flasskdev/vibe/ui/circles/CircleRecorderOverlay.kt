package com.flasskdev.vibe.ui.circles

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cameraswitch
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FiberManualRecord
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.flasskdev.vibe.ui.theme.VibeAuroraGradient
import com.flasskdev.vibe.ui.theme.VibeSpacing
import com.flasskdev.vibe.ui.theme.VibeStrings
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File

/**
 * ============================================================================
 *  ПУНКТ 2 — ОВЕРЛЕЙ ЗАПИСИ КРУЖКА
 * ============================================================================
 *
 *  ЧТО БЫЛО СЛОМАНО
 *   1. Оверлей вообще ниоткуда не вызывался: ни ChatScreen, ни ChatInputBar
 *      про кружки не знали. Записать их было нельзя даже теоретически.
 *   2. Разрешения не запрашивались. Без CAMERA камера просто «не работала».
 *   3. Кнопка смены камеры была пустой заглушкой: switchCamera() объявлен
 *      suspend, а из onClick его вызывать было нечем — scope отсутствовал.
 *   4. На кнопке записи стояла иконка Cameraswitch — та же, что у смены
 *      камеры, поэтому было непонятно, куда вообще нажимать.
 *   5. Два конкурирующих pointerInput (detectDragGestures + detectTapGestures)
 *      на одной кнопке: оба перехватывали нажатие, из-за чего onPressStart
 *      мог прийти дважды, а onPressEnd — ни разу.
 *
 *  КАК СТАЛО
 *   - Полноэкранный Dialog: оверлей живёт внутри ChatInputBar, но рисуется
 *     поверх всего, включая клавиатуру и таббар. ChatScreen трогать не нужно.
 *   - Два сценария записи в одном жесте, как в Telegram, но надёжнее:
 *       • короткий тап по большой кнопке — старт, повторный тап — стоп и отправка;
 *       • удержание — запись, отпустил — отправка, увёл влево — отмена.
 *   - Один pointerInput с ручным awaitEachGesture, поэтому события больше
 *     не задваиваются.
 *   - Разрешения спрашиваются на входе, отказ обрабатывается явно.
 */
@Composable
fun CircleRecorderOverlay(
    visible: Boolean,
    strings: VibeStrings,
    onSend: (file: File, durationMs: Long) -> Unit,
    onError: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val recorder = remember(lifecycleOwner) { CircleRecorder(context, lifecycleOwner) }
    val state by recorder.state.collectAsState()

    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var hasPermission by remember { mutableStateOf(CircleRecorder.hasPermissions(context)) }
    var permissionAsked by remember { mutableStateOf(false) }
    var locked by remember { mutableStateOf(false) }
    var dragX by remember { mutableFloatStateOf(0f) }
    var dragY by remember { mutableFloatStateOf(0f) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = CircleRecorder.REQUIRED_PERMISSIONS.all { result[it] == true }
        hasPermission = granted
        if (!granted) {
            onError(strings.circlePermissionRequired)
            onDismiss()
        }
    }

    // Спрашиваем разрешения ровно один раз на открытие оверлея.
    LaunchedEffect(Unit) {
        if (!hasPermission && !permissionAsked) {
            permissionAsked = true
            permissionLauncher.launch(CircleRecorder.REQUIRED_PERMISSIONS)
        }
    }

    DisposableEffect(recorder) { onDispose { recorder.release() } }

    LaunchedEffect(previewView, hasPermission) {
        if (hasPermission) previewView?.let { recorder.bind(it) }
    }

    LaunchedEffect(state) {
        when (val s = state) {
            is CircleRecorder.State.Finished -> {
                onSend(s.file, s.durationMs)
                onDismiss()
            }
            is CircleRecorder.State.Cancelled -> onDismiss()
            is CircleRecorder.State.Failed -> {
                // NO_PERMISSION — это не ошибка камеры, а нехватка прав:
                // диалог уже показан, второй тост тут был бы лишним шумом.
                if (s.reason != REASON_NO_PERMISSION) {
                    onError(s.reason)
                    onDismiss()
                }
            }
            else -> Unit
        }
    }

    val recordingState = state as? CircleRecorder.State.Recording
    val elapsed = recordingState?.elapsedMs ?: 0L
    val amplitude = recordingState?.amplitude ?: 0f
    val progress = (elapsed.toFloat() / CircleRecorder.MAX_DURATION_MS).coerceIn(0f, 1f)
    val isRecording = recordingState != null
    val isReady = state is CircleRecorder.State.Ready || isRecording
    val aboutToCancel = isRecording && !locked && dragX < -60f

    Dialog(
        onDismissRequest = {
            recorder.cancel()
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(horizontal = VibeSpacing.lg)
            ) {

                /* ---------- превью в круге с кольцом прогресса ---------- */
                Box(contentAlignment = Alignment.Center) {
                    // Кольцо реагирует на громкость: сразу видно, что микрофон слышит.
                    val ringPad by animateFloatAsState(
                        targetValue = 6f + amplitude * 10f,
                        animationSpec = spring(dampingRatio = 0.5f),
                        label = "ring"
                    )
                    val cancelShift by animateFloatAsState(
                        targetValue = if (aboutToCancel) 0.7f else 0.35f,
                        animationSpec = spring(),
                        label = "cancelShift"
                    )

                    Box(
                        Modifier
                            .size(268.dp)
                            .graphicsLayer {
                                translationX = dragX * cancelShift
                                translationY = dragY * 0.25f
                                alpha = if (aboutToCancel) 0.55f else 1f
                            }
                    ) {
                        Canvas(Modifier.fillMaxSize()) {
                            val stroke = 5.dp.toPx()
                            drawCircle(
                                color = Color.White.copy(alpha = 0.18f),
                                radius = size.minDimension / 2 - stroke / 2,
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

                        AndroidView(
                            factory = { ctx ->
                                PreviewView(ctx).apply {
                                    scaleType = PreviewView.ScaleType.FILL_CENTER
                                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                                }.also { previewView = it }
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size((240 + ringPad).dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(Modifier.height(VibeSpacing.xl))

                Text(
                    text = formatElapsed(elapsed),
                    color = if (isRecording) Color.White else Color.White.copy(alpha = 0.45f),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
                )

                Spacer(Modifier.height(VibeSpacing.sm))

                Text(
                    text = when {
                        !hasPermission -> strings.circlePermissionRequired
                        !isReady -> strings.circleCameraPreparing
                        aboutToCancel -> strings.circleReleaseToCancel
                        locked && isRecording -> strings.circleLockedHint
                        isRecording -> strings.circleRecordingHint
                        else -> strings.circleHoldOrTapHint
                    },
                    color = if (aboutToCancel) MaterialTheme.colorScheme.error
                    else Color.White.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(VibeSpacing.xxl))

                /* ---------- органы управления ---------- */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            recorder.cancel()
                            onDismiss()
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = strings.circleCancel,
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(Modifier.width(VibeSpacing.xxl))

                    RecordButton(
                        isRecording = isRecording,
                        locked = locked,
                        enabled = isReady,
                        contentDescription = when {
                            locked && isRecording -> strings.circleSend
                            isRecording -> strings.circleTapToStop
                            else -> strings.circleHoldOrTapHint
                        },
                        onTap = {
                            // Короткий тап: старт, если стоим, стоп и отправка, если пишем.
                            if (isRecording) recorder.stop() else recorder.start()
                            locked = isRecording
                            dragX = 0f
                            dragY = 0f
                        },
                        onHoldStart = {
                            locked = false
                            dragX = 0f
                            dragY = 0f
                            if (!isRecording) recorder.start()
                        },
                        onHoldDrag = { dx, dy ->
                            dragX += dx
                            dragY += dy
                            // Свайп вверх — «залочить» запись, чтобы не держать палец.
                            if (dragY < -with(density) { 90.dp.toPx() }) {
                                locked = true
                                dragX = 0f
                                dragY = 0f
                            }
                        },
                        onHoldEnd = {
                            if (!locked) {
                                if (dragX < -with(density) { 96.dp.toPx() }) recorder.cancel()
                                else recorder.stop()
                            }
                            dragX = 0f
                            dragY = 0f
                        }
                    )

                    Spacer(Modifier.width(VibeSpacing.xxl))

                    IconButton(
                        // Раньше здесь была пустая лямбда: switchCamera() — suspend,
                        // и его просто не из чего было вызвать. Теперь есть scope.
                        onClick = {
                            val pv = previewView ?: return@IconButton
                            scope.launch { recorder.switchCamera(pv) }
                        },
                        enabled = !isRecording && isReady
                    ) {
                        Icon(
                            Icons.Rounded.Cameraswitch,
                            contentDescription = strings.circleSwitchCamera,
                            tint = Color.White.copy(alpha = if (isRecording || !isReady) 0.3f else 0.8f)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isRecording,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(VibeSpacing.lg))
                        Text(
                            text = strings.circleMaxDurationHint,
                            color = Color.White.copy(alpha = 0.35f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordButton(
    isRecording: Boolean,
    locked: Boolean,
    enabled: Boolean,
    contentDescription: String,
    onTap: () -> Unit,
    onHoldStart: () -> Unit,
    onHoldDrag: (Float, Float) -> Unit,
    onHoldEnd: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = when {
            !enabled -> 0.9f
            isRecording -> 1.16f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "recScale"
    )

    Box(
        Modifier
            .size(84.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (enabled) 1f else 0.4f
            }
            .clip(CircleShape)
            .background(Brush.linearGradient(VibeAuroraGradient))
            // ОДИН обработчик вместо двух конкурирующих: сначала ждём, не
            // окажется ли жест длинным удержанием, и только потом решаем,
            // это тап или hold-to-record.
            .pointerInput(enabled, isRecording, locked) {
                if (!enabled) return@pointerInput
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    down.consume()

                    // Фаза 1: тап или удержание? Ждём ровно longPressTimeout.
                    val releasedBeforeLongPress =
                        withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                            while (true) {
                                val event = awaitPointerEvent()
                                val pointer = event.changes.firstOrNull { it.id == down.id }
                                    ?: return@withTimeoutOrNull true
                                if (!pointer.pressed) {
                                    pointer.consume()
                                    return@withTimeoutOrNull true
                                }
                                pointer.consume()
                            }
                        }

                    if (releasedBeforeLongPress != null) {
                        onTap()
                        return@awaitEachGesture
                    }

                    // Фаза 2: удержание — пишем, пока палец на кнопке.
                    onHoldStart()
                    while (true) {
                        val event = awaitPointerEvent()
                        val pointer = event.changes.firstOrNull { it.id == down.id } ?: break
                        if (!pointer.pressed) {
                            pointer.consume()
                            break
                        }
                        val change = pointer.positionChange()
                        onHoldDrag(change.x, change.y)
                        pointer.consume()
                    }
                    onHoldEnd()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when {
                locked && isRecording -> Icons.Rounded.Send
                isRecording -> Icons.Rounded.Stop
                else -> Icons.Rounded.FiberManualRecord
            },
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

private fun formatElapsed(ms: Long): String {
    val total = ms / 1000
    return "%d:%02d".format(total / 60, total % 60)
}