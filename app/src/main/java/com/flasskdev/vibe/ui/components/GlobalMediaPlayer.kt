package com.flasskdev.vibe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.expandVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.flasskdev.vibe.ui.theme.LocalVibeStrings
import com.flasskdev.vibe.ui.theme.VibePrimary
import com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo
import com.flasskdev.vibe.ui.viewmodels.GlobalAudioPlayerViewModel
import com.flasskdev.vibe.ui.viewmodels.VibeRepeatMode
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import java.util.Locale
import kotlin.math.roundToInt

/* ==========================================================================
 *  МИНИ-ПЛЕЕР
 * ========================================================================== */

@Composable
fun GlobalMiniPlayer(
    viewModel: GlobalAudioPlayerViewModel,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    isInline: Boolean = false,
    onExpand: () -> Unit
) {
    val isVisible by viewModel.isPlayerVisible.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isBuffering by viewModel.isBuffering.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val position by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val hasNext by viewModel.hasNext.collectAsState()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(120, easing = LinearEasing),
        label = "mini-progress"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(280)) +
                expandVertically(animationSpec = tween(280)) + fadeIn(tween(200)),
        exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(240)) +
                shrinkVertically(animationSpec = tween(240)) + fadeOut(tween(160)),
        modifier = modifier
    ) {
        val container = if (isInline) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.94f))
        }

        Column(
            modifier = container.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onExpand() }
        ) {
            if (!isInline) {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Обложка вместо голой кнопки — сразу видно, что играет.
                TrackArtwork(
                    url = currentTrack?.avatarUrl,
                    size = 38.dp,
                    corner = 10.dp,
                    showEqualizer = isPlaying
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentTrack?.title ?: "Audio",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                    )
                    Text(
                        text = currentTrack?.subtitle
                            ?: "${formatTime(position)} / ${formatTime(duration)}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Play/Pause с кольцом буферизации
                Box(
                    modifier = Modifier.size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isBuffering) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            strokeWidth = 2.dp,
                            color = VibePrimary.copy(alpha = 0.7f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(VibePrimary)
                            .clickable { viewModel.togglePlayPause() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                // Кнопка «дальше» показывается, только когда есть куда идти.
                if (hasNext) {
                    IconButton(onClick = { viewModel.playNext() }, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }

                IconButton(onClick = { viewModel.closePlayer() }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            // Тонкая полоса прогресса со скруглением
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isInline) 0.dp else 10.dp)
                    .padding(bottom = if (isInline) 0.dp else 6.dp)
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(2.dp))
                        .background(VibePrimary)
                )
            }
        }
    }
}

@Composable
fun GlobalMediaPlayer(
    viewModel: GlobalAudioPlayerViewModel,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    onExpand: () -> Unit
) = GlobalMiniPlayer(viewModel, hazeState, modifier, onExpand = onExpand)

