package com.flasskdev.vibe.ui.components

import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.flasskdev.vibe.data.local.MessageEntity
import com.flasskdev.vibe.ui.theme.LocalVibeStrings
import com.flasskdev.vibe.ui.theme.VibePrimary
import com.flasskdev.vibe.utils.AttachmentType
import com.flasskdev.vibe.utils.AttachmentUtils
import com.flasskdev.vibe.utils.DownloadHelper
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.layout.statusBarsPadding


private const val PAGE_SIZE = 20

data class ProfileAttachmentItem(
    val url: String,
    val message: MessageEntity,
    val type: AttachmentType
)

/**
 * Profile media sections: Photos&Video, Files, Music, Voice.
 * - Hides empty sections completely
 * - Shows item count when section is opened
 * - Lazy loading (pagination)
 * - Search for Music and Files
 * - Navigation arrow to jump to source message
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMediaTabs(
    messages: List<MessageEntity>,
    modifier: Modifier = Modifier,
    partnerAvatarUrl: String? = null,
    onNavigateToMessage: ((Int, Int) -> Unit)? = null // (messageId, partnerId) -> navigate to chat
) {
    val strings = LocalVibeStrings.current

    // Categorize all attachments
    val allItems = remember(messages) {
        messages.flatMap { msg ->
            msg.attachments?.map { url ->
                ProfileAttachmentItem(
                    url = url,
                    message = msg,
                    type = AttachmentUtils.getType(url, msg.content)
                )
            } ?: emptyList()
        }
    }

    val mediaItems = remember(allItems) {
        allItems.filter { it.type == AttachmentType.IMAGE || it.type == AttachmentType.VIDEO }
    }
    val fileItems = remember(allItems) {
        allItems.filter { it.type == AttachmentType.FILE }
    }
    val musicItems = remember(allItems) {
        allItems.filter { it.type == AttachmentType.AUDIO }
    }
    val voiceItems = remember(allItems) {
        allItems.filter { it.type == AttachmentType.VOICE || it.type == AttachmentType.VIDEO_MESSAGE }
    }

    // Build visible tabs — hide empty sections
    data class SectionInfo(
        val key: String,
        val title: String,
        val items: List<ProfileAttachmentItem>,
        val hasSearch: Boolean
    )

    val sections = remember(mediaItems, fileItems, musicItems, voiceItems) {
        listOfNotNull(
            if (mediaItems.isNotEmpty()) SectionInfo("media", strings.sectionPhotosVideos, mediaItems, false) else null,
            if (fileItems.isNotEmpty()) SectionInfo("files", strings.sectionFiles, fileItems, true) else null,
            if (musicItems.isNotEmpty()) SectionInfo("music", strings.sectionMusic, musicItems, true) else null,
            if (voiceItems.isNotEmpty()) SectionInfo("voice", strings.sectionVoice, voiceItems, false) else null
        )
    }

    if (sections.isEmpty()) return

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    // Reset tab index if sections changed
    LaunchedEffect(sections.size) {
        if (selectedTabIndex >= sections.size) selectedTabIndex = 0
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = VibePrimary,
            edgePadding = 16.dp,
            divider = { HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)) }
        ) {
            sections.forEachIndexed { index, section ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = "${section.title} (${section.items.size})",
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTabIndex == index) VibePrimary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        if (selectedTabIndex in sections.indices) {
            val currentSection = sections[selectedTabIndex]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(top = 8.dp)
            ) {
                when (currentSection.key) {
                    "media" -> ProfileMediaGrid(
                        items = currentSection.items,
                        onNavigateToMessage = onNavigateToMessage
                    )
                    "files" -> ProfileSearchableList(
                        items = currentSection.items,
                        hasSearch = true,
                        searchPlaceholder = strings.profileSearchFiles,
                        onNavigateToMessage = onNavigateToMessage,
                        renderItem = { item -> FileListItem(item) }
                    )
                    "music" -> ProfileSearchableList(
                        items = currentSection.items,
                        hasSearch = true,
                        searchPlaceholder = strings.playerSearchTracks,
                        onNavigateToMessage = onNavigateToMessage,
                        renderItem = { item -> MusicListItem(item, partnerAvatarUrl) }
                    )
                    "voice" -> ProfileSearchableList(
                        items = currentSection.items,
                        hasSearch = false,
                        searchPlaceholder = "",
                        onNavigateToMessage = onNavigateToMessage,
                        renderItem = { item -> VoiceListItem(item, partnerAvatarUrl) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileMediaGrid(
    items: List<ProfileAttachmentItem>,
    onNavigateToMessage: ((Int, Int) -> Unit)? = null
) {
    val context = LocalContext.current
    var visibleCount by remember { mutableIntStateOf(PAGE_SIZE) }
    val displayItems = items.take(visibleCount)
    val gridState = rememberLazyGridState()

    // Fullscreen viewer state
    var viewerIndex by remember { mutableIntStateOf(-1) }

    // Lazy loading trigger
    LaunchedEffect(gridState.firstVisibleItemIndex, displayItems.size) {
        if (gridState.firstVisibleItemIndex + 12 >= displayItems.size && visibleCount < items.size) {
            visibleCount = (visibleCount + PAGE_SIZE).coerceAtMost(items.size)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(displayItems.size, key = { displayItems[it].url + it }) { index ->
                val item = displayItems[index]
                val url = item.url
                val isLocal = url.startsWith("/") || url.startsWith("content://") || url.contains("cacheDir")
                val resolvedModel: Any = if (isLocal) {
                    File(url)
                } else if (url.startsWith("http")) {
                    url
                } else {
                    "https://flasskdev.alwaysdata.net/api/upload/file/$url"
                }
                val isVideo = item.type == AttachmentType.VIDEO

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            // Open fullscreen viewer instead of navigating to chat
                            viewerIndex = index
                        }
                ) {
                    var isError by remember { mutableStateOf(false) }
                    var isLoading by remember { mutableStateOf(true) }

                    AsyncImage(
                        model = if (isVideo) {
                            ImageRequest.Builder(context)
                                .data(resolvedModel)
                                .videoFrameMillis(1000)
                                .crossfade(true)
                                .memoryCacheKey("profile_thumb_${url}")
                                .diskCacheKey("profile_thumb_${url}")
                                .size(300)
                                .build()
                        } else {
                            ImageRequest.Builder(context)
                                .data(resolvedModel)
                                .crossfade(true)
                                .memoryCacheKey("profile_thumb_${url}")
                                .diskCacheKey("profile_thumb_${url}")
                                .size(300)
                                .build()
                        },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        onSuccess = { isLoading = false; isError = false },
                        onError = { isLoading = false; isError = true },
                        onLoading = { isLoading = true }
                    )

                    // Loading shimmer placeholder
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = VibePrimary,
                                strokeWidth = 2.dp
                            )
                        }
                    }

                    // Error state with retry
                    if (isError) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.InsertDriveFile,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    if (isVideo && !isLoading && !isError) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Video",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(24.dp)
                                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }

        // Fullscreen photo viewer overlay
        if (viewerIndex >= 0 && viewerIndex < displayItems.size) {
            FullScreenMediaViewer(
                items = displayItems,
                initialIndex = viewerIndex,
                onDismiss = { viewerIndex = -1 }
            )
        }
    }
}

/**
 * Fullscreen media viewer with zoom, pan, and navigation.
 */
