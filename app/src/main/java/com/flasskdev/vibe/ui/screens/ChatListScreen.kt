package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.foundation.border
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.data.local.ChatWithUser
import com.flasskdev.vibe.ui.theme.*
import com.flasskdev.vibe.ui.viewmodels.ChatViewModel
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.InsertDriveFile
import com.flasskdev.vibe.ui.components.TypingIndicator
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.rotate


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    liquidState: LiquidState,
    webSocket: VibeWebSocket,
    onChatClick: (interlocutorId: Int, interlocutorName: String) -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    val chats by viewModel.chats.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val typingUsers by viewModel.typingUsers.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    var hasInternet by remember { mutableStateOf(true) }
    val strings = LocalVibeStrings.current
    var searchQuery by remember { mutableStateOf("") }
    var showContextMenuFor by remember { mutableStateOf<ChatWithUser?>(null) }
    val filteredChats = remember(chats, searchQuery) {
        chats.filter {
            it.name?.contains(searchQuery, ignoreCase = true) == true ||
                it.username?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    if (showContextMenuFor != null) {
        val chat = showContextMenuFor!!.chat
        ModalBottomSheet(
            onDismissRequest = { showContextMenuFor = null }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, top = 8.dp)
            ) {
                // Элемент 2: Закрепление чата (вынесен в отдельный Row)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (chat.pinned) viewModel.unpinChat(chat.interlocutorId)
                            else viewModel.pinChat(chat.interlocutorId)
                            showContextMenuFor = null
                        }
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.PushPin,
                        modifier = Modifier.rotate(45f), // Запятая добавлена
                        contentDescription = null,
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (chat.pinned) strings.unpin else strings.pin,
                        fontSize = 16.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                    )
                }

                // Элемент 1: Уведомления
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (chat.isMuted) viewModel.unmuteUser(chat.interlocutorId)
                            else viewModel.muteUser(chat.interlocutorId)
                            showContextMenuFor = null
                        }
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (chat.isMuted) Icons.Default.NotificationsActive else Icons.Default.VolumeOff,
                        contentDescription = null,
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (chat.isMuted) strings.unmuteNotifications else strings.muteNotifications,
                        fontSize = 16.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }


    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            kotlinx.coroutines.delay(300)
            viewModel.searchUsers(searchQuery)
        } else {
            viewModel.clearSearchResults()
        }
    }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            hasInternet = true
        } else {
            // Connectivity checks do not belong on the main thread and do not need a 1-second cadence.
            while (!isConnected) {
                hasInternet = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    viewModel.hasInternet()
                }
                kotlinx.coroutines.delay(4_000)
            }
        }
    }

    LaunchedEffect(webSocket) {
        viewModel.attachWebSocket(webSocket)
    }
    
    var toastMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(showToast) {
        if (showToast) {
            kotlinx.coroutines.delay(2500)
            showToast = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is com.flasskdev.vibe.ui.viewmodels.ChatUiEvent.ToastEvent -> {
                    toastMessage = event.message
                    showToast = true
                }
                is com.flasskdev.vibe.ui.viewmodels.ChatUiEvent.SpamblockError -> {
                    // Optional: Handle Spamblock error here
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(androidx.compose.material3.MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(top = 12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 16.dp)
            ) {
                Text(
                    text = if (isConnected) strings.chatsTitle else if (hasInternet) strings.connecting else strings.waitingForNetwork,
                    style = androidx.compose.material3.MaterialTheme.typography.displayLarge,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                )
                if (!isConnected) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TypingIndicator(
                        dotSize = 6.dp, 
                        dotColor = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }

            androidx.compose.foundation.text.BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(VibePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .liquid(liquidState) {
                        refraction = 0.2f
                        curve = 0.2f
                        edge = 0.08f
                    }
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.06f)),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = strings.searchPlaceholder,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )

            // Mini-player: only visible on Chats tab, under search bar
            val audioPlayerViewModel = com.flasskdev.vibe.LocalGlobalAudioPlayer.current
            val hazeState = remember { dev.chrisbanes.haze.HazeState() }
            var showExpandedPlayer by remember { mutableStateOf(false) }
            com.flasskdev.vibe.ui.components.GlobalMiniPlayer(
                viewModel = audioPlayerViewModel,
                hazeState = hazeState,
                onExpand = { showExpandedPlayer = true }
            )
            if (showExpandedPlayer) {
                com.flasskdev.vibe.ui.components.ExpandedAudioPlayerSheet(
                    viewModel = audioPlayerViewModel,
                    hazeState = hazeState,
                    onDismiss = { showExpandedPlayer = false }
                )
            }

            if (filteredChats.isEmpty() && chats.isEmpty() && searchQuery.isBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Forum,
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = strings.chatsEmptyTitle,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = strings.chatsEmptySubtitle,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(filteredChats, key = { it.chat.interlocutorId }) { chatWithUser ->
                        Box {
                            ChatItemView(
                                chatWithUser = chatWithUser,
                                isTyping = typingUsers[chatWithUser.chat.interlocutorId] == true,
                                onClick = {
                                    val name = chatWithUser.name ?: chatWithUser.username ?: "User #${chatWithUser.chat.interlocutorId}"
                                    onChatClick(chatWithUser.chat.interlocutorId, name)
                                },
                                onLongClick = {
                                    showContextMenuFor = chatWithUser
                                }
                            )
                        }
                    }

                    if (searchQuery.isNotBlank() && searchResults.isNotEmpty()) {
                        item {
                            Text(
                                text = strings.globalSearchResults,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        items(searchResults, key = { "search_${it.id}" }) { result ->
                            UserSearchResultView(
                                user = result,
                                onClick = {
                                    val name = result.name ?: result.username ?: "User #${result.id}"
                                    onChatClick(result.id, name)
                                }
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            com.flasskdev.vibe.ui.components.VibeToast(
                message = toastMessage,
                isVisible = showToast,
                onDismiss = { showToast = false }
            )
        }
    }
}

@Composable
fun UserSearchResultView(
    user: com.flasskdev.vibe.data.UserSearchResult,
    onClick: () -> Unit
) {
    val strings = LocalVibeStrings.current
    val name = when {
        user.isBanned -> strings.accountDeleted
        user.isFreezed -> strings.accountFrozen
        else -> user.name ?: user.username ?: "User #${user.id}"
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().clip(CircleShape).background(VibePrimary.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!user.avatarUrl.isNullOrEmpty() && !user.isBanned && !user.isFreezed) {
                        AsyncImage(
                            model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                .data(user.avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(text = name.take(1).uppercase(), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (user.isBanned) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    } else if (user.isFreezed) {
                        Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = Color(0xFF87CEEB), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = name,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    com.flasskdev.vibe.ui.components.UserBadgesRow(
                        isVerified = user.isVerified,
                        isDeveloper = user.isDeveloper,
                        isBot = user.isBot,
                        isBanned = user.isBanned,
                        isFreezed = user.isFreezed,
                        badgeSize = 14.dp
                    )
                }
                if (!user.username.isNullOrBlank()) {
                    Text(
                        text = "@${user.username}",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ChatItemView(
    chatWithUser: ChatWithUser,
    isTyping: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    val chat = chatWithUser.chat
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val name = when {
        chat.isBanned -> strings.accountDeleted
        chat.isFreezed -> strings.accountFrozen
        else -> chatWithUser.name ?: chatWithUser.username ?: "${strings.userLabel} #${chat.interlocutorId}"
    }
    val hasUnread = chat.unreadCount > 0
    val timeFormatted = remember(chat.timestamp) { formatChatTimestamp(chat.timestamp, strings) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().clip(CircleShape).background(VibePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    if (!chatWithUser.avatarUrl.isNullOrEmpty() && !chat.isBanned && !chat.isFreezed && !chat.isBlockedByUser) {
                        AsyncImage(
                            model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                .data(chatWithUser.avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(text = name.take(1).uppercase(), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                if (chatWithUser.isOnline == true && chatWithUser.isBot != true && !chat.isBanned && !chat.isFreezed && !chat.isBlockedByUser && !chat.isBlockedByMe) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-1).dp, y = (-1).dp)
                            .background(VibeOnlineGreen, CircleShape)
                            .border(2.5.dp, androidx.compose.material3.MaterialTheme.colorScheme.background, CircleShape)
                    )
                }

                if (chat.isMuted) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.TopStart)
                            .offset(x = (-2).dp, y = (-2).dp)
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface, CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeOff,
                            contentDescription = "Muted",
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        /* // Если аккаунт заблокирован - перед ником выводим иконку мусорки
                        if (chat.isBanned) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                        } else if (chat.isFreezed) {
                            Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = Color(0xFF87CEEB), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                        }*/
                        Text(
                            text = name,
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        com.flasskdev.vibe.ui.components.UserBadgesRow(
                            isVerified = chatWithUser.chat.isVerified,
                            isDeveloper = chatWithUser.chat.isDeveloper,
                            isBot = chatWithUser.isBot == true,
                            isBanned = chatWithUser.chat.isBanned,
                            isFreezed = chatWithUser.chat.isFreezed,
                            badgeSize = 16.dp
                        )
                    }

                    Text(
                        text = timeFormatted,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        color = if (hasUnread && !chat.isMuted) VibePrimary else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isTyping) {
                        Text(
                            text = LocalVibeStrings.current.typing,
                            color = VibePrimary,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        TypingIndicator()
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        if (!chat.draft.isNullOrBlank()) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color(0xFFfc0a00))) {
                                        append(strings.draftLabel)
                                    }
                                    append(chat.draft)
                                },
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            if (chat.isLastMessageMine) {
                                Icon(
                                    imageVector = if (chat.isLastMessageRead) Icons.Default.DoneAll else Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (chat.isLastMessageRead) VibePrimary else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }

                            val attachments = chat.lastMessageAttachments
                            val hasAttachments = !attachments.isNullOrEmpty()
                            val isVoiceMessage = chat.lastMessage.startsWith("duration:")
                            val isVideoMessage = chat.lastMessage.startsWith("video_message:")
                            val isAudio = hasAttachments && !isVoiceMessage && !isVideoMessage &&
                                com.flasskdev.vibe.utils.AttachmentUtils.isPlayableAudio(attachments!![0])
                            val isFile = hasAttachments && !isVoiceMessage && !isVideoMessage && !isAudio &&
                                !com.flasskdev.vibe.utils.AttachmentUtils.isImage(attachments!![0]) &&
                                !com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(attachments!![0])
                            
                            // Show SINGLE icon per type — no double icons
                            if (isVoiceMessage) {
                                Icon(Icons.Default.Mic, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            } else if (isVideoMessage) {
                                val videoAttachment = attachments?.firstOrNull()
                                if (videoAttachment != null) {
                                    val isLocal = videoAttachment.startsWith("/") || videoAttachment.startsWith("content://") || videoAttachment.contains("cacheDir")
                                    val model = if (isLocal) java.io.File(videoAttachment) else if (videoAttachment.startsWith("http")) videoAttachment else "https://flasskdev.alwaysdata.net/api/upload/file/$videoAttachment"
                                    com.flasskdev.vibe.ui.components.VideoCover(
                                        source = model,
                                        modifier = Modifier.size(24.dp).clip(RoundedCornerShape(4.dp)),
                                        showPlayIcon = false
                                    )
                                } else {
                                    Icon(Icons.Default.Videocam, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            } else if (isAudio) {
                                Icon(Icons.Default.MusicNote, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            } else if (isFile) {
                                Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            } else if (hasAttachments) {
                                val firstAtt = attachments!![0]
                                val isLocal = firstAtt.startsWith("/") || firstAtt.startsWith("content://") || firstAtt.contains("cacheDir")
                                val model = if (isLocal) java.io.File(firstAtt) else if (firstAtt.startsWith("http")) firstAtt else "https://flasskdev.alwaysdata.net/api/upload/file/$firstAtt"
                                val isVideoAttachment = com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(firstAtt)
                                if (isVideoAttachment) {
                                    com.flasskdev.vibe.ui.components.VideoCover(
                                        source = model,
                                        modifier = Modifier.size(24.dp).clip(RoundedCornerShape(4.dp)),
                                        showPlayIcon = false
                                    )
                                } else {
                                    AsyncImage(
                                        model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                            .data(model).crossfade(true).build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(20.dp).clip(RoundedCornerShape(4.dp))
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            
                            // Do not start network/metadata extraction from every recycled lazy row.
                            // A cached value keeps previews informative while the detailed chat player owns full metadata loading.
                            val audioPreviewUrl = remember(attachments) {
                                attachments?.firstOrNull()?.let { firstAtt ->
                                    val isLocal = firstAtt.startsWith("/") || firstAtt.startsWith("content://") || firstAtt.contains("cacheDir")
                                    if (isLocal || firstAtt.startsWith("http")) firstAtt
                                    else "https://flasskdev.alwaysdata.net/api/upload/file/$firstAtt"
                                }
                            }
                            val cachedAudioMetadata = remember(audioPreviewUrl) {
                                audioPreviewUrl?.let { com.flasskdev.vibe.utils.AudioMetadataHelper.getCachedMetadata(it) }
                            }
                            val audioTitle = cachedAudioMetadata?.displayTitle
                            val audioArtist = cachedAudioMetadata?.displayArtist

                            // Clean text WITHOUT emoji prefix — icon already shown above
                            val displayText = when {
                                chat.lastMessage.startsWith("\$\$SYSTEM\$\$PINNED_MESSAGE|") -> {
                                    val parts = chat.lastMessage.substringAfter("\$\$SYSTEM\$\$PINNED_MESSAGE|").split("|")
                                    val senderN = parts.getOrNull(0) ?: "Someone"
                                    val msgContent = parts.getOrNull(1) ?: ""
                                    strings.pinnedMessageSystemText(senderN, msgContent)
                                }
                                isVoiceMessage -> {
                                    val ms = chat.lastMessage.substringAfter("duration:").toLongOrNull() ?: 0L
                                    val totalSec = ms / 1000
                                    "Голосовое сообщение ${String.format("%d:%02d", totalSec / 60, totalSec % 60)}"
                                }
                                isVideoMessage -> {
                                    val ms = chat.lastMessage.substringAfter("video_message:").toLongOrNull() ?: 0L
                                    val totalSec = ms / 1000
                                    "Видеосообщение ${String.format("%d:%02d", totalSec / 60, totalSec % 60)}"
                                }
                                isAudio -> {
                                    if (chat.lastMessage.isNotBlank()) {
                                        chat.lastMessage
                                    } else if (audioTitle != null) {
                                        if (audioArtist != "Unknown Artist") "$audioArtist — $audioTitle" else audioTitle!!
                                    } else {
                                        "Музыка..." // Loading state
                                    }
                                }
                                isFile -> {
                                    val fn = com.flasskdev.vibe.utils.AttachmentUtils.getFilename(attachments!![0])
                                    if (chat.lastMessage.isNotBlank()) chat.lastMessage else fn
                                }
                                hasAttachments -> {
                                    val count = attachments!!.size
                                    val hasCaption = chat.lastMessage.isNotBlank()
                                    val firstIsVideo = com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(attachments[0])
                                    if (count == 1) {
                                        if (hasCaption) chat.lastMessage else if (firstIsVideo) "Видео" else "Фотография"
                                    } else {
                                        val rem = count - 1
                                        if (hasCaption) "+$rem ${chat.lastMessage}"
                                        else if (firstIsVideo) "+$rem видео"
                                        else "+$rem фотографий"
                                    }
                                }
                                chat.lastMessage.isBlank() -> strings.chatHistoryEmpty
                                else -> chat.lastMessage
                            }

                            val previewText = displayText.replace("\n", " ")
                            if (com.flasskdev.vibe.utils.TextFormatting.hasFormatting(previewText)) {
                                com.flasskdev.vibe.ui.components.FormattedText(
                                    text = previewText,
                                    baseColor = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                                    fontSize = 14.sp,
                                    lineHeight = 18.sp,
                                    maxLines = 1,
                                    interactive = false,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Text(
                                    text = previewText,
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    if (chat.pinned) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.PushPin,
                            contentDescription = "Pinned",
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp).rotate(45f)
                        )
                    }

                    if (hasUnread) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .widthIn(min = 20.dp)
                                .background(if (chat.isMuted) Color.Gray else VibePrimary, CircleShape)
                                .padding(horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chat.unreadCount.toString(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                style = androidx.compose.ui.text.TextStyle(
                                    platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)
                                )
                            )
                        }
                    }
                }
            }
        }
        
        androidx.compose.material3.HorizontalDivider(
            modifier = Modifier.padding(start = 90.dp),
            thickness = 0.4.dp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
        )
    }
}

private fun formatChatTimestamp(timestamp: Long, strings: com.flasskdev.vibe.ui.theme.VibeStrings): String {
    val now = Calendar.getInstance()
    val msgTime = Calendar.getInstance().apply { timeInMillis = timestamp }

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return when {
        now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == msgTime.get(Calendar.DAY_OF_YEAR) -> {
            timeFormat.format(Date(timestamp))
        }
        now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - msgTime.get(Calendar.DAY_OF_YEAR) == 1 -> {
            strings.dateYesterday
        }
        now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR) &&
                now.get(Calendar.WEEK_OF_YEAR) == msgTime.get(Calendar.WEEK_OF_YEAR) -> {
            val dayFormat = SimpleDateFormat("EEE", Locale(strings.locale))
            dayFormat.format(Date(timestamp))
        }
        else -> {
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            dateFormat.format(Date(timestamp))
        }
    }
}