/* ==========================================================================
 *  РАЗВЁРНУТЫЙ ПЛЕЕР
 * ========================================================================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedAudioPlayerSheet(
    viewModel: GlobalAudioPlayerViewModel,
    hazeState: HazeState,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isBuffering by viewModel.isBuffering.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val buffered by viewModel.bufferedProgress.collectAsState()
    val position by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val playlist by viewModel.playlist.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    val shuffle by viewModel.shuffleEnabled.collectAsState()
    val speed by viewModel.speed.collectAsState()
    val strings = LocalVibeStrings.current

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val filteredPlaylist = remember(playlist, searchQuery) {
        if (searchQuery.isBlank()) playlist
        else playlist.filter {
            it.title.contains(searchQuery, true) || it.subtitle?.contains(searchQuery, true) == true
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(currentTrack, isSearchActive) {
        if (currentTrack != null && !isSearchActive) {
            playlist.indexOfFirst { it.id == currentTrack?.id }
                .takeIf { it >= 0 }
                ?.let { listState.animateScrollToItem(it) }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        dragHandle = null
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .hazeChild(state = hazeState)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.94f))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // ---------- ручка ----------
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .width(38.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f))
                    )
                }

                Spacer(Modifier.height(14.dp))

                // ---------- заголовок ----------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.playerPlaylist,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${playlist.size} " + pluralTracks(playlist.size),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                    IconButton(onClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) searchQuery = ""
                    }) {
                        Icon(
                            imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                AnimatedVisibility(visible = isSearchActive) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            cursorBrush = SolidColor(VibePrimary),
                            modifier = Modifier.weight(1f),
                            decorationBox = { inner ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = strings.playerSearchTracks,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                        fontSize = 14.sp
                                    )
                                }
                                inner()
                            }
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                // ---------- плейлист ----------
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(filteredPlaylist, key = { it.id }) { track ->
                        PlaylistRow(
                            track = track,
                            isCurrent = track.id == currentTrack?.id,
                            isPlaying = isPlaying,
                            onClick = { viewModel.playAudio(track, playlist) }
                        )
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                )

                // ---------- блок управления ----------
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 14.dp, bottom = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TrackArtwork(
                            url = currentTrack?.avatarUrl,
                            size = 52.dp,
                            corner = 14.dp,
                            showEqualizer = false
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentTrack?.title ?: "Audio",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                            )
                            currentTrack?.subtitle?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        // Чип скорости
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (speed == 1f) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                    else VibePrimary.copy(alpha = 0.18f)
                                )
                                .clickable { viewModel.cycleSpeed() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = formatSpeed(speed),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (speed == 1f)
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                else VibePrimary
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // ---------- скраббер ----------
                    VibeScrubber(
                        progress = progress,
                        buffered = buffered,
                        onSeek = { viewModel.seekTo(it) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTime(position),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Text(
                            // Оставшееся время читается удобнее, чем общая длительность.
                            text = if (duration > 0) "-${formatTime(duration - position)}" else "--:--",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // ---------- кнопки ----------
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ToggleIcon(
                            icon = Icons.Default.Shuffle,
                            active = shuffle,
                            description = "Перемешать",
                            onClick = { viewModel.toggleShuffle() }
                        )

                        IconButton(onClick = { viewModel.skipBackward(10_000L) }) {
                            Icon(
                                Icons.Default.Replay10, "Назад 10 секунд",
                                modifier = Modifier.size(26.dp),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                            )
                        }

                        IconButton(onClick = { viewModel.playPrevious() }) {
                            Icon(
                                Icons.Default.SkipPrevious, "Previous",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Box(
                            modifier = Modifier.size(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isBuffering) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(60.dp),
                                    strokeWidth = 2.5.dp,
                                    color = VibePrimary.copy(alpha = 0.55f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(VibePrimary)
                                    .clickable { viewModel.togglePlayPause() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        IconButton(onClick = { viewModel.playNext() }) {
                            Icon(
                                Icons.Default.SkipNext, "Next",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(onClick = { viewModel.skipForward(10_000L) }) {
                            Icon(
                                Icons.Default.Forward10, "Вперёд 10 секунд",
                                modifier = Modifier.size(26.dp),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                            )
                        }

                        ToggleIcon(
                            icon = if (repeatMode == VibeRepeatMode.ONE) Icons.Default.RepeatOne
                            else Icons.Default.Repeat,
                            active = repeatMode != VibeRepeatMode.OFF,
                            description = "Повтор",
                            onClick = { viewModel.cycleRepeatMode() }
                        )
                    }
                }
            }
        }
    }
}

/* ==========================================================================
 *  ПЕРЕИСПОЛЬЗУЕМЫЕ ЭЛЕМЕНТЫ
 * ========================================================================== */

/**
 * Скраббер, у которого ползунок реально попадает под палец.
 *
 * В старой версии было две конкурирующих pointerInput-ветки, а thumb смещался
 * на `width * progress - 6.dp` и уезжал за правый край. Здесь один жест,
 * позиция абсолютная, ползунок корректно ограничен шириной дорожки.
 */