@Composable
private fun FullScreenMediaViewer(
    items: List<ProfileAttachmentItem>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var currentIndex by remember { mutableIntStateOf(initialIndex) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Reset zoom when switching images
    LaunchedEffect(currentIndex) {
        scale = 1f
        offsetX = 0f
        offsetY = 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) {
                if (scale <= 1f) onDismiss()
            }
    ) {
        val item = items.getOrNull(currentIndex)
        if (item != null) {
            val url = item.url
            val isLocal = url.startsWith("/") || url.startsWith("content://") || url.contains("cacheDir")
            val resolvedModel: Any = if (isLocal) File(url) else if (url.startsWith("http")) url else "https://flasskdev.alwaysdata.net/api/upload/file/$url"
            val isVideo = item.type == AttachmentType.VIDEO

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(currentIndex) {
                        awaitEachGesture {
                            val firstDown = awaitFirstDown(requireUnconsumed = false)
                            do {
                                val event = awaitPointerEvent()
                                val zoomChange = event.calculateZoom()
                                val panChange = event.calculatePan()

                                scale = (scale * zoomChange).coerceIn(0.5f, 5f)
                                if (scale > 1f) {
                                    offsetX += panChange.x
                                    offsetY += panChange.y
                                } else {
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                                event.changes.forEach { it.consume() }
                            } while (event.changes.any { it.pressed })
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = if (isVideo) {
                        ImageRequest.Builder(context)
                            .data(resolvedModel)
                            .videoFrameMillis(1000)
                            .crossfade(true)
                            .build()
                    } else {
                        ImageRequest.Builder(context)
                            .data(resolvedModel)
                            .crossfade(true)
                            .build()
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offsetX
                            translationY = offsetY
                        }
                )
            }
        }

        // Top bar with close and counter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
            Text(
                text = "${currentIndex + 1} / ${items.size}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(48.dp)) // Balance
        }

        // Navigation arrows
        if (items.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentIndex > 0) {
                    IconButton(
                        onClick = { currentIndex-- },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier.graphicsLayer { rotationZ = 180f }
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }

                if (currentIndex < items.size - 1) {
                    IconButton(
                        onClick = { currentIndex++ },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileSearchableList(
    items: List<ProfileAttachmentItem>,
    hasSearch: Boolean,
    searchPlaceholder: String,
    onNavigateToMessage: ((Int, Int) -> Unit)? = null,
    renderItem: @Composable (ProfileAttachmentItem) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var visibleCount by remember { mutableIntStateOf(PAGE_SIZE) }
    val listState = rememberLazyListState()

    val filteredItems = remember(items, searchQuery) {
        if (searchQuery.isBlank()) items
        else items.filter {
            val filename = AttachmentUtils.getFilename(it.url)
            filename.contains(searchQuery, ignoreCase = true)
        }
    }

    val displayItems = filteredItems.take(visibleCount)

    // Lazy loading trigger
    LaunchedEffect(listState.firstVisibleItemIndex, displayItems.size) {
        if (listState.firstVisibleItemIndex + 10 >= displayItems.size && visibleCount < filteredItems.size) {
            visibleCount = (visibleCount + PAGE_SIZE).coerceAtMost(filteredItems.size)
        }
    }

    // Reset pagination on search change
    LaunchedEffect(searchQuery) {
        visibleCount = PAGE_SIZE
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        if (hasSearch) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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
                                    text = searchPlaceholder,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                    fontSize = 14.sp
                                )
                            }
                            inner()
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(displayItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable {
                            val myId = try { com.flasskdev.vibe.data.UserPreferences(context).userId } catch (_: Exception) { 0 }
                            val partnerId = if (item.message.senderId == myId) item.message.receiverId else item.message.senderId
                            onNavigateToMessage?.invoke(item.message.id, partnerId)
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        renderItem(item)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Go to message",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FileListItem(item: ProfileAttachmentItem) {
    val context = LocalContext.current
    val url = item.url
    val isLocal = url.startsWith("/") || url.startsWith("content://") || url.contains("cacheDir")
    val filename = if (isLocal) File(url).name else url.substringAfterLast("/")
    val downloadUrl = if (isLocal) url else if (url.startsWith("http")) url else "https://flasskdev.alwaysdata.net/api/upload/file/$url"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(VibePrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                filename,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
            Text(
                formatTimestamp(item.message.timestamp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                fontSize = 11.sp
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = {
                if (!isLocal) {
                    DownloadHelper.downloadFile(context, downloadUrl, filename)
                }
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(Icons.Default.Download, contentDescription = "Download", tint = VibePrimary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun MusicListItem(item: ProfileAttachmentItem, avatarUrl: String? = null) {
    val url = item.url
    val isLocal = url.startsWith("/") || url.startsWith("content://") || url.contains("cacheDir")
    val fallbackFilename = if (isLocal) File(url).nameWithoutExtension else url.substringAfterLast("/").substringBeforeLast(".")
    val audioUrl = if (isLocal) url else if (url.startsWith("http")) url else "https://flasskdev.alwaysdata.net/api/upload/file/$url"

    var audioMeta by remember(audioUrl) { mutableStateOf<com.flasskdev.vibe.utils.AudioMetadata?>(null) }
    LaunchedEffect(audioUrl) {
        audioMeta = com.flasskdev.vibe.utils.AudioMetadataHelper.getMetadata(audioUrl)
    }

    val displayTitle = audioMeta?.displayTitle ?: fallbackFilename
    val displayArtist = audioMeta?.displayArtist ?: "Unknown Artist"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            val coverArtBytes = audioMeta?.coverArt
            if (coverArtBytes != null) {
                val bitmap = remember(coverArtBytes) {
                    android.graphics.BitmapFactory.decodeByteArray(coverArtBytes, 0, coverArtBytes.size)
                }
                if (bitmap != null) {
                    androidx.compose.foundation.Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                }
            } else if (!avatarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(avatarUrl).crossfade(true).build(),
                    contentDescription = "Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(VibePrimary.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.MusicNote, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(20.dp))
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                displayTitle,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    displayArtist,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(3.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    formatTimestamp(item.message.timestamp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun VoiceListItem(item: ProfileAttachmentItem, avatarUrl: String? = null) {
    val isVideoMsg = item.type == AttachmentType.VIDEO_MESSAGE

    val durationText = remember(item.message.content) {
        val prefix = if (isVideoMsg) "video_message:" else "duration:"
        val ms = item.message.content.substringAfter(prefix).toLongOrNull() ?: 0L
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        String.format("%d:%02d", minutes, seconds)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (!avatarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(avatarUrl).crossfade(true).build(),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(VibePrimary.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isVideoMsg) Icons.Default.Videocam else Icons.Default.Mic,
                        contentDescription = null,
                        tint = VibePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isVideoMsg) "Видеосообщение" else "Голосовое сообщение",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = "$durationText • ${formatTimestamp(item.message.timestamp)}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                fontSize = 11.sp
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