@Composable
fun VibeScrubber(
    progress: Float,
    buffered: Float,
    modifier: Modifier = Modifier,
    onSeek: (Float) -> Unit
) {
    val thumbSize = 13.dp
    var widthPx by remember { mutableIntStateOf(1) }
    var thumbPx by remember { mutableIntStateOf(0) }
    var dragging by remember { mutableStateOf(false) }
    var dragFraction by remember { mutableFloatStateOf(0f) }

    val shown = if (dragging) dragFraction else progress.coerceIn(0f, 1f)
    val thumbScale by animateFloatAsState(if (dragging) 1.35f else 1f, label = "thumb-scale")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .onSizeChanged { widthPx = it.width.coerceAtLeast(1) }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    dragging = true
                    dragFraction = (down.position.x / size.width).coerceIn(0f, 1f)
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                        if (!change.pressed) break
                        dragFraction = (change.position.x / size.width).coerceIn(0f, 1f)
                        change.consume()
                    }
                    onSeek(dragFraction)
                    dragging = false
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // фон
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
        )
        // буфер
        Box(
            modifier = Modifier
                .fillMaxWidth(buffered.coerceIn(0f, 1f))
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f))
        )
        // проигранное
        Box(
            modifier = Modifier
                .fillMaxWidth(shown.coerceIn(0.0001f, 1f))
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(VibePrimary)
        )
        // ползунок
        Box(
            modifier = Modifier
                .onSizeChanged { thumbPx = it.width }
                .offset {
                    IntOffset(((widthPx - thumbPx).coerceAtLeast(0) * shown).roundToInt(), 0)
                }
                .size(thumbSize * thumbScale)
                .clip(CircleShape)
                .background(VibePrimary)
        )
    }
}

@Composable
private fun PlaylistRow(
    track: AudioTrackInfo,
    isCurrent: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val bg by animateColorAsState(
        if (isCurrent) VibePrimary.copy(alpha = 0.13f) else Color.Transparent,
        label = "row-bg"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(
                    if (isCurrent) VibePrimary.copy(alpha = 0.22f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!track.avatarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(track.avatarUrl).crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            when {
                isCurrent && isPlaying -> EqualizerBars(color = VibePrimary)
                isCurrent -> Icon(
                    Icons.Default.Pause, null, tint = VibePrimary,
                    modifier = Modifier.size(18.dp)
                )
                track.avatarUrl.isNullOrEmpty() -> Icon(
                    Icons.Default.MusicNote, null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                color = if (isCurrent) VibePrimary else MaterialTheme.colorScheme.onBackground,
                fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            track.subtitle?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        track.durationMs?.takeIf { it > 0 }?.let {
            Spacer(Modifier.width(8.dp))
            Text(
                text = formatTime(it),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun TrackArtwork(
    url: String?,
    size: Dp,
    corner: Dp,
    showEqualizer: Boolean
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .background(VibePrimary.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center
    ) {
        if (!url.isNullOrEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url).crossfade(true).build(),
                contentDescription = "Обложка трека",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            if (showEqualizer) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) { EqualizerBars(color = Color.White) }
            }
        } else {
            if (showEqualizer) EqualizerBars(color = VibePrimary)
            else Icon(
                Icons.Default.MusicNote, null,
                tint = VibePrimary,
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}

/** Живой эквалайзер: три столбика, сразу видно, что трек играет. */
@Composable
private fun EqualizerBars(color: Color) {
    val transition = rememberInfiniteTransition(label = "eq")
    val heights = listOf(0, 180, 360).map { delayMs ->
        transition.animateFloat(
            initialValue = 0.30f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(520, delayMs, FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "eq-$delayMs"
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        modifier = Modifier.height(16.dp)
    ) {
        heights.forEach { anim ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight(anim.value)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun ToggleIcon(
    icon: ImageVector,
    active: Boolean,
    description: String,
    onClick: () -> Unit
) {
    val tint by animateColorAsState(
        if (active) VibePrimary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
        label = "toggle-tint"
    )
    IconButton(onClick = onClick) {
        Icon(icon, description, tint = tint, modifier = Modifier.size(21.dp))
    }
}

/* ==========================================================================
 *  ХЕЛПЕРЫ
 * ========================================================================== */

private fun formatTime(ms: Long): String {
    if (ms <= 0L) return "0:00"
    val total = ms / 1000
    val h = total / 3600
    val m = (total % 3600) / 60
    val s = total % 60
    return if (h > 0) String.format(Locale.US, "%d:%02d:%02d", h, m, s)
    else String.format(Locale.US, "%d:%02d", m, s)
}

private fun formatSpeed(speed: Float): String =
    if (speed == speed.toInt().toFloat()) "${speed.toInt()}x"
    else "${speed.toString().trimEnd('0').trimEnd('.')}x"

private fun pluralTracks(count: Int): String = when {
    count % 10 == 1 && count % 100 != 11 -> "трек"
    count % 10 in 2..4 && count % 100 !in 12..14 -> "трека"
    else -> "треков"
}