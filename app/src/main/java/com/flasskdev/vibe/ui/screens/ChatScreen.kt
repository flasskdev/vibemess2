package com.flasskdev.vibe.ui.screens

import androidx.compose.animation.*

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.ui.zIndex
import androidx.compose.foundation.text.BasicTextField
import io.github.fletchmckee.liquid.rememberLiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.data.local.MessageEntity
import com.flasskdev.vibe.ui.components.VibeBackgroundMesh
import com.flasskdev.vibe.ui.theme.*
import com.flasskdev.vibe.ui.viewmodels.ChatScreenViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.runtime.produceState
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Forum
import com.flasskdev.vibe.ui.components.TypingIndicator
import com.flasskdev.vibe.LocalGlobalAudioPlayer
import com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo
import com.flasskdev.vibe.ui.components.VoiceMessageBubble
import com.flasskdev.vibe.utils.AudioRecorderHelper
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.graphics.asImageBitmap
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.FormatQuote
import com.flasskdev.vibe.ui.components.FormattedText
import com.flasskdev.vibe.utils.TextFormatting
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    interlocutorId: Int,
    interlocutorName: String,
    webSocket: VibeWebSocket,
    onBack: () -> Unit,
    onProfileClick: (Int, String) -> Unit,
    onNavigateToSpamInfo: (Int) -> Unit = {},
    scrollToMessageId: Int? = null,
    viewModel: ChatScreenViewModel = viewModel()
) {
    val context = LocalContext.current
    val db = remember { com.flasskdev.vibe.data.local.AppDatabase.getDatabase(context) }
    val messages by viewModel.messages.collectAsState()
    val isPartnerTyping by viewModel.isPartnerTyping.collectAsState()
    val partnerName by viewModel.partnerName.collectAsState()
    val partnerUser by viewModel.partnerUser.collectAsState()
    val effectiveUser = partnerUser
    val isBlockedByMe = effectiveUser?.isBlockedByMe == true

    val highlightedMessageId by viewModel.highlightedMessageId.collectAsState()
    val editingMessage by viewModel.editingMessage.collectAsState()

    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val currentSearchIndex by viewModel.currentSearchIndex.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

        val pinnedMessages by viewModel.pinnedMessages.collectAsState()
    val currentPinnedIndex by viewModel.currentPinnedIndex.collectAsState()
    val pendingInlineCallbacks by viewModel.pendingInlineCallbacks.collectAsState()

    var showPinnedMessagesModal by remember { mutableStateOf(false) }
    val pinnedSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showUnpinAllDialog by remember { mutableStateOf(false) }
    var unpinForBoth by remember { mutableStateOf(false) }
    
    var messageToPin by remember { mutableStateOf<MessageEntity?>(null) }
    var messageToUnpin by remember { mutableStateOf<MessageEntity?>(null) }
    var botAlertText by remember { mutableStateOf<String?>(null) }

    var inputTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        var pendingPhotos by remember { mutableStateOf<List<android.net.Uri>>(emptyList()) }
    var pendingFiles by remember { mutableStateOf<List<android.net.Uri>>(emptyList()) }
    var pendingVideoCoverPaths by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    var viewingMessage by remember { mutableStateOf<MessageEntity?>(null) }
    var viewingPhotoIndex by remember { mutableIntStateOf(0) }
    var initialDraftLoaded by remember { mutableStateOf(false) }

    val photoPickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            pendingPhotos = uris
        }
    }

        LaunchedEffect(pendingPhotos) {
        pendingVideoCoverPaths = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            pendingPhotos.mapNotNull { uri ->
                val isVideo = context.contentResolver.getType(uri).orEmpty().startsWith("video/") ||
                    com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(uri.toString())
                if (isVideo) {
                    com.flasskdev.vibe.utils.VideoCoverGenerator.create(context, uri)
                        ?.absolutePath
                        ?.let { coverPath -> uri.toString() to coverPath }
                } else {
                    null
                }
            }.toMap()
        }
    }

    val documentPickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(

        contract = androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            pendingFiles = uris
        }
    }

    LaunchedEffect(interlocutorId) {
        val chat = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            db.chatDao().getChatById(interlocutorId)
        }
        if (chat?.draft != null && inputTextFieldValue.text.isEmpty() && editingMessage == null) {
            inputTextFieldValue = TextFieldValue(chat.draft)
        }
        initialDraftLoaded = true
    }

    LaunchedEffect(inputTextFieldValue.text) {
        if (initialDraftLoaded && editingMessage == null) {
            kotlinx.coroutines.delay(300)
            db.chatDao().saveDraft(interlocutorId, inputTextFieldValue.text.ifBlank { null })
        }
    }

    val selectedMessages = remember { mutableStateListOf<Int>() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showForwardSheet by remember { mutableStateOf(false) }
    val forwardSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val liquidState = rememberLiquidState()
    val hazeState = remember { HazeState() }
    
    var isActionMenuExpanded by remember { mutableStateOf(false) }
    var showAttachmentMenu by remember { mutableStateOf(false) }
    var showEmojiPanel by remember { mutableStateOf(false) }
    var showFormattingBar by remember { mutableStateOf(false) }
    var showPreviewMode by remember { mutableStateOf(false) }
    var showLinkInputDialog by remember { mutableStateOf(false) }
    var linkInputSelection by remember { mutableStateOf(androidx.compose.ui.text.TextRange.Zero) }
    var linkInputInitialText by remember { mutableStateOf("") }
    var showColorInputDialog by remember { mutableStateOf(false) }
    var colorInputSelection by remember { mutableStateOf(androidx.compose.ui.text.TextRange.Zero) }
    var colorInputInitialText by remember { mutableStateOf("") }
    
    var spamblockErrorMsg by remember { mutableStateOf<String?>(null) }
    var waitingForSpamInfo by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastTrigger by remember { androidx.compose.runtime.mutableLongStateOf(0L) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(toastTrigger) {
        if (toastTrigger > 0L) {
            showToast = true
            kotlinx.coroutines.delay(2500)
            showToast = false
        }
    }
    val triggerToast: (String) -> Unit = remember {
        { msg: String ->
            toastMessage = msg
            toastTrigger = System.currentTimeMillis()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.botCallbackAlert.collect { (text, _) ->
            botAlertText = text
        }
    }
    LaunchedEffect(Unit) {
        viewModel.botCallbackToast.collect { text ->
            triggerToast(text)
        }
    }
    
    var reportMessage by remember { mutableStateOf<MessageEntity?>(null) }
    
    val recorderHelper = remember { com.flasskdev.vibe.utils.AudioRecorderHelper(context) }
    
    val recordAudioPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            triggerToast("Разрешение на микрофон необходимо для записи")
        }
    }
    val isRecording by recorderHelper.isRecording.collectAsState()
    val recordingDuration by recorderHelper.recordingDuration.collectAsState()
    val audioPlayer = LocalGlobalAudioPlayer.current
    val currentPlayingTrack by audioPlayer.currentTrack.collectAsState()
    val isPlayingAudio by audioPlayer.isPlaying.collectAsState()
    val audioProgress by audioPlayer.progress.collectAsState()
    
    val scope = rememberCoroutineScope()
    
    DisposableEffect(webSocket) {
        val listener = object : com.flasskdev.vibe.data.VibeWebSocketListener {
            override fun onReportError(error: String) {
                scope.launch { triggerToast(error) }
            }
            override fun onReportSuccess(messageId: Int) {
                scope.launch { triggerToast("Жалоба успешно отправлена") }
            }
            override fun onSendMessageError(error: String, message: String) {
                if (error == "spamblock_active") {
                    spamblockErrorMsg = message
                } else {
                    scope.launch {
                        triggerToast(message)
                    }
                }
            }
            override fun onUsersSearchResult(usersList: List<com.flasskdev.vibe.data.UserSearchResult>) {
                if (waitingForSpamInfo) {
                    val spamBot = usersList.find { it.username.equals("SpamInfo", ignoreCase = true) }
                    if (spamBot != null) {
                        waitingForSpamInfo = false
                        spamblockErrorMsg = null
                        // Navigate to chat
                        onNavigateToSpamInfo(spamBot.id)
                    }
                }
            }
        }
        webSocket.addListener(listener)
        onDispose { webSocket.removeListener(listener) }
    }
    
    LaunchedEffect(editingMessage) {
        if (editingMessage != null) {
            inputTextFieldValue = TextFieldValue(editingMessage!!.content, TextRange(editingMessage!!.content.length))
        }
    }
    val listState = rememberLazyListState()
    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val displayName = when {
        partnerUser?.isBanned == true -> strings.accountDeleted
        partnerUser?.isFreezed == true -> strings.accountFrozen
        else -> partnerName.ifEmpty { interlocutorName }
    }

    val groupedMessages by viewModel.groupedMessages.collectAsState()
    // Build indices from the same snapshot used by LazyColumn. This avoids a stale asynchronous
    // index map when a reply or pin jump replaces the message window.
    val messageLazyIndex = remember(groupedMessages) {
        buildMap {
            var lazyIndex = 0
            groupedMessages.forEach { (_, messagesInDay) ->
                messagesInDay.forEach { message ->
                    put(message.id, lazyIndex++)
                }
                lazyIndex++ // Date separator for the group.
            }
        }
    }
    val myAvatarUrl by viewModel.myAvatarUrl.collectAsState()

    val chatMusicPlaylist by produceState<List<AudioTrackInfo>>(emptyList(), messages, myAvatarUrl, partnerUser?.avatarUrl) {
        value = withContext(Dispatchers.Default) {
            buildChatAudioPlaylist(messages, myAvatarUrl, partnerUser?.avatarUrl, viewModel.myUserId)
        }
    }

    val messagesById = remember(messages) {
        messages.associateBy { it.id }
    }

    LaunchedEffect(interlocutorId) {
        viewModel.init(interlocutorId, interlocutorName, webSocket)
    }

    var hasJumpedToInitialMessage by remember(scrollToMessageId) { mutableStateOf(false) }
    LaunchedEffect(scrollToMessageId, messages.isNotEmpty()) {
        if (scrollToMessageId != null && scrollToMessageId > 0 && messages.isNotEmpty() && !hasJumpedToInitialMessage) {
            hasJumpedToInitialMessage = true
            viewModel.jumpToMessage(scrollToMessageId, messages)
        }
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.refreshUserInfo()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    androidx.activity.compose.BackHandler {
        if (showEmojiPanel) {
            showEmojiPanel = false
        } else if (selectedMessages.isNotEmpty()) {
            selectedMessages.clear()
        } else if (isSearchActive) {
            viewModel.closeSearch()
        } else {
            onBack()
        }
    }

    var previousLastMessageId by remember { mutableStateOf<Int?>(null) }
    var previousMessageCount by remember { mutableIntStateOf(0) }
    var newMessagesCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (listState.firstVisibleItemIndex == 0) {
            newMessagesCount = 0
        }
    }

    LaunchedEffect(messages) {
        val lastMsg = messages.lastOrNull()
        if (messages.size > 0 && previousMessageCount > 0) {
            if (messages.size < previousMessageCount) {
                // Сброс контекстного режима - скроллим в самый низ
                listState.scrollToItem(0)
            } else if (lastMsg != null && previousLastMessageId != null && lastMsg.id != previousLastMessageId) {
                // Новое сообщение пришло
                val added = maxOf(1, messages.size - previousMessageCount)
                val isMyMessage = lastMsg.senderId == viewModel.myUserId
                if (isMyMessage || listState.firstVisibleItemIndex <= 1) {
                    listState.animateScrollToItem(0)
                } else {
                    newMessagesCount += added
                }
            }
        }
        previousMessageCount = messages.size
        previousLastMessageId = lastMsg?.id
    }

    // Подгрузка старых сообщений при скролле вверх (в reverseLayout это конец списка)
    LaunchedEffect(listState) {
        snapshotFlow { 
            val layoutInfo = listState.layoutInfo
            val total = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            Pair(lastVisible, total)
        }
        .distinctUntilChanged()
        .collect { (lastVisible, total) ->
            if (total > 0 && total - lastVisible <= 5 && messages.size >= 15) {
                viewModel.loadMoreMessages()
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.mapNotNull { it.key as? Int } }
            .distinctUntilChanged()
            .collect { visibleKeys ->
                if (visibleKeys.isNotEmpty()) {
                    viewModel.updateCurrentPinnedIndex(visibleKeys)
                    viewModel.onMessagesVisible(visibleKeys)
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize().background(androidx.compose.material3.MaterialTheme.colorScheme.background)) {

        // The chat list is composed later in this Box. Keep the complete chrome layer above it
        // both visually and in Compose hit testing.
        Box(modifier = Modifier.fillMaxSize().imePadding().zIndex(20f)) {

                        // This Column and the full-screen message container are direct children of the same Box.
            // Its zIndex must therefore be set here (not only on descendants) for both rendering and taps.
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(10f)
                    .statusBarsPadding()
                    .fillMaxWidth()
            ) {

                // ========== HEADER ==========
                Box(
                    modifier = Modifier
                                                .padding(start = 12.dp, end = 12.dp, top = 6.dp)
                        .fillMaxWidth()
                        .zIndex(21f)

                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                        .hazeChild(state = hazeState)
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                    ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedMessages.isNotEmpty()) {
                            IconButton(onClick = { selectedMessages.clear() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Clear selection",
                                    tint = VibePrimary
                                )
                            }
                            Text(
                                text = strings.selectedMessagesCount(selectedMessages.size),
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { showForwardSheet = true }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Reply,
                                    contentDescription = "Forward",
                                    tint = VibePrimary,
                                    modifier = Modifier.size(24.dp).scale(scaleX = -1f, scaleY = 1f)
                                )
                            }
                            IconButton(onClick = { showDeleteDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = VibeError
                                )
                            }
                        } else if (isSearchActive) {
                            IconButton(onClick = { viewModel.closeSearch() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Close search",
                                    tint = VibePrimary
                                )
                            }

                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.setSearchQuery(it) },
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 16.sp
                                ),
                                cursorBrush = SolidColor(VibePrimary),
                                singleLine = true,
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    imeAction = androidx.compose.ui.text.input.ImeAction.Search
                                ),
                                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                                    onSearch = {
                                        val results = searchResults
                                        if (results.isNotEmpty()) {
                                            val target = results.getOrNull(currentSearchIndex) ?: results.first()
                                            viewModel.jumpToMessage(target.id, messages)
                                        }
                                    }
                                ),
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                                decorationBox = { innerTextField ->
                                    Box(contentAlignment = Alignment.CenterStart) {
                                        if (searchQuery.isEmpty()) {
                                            Text(
                                                text = "Поиск сообщений...",
                                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                                                fontSize = 15.sp
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )

                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.setSearchQuery("") },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = { showDatePicker = true },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Search by date",
                                    tint = VibePrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            if (searchQuery.isNotBlank()) {
                                if (searchResults.isNotEmpty()) {
                                    Text(
                                        text = "${currentSearchIndex + 1}/${searchResults.size}",
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    IconButton(
                                        onClick = { viewModel.nextSearchResult(messages) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Next result (up)",
                                            tint = VibePrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.prevSearchResult(messages) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Previous result (down)",
                                            tint = VibePrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "0",
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp)
                                    )
                                }
                            }
                        } else {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = strings.backBtn,
                                    tint = VibePrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                                                .weight(1f)
                                .zIndex(22f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onProfileClick(interlocutorId, displayName) }

                                .padding(4.dp)
                        ) {
                            // 1. АВАТАРКА
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(VibePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!partnerUser?.avatarUrl.isNullOrEmpty() && partnerUser?.isBanned != true && partnerUser?.isFreezed != true && partnerUser?.isBlockedByUser != true) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(partnerUser?.avatarUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text(
                                        text = if (displayName.isNotEmpty()) displayName.take(1).uppercase() else "",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    /* // Если аккаунт заблокирован - перед ником выводим иконку мусорки
                                    if (partnerUser?.isBanned == true) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    } else if (partnerUser?.isFreezed == true) {
                                        Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = Color(0xFF87CEEB), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }*/
                                    Text(
                                        text = displayName,
                                        modifier = Modifier.weight(1f, fill = false),
                                        style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    com.flasskdev.vibe.ui.components.UserBadgesRow(
                                        isVerified = partnerUser?.isVerified == true,
                                        isDeveloper = partnerUser?.isDeveloper == true,
                                        isBot = partnerUser?.isBot == true,
                                        isBanned = partnerUser?.isBanned == true,
                                        isFreezed = partnerUser?.isFreezed == true,
                                        badgeSize = 14.dp
                                    )
                                }



                                if (isPartnerTyping) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = strings.typing,
                                            color = VibePrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        TypingIndicator()
                                    }
                                } else {
                                    val isBlocked = partnerUser?.isBlockedByUser == true || partnerUser?.isBanned == true || partnerUser?.isFreezed == true
                                    val isBlockedByMe = partnerUser?.isBlockedByMe == true
                                    val isOnline = partnerUser?.isOnline == true && !isBlocked && !isBlockedByMe && partnerUser?.isBot != true

                                    val statusText = when {
                                        isBlocked -> strings.lastSeenLongAgo
                                        isBlockedByMe -> "Заблокирован"
                                        partnerUser?.isBot == true -> strings.statusBot
                                        isOnline -> strings.statusOnline
                                        partnerUser?.lastSeenStatus == "hidden" || partnerUser?.lastSeenStatus == "approximate" || partnerUser?.lastSeenStatus == "recently" -> strings.lastSeenRecently
                                        partnerUser?.lastSeenStatus == "long_ago" -> strings.lastSeenLongAgo
                                        partnerUser?.lastSeenStatus == "this_week" -> strings.lastSeenInWeek
                                        partnerUser?.lastSeenStatus == "this_month" -> strings.lastSeenInMonth
                                        else -> formatLastSeen(partnerUser?.lastSeen, strings)
                                    }
                                    Text(
                                        text = statusText,
                                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                        color = if (isBlockedByMe) com.flasskdev.vibe.ui.theme.VibeError
                                            else if (isOnline) VibeOnlineGreen
                                            else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
                                    )
                                }
                            }
                        } // close profile Row
                            // Three-dots menu button
                            var showHeaderMenu by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { showHeaderMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Menu",
                                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    )
                                }
                                DropdownMenu(
                                    expanded = showHeaderMenu,
                                    onDismissRequest = { showHeaderMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Поиск") },
                                        onClick = {
                                            showHeaderMenu = false
                                            viewModel.openSearch()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Поиск по дате") },
                                        onClick = {
                                            showHeaderMenu = false
                                            showDatePicker = true
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.CalendarMonth,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text(strings.formatFormat, fontWeight = if (showFormattingBar) FontWeight.Bold else FontWeight.Normal) },
                                        onClick = {
                                            showFormattingBar = !showFormattingBar
                                            if (!showFormattingBar) {
                                                showPreviewMode = false
                                            }
                                            showHeaderMenu = false
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.FormatBold,
                                                contentDescription = null,
                                                tint = if (showFormattingBar) VibePrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                    )

                                    if (effectiveUser?.isBot != true) {
                                        if (isBlockedByMe) {
                                            DropdownMenuItem(
                                                text = { Text("Разблокировать", color = MaterialTheme.colorScheme.onSurface) },
                                                onClick = {
                                                    viewModel.unblockUser(interlocutorId)
                                                    showHeaderMenu = false
                                                    triggerToast("Пользователь разблокирован")
                                                },
                                                leadingIcon = {
                                                    Icon(
                                                        imageVector = Icons.Outlined.LockOpen,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                    )
                                                }
                                            )
                                        } else {
                                            DropdownMenuItem(
                                                text = { Text("Заблокировать", color = VibeError) },
                                                onClick = {
                                                    viewModel.blockUser(interlocutorId)
                                                    showHeaderMenu = false
                                                },
                                                leadingIcon = {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Block,
                                                        contentDescription = null,
                                                        tint = VibeError
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        } // close else block
                    } // close header Row

                    // ========== PINNED MESSAGES HEADER ==========
                    if (pinnedMessages.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        )
                        
                                                val currentMsg = pinnedMessages.getOrNull(currentPinnedIndex) ?: pinnedMessages.first()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.jumpToMessage(currentMsg.id, messages) }
                                .padding(horizontal = 4.dp, vertical = 4.dp),

                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 12.dp)
                            ) {
                                val count = pinnedMessages.size
                                // Pinned messages are ordered newest-first; the banner number must use the same order.
                                val currentIndexNum = currentPinnedIndex + 1

                                Text(
                                    text = "$currentIndexNum/$count",
                                    color = VibePrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp, start = 4.dp)
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(VibePrimary)
                                )
                            }
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (pinnedMessages.size > 1) strings.pinnedMessages else strings.pinnedMessage,
                                    color = VibePrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                
                                MessagePreviewBlock(
                                    message = currentMsg,
                                    textColor = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                            }

                            IconButton(
                                onClick = { showUnpinAllDialog = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Unpin all",
                                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    // ========== MINI PLAYER (under pinned/topbar) ==========
                    val chatHazeState = remember { dev.chrisbanes.haze.HazeState() }
                    var showChatExpandedPlayer by remember { mutableStateOf(false) }
                    com.flasskdev.vibe.ui.components.GlobalMiniPlayer(
                        viewModel = audioPlayer,
                        hazeState = chatHazeState,
                        isInline = true,
                        onExpand = { showChatExpandedPlayer = true }
                    )
                    if (showChatExpandedPlayer) {
                        com.flasskdev.vibe.ui.components.ExpandedAudioPlayerSheet(
                            viewModel = audioPlayer,
                            hazeState = chatHazeState,
                            onDismiss = { showChatExpandedPlayer = false }
                        )
                    }

                    } // close Column
                } // close Box
            }

                        LaunchedEffect(highlightedMessageId, messageLazyIndex) {
                val targetIndex = highlightedMessageId?.let(messageLazyIndex::get)
                if (targetIndex != null) {
                    // scrollToItem performs a deterministic jump and supersedes the prior scroll mutation.
                    listState.scrollToItem(targetIndex)
                }
            }

            // Keep the scrolling content below the top chrome. This is a sibling-level layer boundary.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(0f)
                    .clipToBounds()
                    .liquefiable(liquidState)
                    .haze(hazeState)
            ) {

                if (messages.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.Forum,
                                contentDescription = null,
                                tint = VibePrimary.copy(alpha = 0.5f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = strings.emptyChat ?: "Здесь пока пусто...",
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    val pinnedIds = remember(pinnedMessages) { pinnedMessages.mapTo(HashSet()) { it.id } }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        state = listState,
                        reverseLayout = true,
                        contentPadding = PaddingValues(top = 160.dp, bottom = 120.dp)
                ) {
                    groupedMessages.forEach { (dateMillis, messagesInDay) ->
                        itemsIndexed(messagesInDay, key = { _, it -> it.id }) { index, message ->
                            val isNewerSame = messagesInDay.getOrNull(index - 1)?.senderId == message.senderId
                            val isOlderSame = messagesInDay.getOrNull(index + 1)?.senderId == message.senderId
                            
                            if (message.content.startsWith("\$\$SYSTEM\$\$PINNED_MESSAGE|")) {
                                val parts = message.content.substringAfter("\$\$SYSTEM\$\$PINNED_MESSAGE|").split("|")
                                val senderN = parts.getOrNull(0) ?: "Someone"
                                val msgContent = parts.getOrNull(1) ?: ""
                                val sysText = strings.pinnedMessageSystemText(senderN, msgContent)
                                Box(
                                                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),

                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.padding(horizontal = 32.dp)
                                    ) {
                                        Text(
                                            text = sysText,
                                            fontSize = 12.sp,
                                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            } else {
                                MessageBubble(
                                                                        modifier = Modifier.padding(

                                        bottom = if (isNewerSame) 2.dp else 12.dp
                                    ),
                                    message = message,
                                    repliedMessage = message.replyToId?.let { messagesById[it] },
                                    isMine = message.senderId == viewModel.myUserId,
                                    isNewerSameSender = isNewerSame,
                                    isOlderSameSender = isOlderSame,
                                    strings = strings,
                                    isHighlighted = message.id == highlightedMessageId,
                                    onReply = { viewModel.replyToMessage(it) },
                                    onReplyClick = { replyId -> viewModel.jumpToMessage(replyId, messages) },
                                    onEditClick = { viewModel.startEditing(it) },
                                    isPinned = message.id in pinnedIds,
                                    onPinRequest = { msg -> 
                                        messageToPin = msg
                                    },
                                    onUnpinRequest = { msg ->
                                        messageToUnpin = msg
                                    },
                                    isSelected = selectedMessages.contains(message.id),
                                    selectionMode = selectedMessages.isNotEmpty(),
                                    onSelect = {
                                        if (selectedMessages.contains(message.id)) {
                                            selectedMessages.remove(message.id)
                                        } else {
                                            if (selectedMessages.size < 10) {
                                                selectedMessages.add(message.id)
                                            }
                                        }
                                    },
                                    onReportClick = { reportMessage = it },
                                    onProfileClick = onProfileClick,
                                    onShowCopyToast = { triggerToast("Скопировано") },
                                    onImageClick = { msg, idx ->
                                        viewingMessage = msg
                                        viewingPhotoIndex = idx
                                    },
                                    onRetryUpload = { msgId -> viewModel.retryUpload(context, msgId) },
                                    myAvatarUrl = myAvatarUrl,
                                                                        partnerAvatarUrl = partnerUser?.avatarUrl,
                                    partnerName = partnerUser?.name,
                                    myDisplayName = viewModel.myDisplayName,
                                    myUserId = viewModel.myUserId,

                                    onReactionToggle = { msg, emoji -> viewModel.toggleReaction(msg, emoji) },
                                    onReactionLongClick = { msg, emoji -> viewModel.openReactionDetails(msg, emoji) },
                                                                        inlineKeyboardEnabled = !pendingInlineCallbacks.containsKey(message.id),
                                    pendingInlineCallbackData = pendingInlineCallbacks[message.id]?.callbackData,
                                    onInlineButtonClick = { msg, btn ->
                                        viewModel.onInlineButtonClicked(msg, btn) { url ->

                                            try {
                                                val uri = android.net.Uri.parse(url)
                                                context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, uri))
                                            } catch (e: Exception) {
                                                triggerToast("Не удалось открыть ссылку")
                                            }
                                        }
                                    },
                                    chatPlaylist = chatMusicPlaylist,
                                    liquidState = liquidState
                                )
                            }
                        }

                        item(key = "date_$dateMillis") {
                            DateSeparator(
                                                                modifier = Modifier,
                                dateMillis = dateMillis,

                                strings = strings
                            )
                        }
                    }
                }
                } // Close else block

                // Кнопка "Вниз"
                val showFab by remember {
                    derivedStateOf {
                        (listState.canScrollBackward || viewModel.isContextMode) && messages.isNotEmpty()
                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = showFab,
                    enter = androidx.compose.animation.scaleIn() + androidx.compose.animation.fadeIn(),
                    exit = androidx.compose.animation.scaleOut() + androidx.compose.animation.fadeOut(),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 88.dp)
                ) {
                    Box {
                        androidx.compose.material3.FloatingActionButton(
                            onClick = {
                                if (!viewModel.isContextMode && listState.canScrollBackward) {
                                    scope.launch {
                                        val current = listState.firstVisibleItemIndex
                                        if (current > 15) {
                                            listState.scrollToItem(15)
                                        }
                                        listState.animateScrollToItem(0)
                                    }
                                } else {
                                    viewModel.jumpToBottom()
                                }
                            },
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            contentColor = VibePrimary,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(androidx.compose.material.icons.Icons.Default.ArrowDownward, contentDescription = "Вниз")
                        }
                        
                        if (newMessagesCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .offset(x = (-4).dp, y = (-4).dp)
                                    .size(20.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(Color.Red),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (newMessagesCount > 99) "99+" else newMessagesCount.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            val replyingToMessage by viewModel.replyingToMessage.collectAsState()

            val canSendTextOrPhoto = inputTextFieldValue.text.isNotBlank() || pendingPhotos.isNotEmpty() || pendingFiles.isNotEmpty()
            var dragOffset by remember { mutableStateOf(0f) }
            var dragOffsetY by remember { mutableStateOf(0f) }
            var isLocked by remember { mutableStateOf(false) }

            if (isRecording && !isLocked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .navigationBarsPadding()
                        .padding(end = 28.dp, bottom = 90.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                            .border(1.dp, androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(androidx.compose.material.icons.Icons.Default.Lock, contentDescription = "Lock", tint = VibePrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Icon(androidx.compose.material.icons.Icons.Default.KeyboardArrowUp, contentDescription = null, tint = VibePrimary.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            ) {
            // Inline emoji/sticker/GIF panel: slides up in place of the keyboard,
            // ABOVE the input bar so it never covers the text field.
            AnimatedVisibility(
                visible = showEmojiPanel,
                enter = expandVertically(animationSpec = tween(180)) + fadeIn(tween(180)),
                exit = shrinkVertically(animationSpec = tween(160)) + fadeOut(tween(120))
            ) {
                com.flasskdev.vibe.ui.components.EmojiStickerGifPanel(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    onEmojiClick = { emoji ->
                        val sel = inputTextFieldValue.selection
                        val start = minOf(sel.start, sel.end).coerceIn(0, inputTextFieldValue.text.length)
                        val end = maxOf(sel.start, sel.end).coerceIn(0, inputTextFieldValue.text.length)
                        val newText = inputTextFieldValue.text.replaceRange(start, end, emoji)
                        if (newText.length <= 2048) {
                            inputTextFieldValue = TextFieldValue(newText, androidx.compose.ui.text.TextRange(start + emoji.length))
                            viewModel.onTextChanged(newText)
                        }
                    },
                    onStickerClick = { stickerId ->
                        viewModel.sendSticker(stickerId)
                        showEmojiPanel = false
                    },
                    onGifClick = { gif ->
                        viewModel.sendGif(gif.fullUrl, gif.width, gif.height)
                        showEmojiPanel = false
                    }
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .hazeChild(state = hazeState)
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f))
            ) {
                Column {
                    AnimatedVisibility(
                        visible = replyingToMessage != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        if (replyingToMessage != null) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(36.dp)
                                        .background(VibePrimary, RoundedCornerShape(2.dp))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    val replyName = if (replyingToMessage!!.senderId == viewModel.myUserId) strings.replyDefault else displayName
                                    Text(
                                        text = replyName, 
                                        color = VibePrimary, 
                                        fontSize = 14.sp, 
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    MessagePreviewBlock(
                                        message = replyingToMessage!!,
                                        textColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                }
                                IconButton(onClick = { viewModel.cancelReply() }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancel", tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                                }
                            }
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = editingMessage != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        if (editingMessage != null) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(36.dp)
                                            .background(VibePrimary, RoundedCornerShape(2.dp))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(strings.editMessageTitle, color = VibePrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        val editContent = editingMessage!!.content.replace("\n", " ")
                                        if (com.flasskdev.vibe.utils.TextFormatting.hasFormatting(editContent)) {
                                            FormattedText(
                                                text = editContent,
                                                baseColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                                fontSize = 13.sp,
                                                lineHeight = 16.sp,
                                                maxLines = 1,
                                                interactive = false
                                            )
                                        } else {
                                            Text(
                                                text = editContent, 
                                                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface, 
                                                fontSize = 13.sp, 
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    IconButton(onClick = { 
                                        viewModel.cancelEditing() 
                                        inputTextFieldValue = TextFieldValue("")
                                    }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                                    }
                                }
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
                            }
                        }
                    }

                    if (isBlockedByMe) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Вы заблокировали пользователя",
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            androidx.compose.material3.Button(
                                onClick = { 
                                    viewModel.unblockUser(interlocutorId)
                                    triggerToast("Пользователь разблокирован")
                                },
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = VibePrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                            ) {
                                Text(
                                    text = "Разблокировать",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else if (effectiveUser?.canMessage != false) {
                        AnimatedVisibility(
                            visible = pendingPhotos.isNotEmpty() || pendingFiles.isNotEmpty(),
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                                                        val selectedVideoCount = pendingPhotos.count { uri ->
                                        val mimeType = context.contentResolver.getType(uri).orEmpty()
                                        mimeType.startsWith("video/") || com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(uri.toString())
                                    }
                                    val selectedPhotoCount = pendingPhotos.size - selectedVideoCount
                                    val countText = buildList {
                                        if (selectedPhotoCount > 0) add("$selectedPhotoCount фото")
                                        if (selectedVideoCount > 0) add("$selectedVideoCount видео")
                                        if (pendingFiles.isNotEmpty()) add("${pendingFiles.size} файл(ов)")
                                    }.joinToString(", ").ifBlank { "Нет вложений" }
                                    Text(
                                        text = "Выбрано: $countText",

                                        color = VibePrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(onClick = { 
                                        pendingPhotos = emptyList() 
                                        pendingFiles = emptyList()
                                    }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                                                        pendingPhotos.forEach { uri ->
                                        val isVideo = context.contentResolver.getType(uri).orEmpty().startsWith("video/") ||
                                            com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(uri.toString())
                                        Box(
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isVideo) {
                                                com.flasskdev.vibe.ui.components.VideoCover(
                                                    source = pendingVideoCoverPaths[uri.toString()]?.let { coverPath -> java.io.File(coverPath) } ?: uri,
                                                    modifier = Modifier.fillMaxSize(),
                                                    frameMillis = 500L
                                                )
                                            } else {
                                                AsyncImage(
                                                    model = ImageRequest.Builder(LocalContext.current)
                                                        .data(uri)
                                                        .crossfade(true)
                                                        .build(),
                                                    contentDescription = "Выбранное фото",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                    }

                                    pendingFiles.forEach { uri ->
                                        val filename = androidx.documentfile.provider.DocumentFile.fromSingleUri(context, uri)?.name ?: "File"
                                        Box(
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(Icons.Default.Code, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(24.dp))
                                                Text(filename, fontSize = 10.sp, color = VibePrimary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(horizontal = 4.dp))
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
                            }
                        }

                        // Formatting toolbar (shown when format mode is active)
                        AnimatedVisibility(
                            visible = showFormattingBar,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Bold
                                FormatButton(Icons.Default.FormatBold) {
                                    inputTextFieldValue = insertFormatMarker(inputTextFieldValue, "**", "**")
                                }
                                // Italic
                                FormatButton(Icons.Default.FormatItalic) {
                                    inputTextFieldValue = insertFormatMarker(inputTextFieldValue, "__", "__")
                                }
                                // Strikethrough
                                FormatButton(Icons.Default.FormatStrikethrough) {
                                    inputTextFieldValue = insertFormatMarker(inputTextFieldValue, "~~", "~~")
                                }
                                // Underline
                                FormatButton(Icons.Default.FormatUnderlined) {
                                    inputTextFieldValue = insertFormatMarker(inputTextFieldValue, "--", "--")
                                }
                                // Monospace
                                FormatButton(Icons.Default.Code) {
                                    inputTextFieldValue = insertFormatMarker(inputTextFieldValue, "`", "`")
                                }
                                // Link
                                FormatButton(Icons.Default.Link) {
                                    val sel = inputTextFieldValue.selection
                                    linkInputSelection = sel
                                    val start = minOf(sel.start, sel.end).coerceIn(0, inputTextFieldValue.text.length)
                                    val end = maxOf(sel.start, sel.end).coerceIn(0, inputTextFieldValue.text.length)
                                    linkInputInitialText = if (start != end) inputTextFieldValue.text.substring(start, end) else ""
                                    showLinkInputDialog = true
                                }
                                // Color
                                FormatButton(Icons.Default.Palette) {
                                    val sel = inputTextFieldValue.selection
                                    colorInputSelection = sel
                                    val start = minOf(sel.start, sel.end).coerceIn(0, inputTextFieldValue.text.length)
                                    val end = maxOf(sel.start, sel.end).coerceIn(0, inputTextFieldValue.text.length)
                                    colorInputInitialText = if (start != end) inputTextFieldValue.text.substring(start, end) else ""
                                    showColorInputDialog = true
                                }
                                // Spoiler
                                FormatButton(Icons.Default.VisibilityOff) {
                                    inputTextFieldValue = insertFormatMarker(inputTextFieldValue, "||", "||")
                                }
                                // Quote
                                FormatButton(Icons.Default.FormatQuote) {
                                    inputTextFieldValue = insertFormatMarker(inputTextFieldValue, ">>", "")
                                }
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
                        }

                        // Preview bar
                        com.flasskdev.vibe.ui.components.InputPreviewBar(
                            inputText = inputTextFieldValue.text,
                            visible = showPreviewMode,
                            strings = strings
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            AnimatedVisibility(visible = !isRecording) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Attachment button (paperclip)
                                    IconButton(
                                        onClick = { showAttachmentMenu = true },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(bottom = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AttachFile,
                                            contentDescription = "Attach",
                                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    // Emoji / sticker / GIF button (single entry, tabbed panel)
                                    IconButton(
                                        onClick = {
                                            if (showEmojiPanel) {
                                                showEmojiPanel = false
                                            } else {
                                                keyboardController?.hide()
                                                showEmojiPanel = true
                                            }
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(bottom = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEmotions,
                                            contentDescription = "Emoji, stickers and GIFs",
                                            tint = if (showEmojiPanel) VibePrimary else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    // Preview toggle button (eye icon) - shown ONLY when formatting mode is active
                                    AnimatedVisibility(
                                        visible = showFormattingBar,
                                        enter = fadeIn() + expandHorizontally(),
                                        exit = fadeOut() + shrinkHorizontally()
                                    ) {
                                        IconButton(
                                            onClick = { showPreviewMode = !showPreviewMode },
                                            modifier = Modifier
                                                .size(40.dp)
                                                .padding(bottom = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (showPreviewMode) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = "Preview formatting",
                                                tint = if (showPreviewMode) VibePrimary else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            
                            if (isRecording) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(VibeError, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = String.format("%02d:%02d", (recordingDuration / 1000) / 60, (recordingDuration / 1000) % 60),
                                        color = VibeError,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    
                                    if (isLocked) {
                                        TextButton(onClick = {
                                            val file = recorderHelper.stopRecording()
                                            file?.delete()
                                            isLocked = false
                                            dragOffset = 0f
                                            dragOffsetY = 0f
                                        }) {
                                            Text("Отмена", color = VibeError, fontSize = 14.sp)
                                        }
                                    } else {
                                        Text(
                                            text = "< " + strings.logoutCancel, // Reusing string or just hardcode "Swipe to cancel" for now
                                            fontSize = 14.sp,
                                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            } else {
                                BasicTextField(
                                    value = inputTextFieldValue,
                                    onValueChange = { newValue ->
                                        if (newValue.text.length <= 2048) {
                                            inputTextFieldValue = newValue
                                            viewModel.onTextChanged(newValue.text)
                                        } else {
                                            val trimmed = newValue.text.take(2048)
                                            inputTextFieldValue = newValue.copy(text = trimmed, selection = androidx.compose.ui.text.TextRange(trimmed.length))
                                            viewModel.onTextChanged(trimmed)
                                        }
                                    },
                                    textStyle = TextStyle(
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                        fontSize = 16.sp,
                                        letterSpacing = (-0.2).sp
                                    ),
                                    cursorBrush = SolidColor(VibePrimary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .heightIn(min = 36.dp, max = 120.dp)
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    decorationBox = { innerTextField ->
                                        if (inputTextFieldValue.text.isEmpty()) {
                                            Text(
                                                text = strings.messagePlaceholder,
                                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                                fontSize = 16.sp,
                                                letterSpacing = (-0.2).sp
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))

                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .pointerInput(canSendTextOrPhoto) {
                                            awaitEachGesture {
                                                val down = awaitFirstDown(requireUnconsumed = false)
                                                
                                                if (canSendTextOrPhoto) {
                                                    // Simple tap → send text/photo/edit
                                                    down.consume()
                                                    if (editingMessage != null) {
                                                        viewModel.submitEditMessage(inputTextFieldValue.text)
                                                    } else if (pendingPhotos.isNotEmpty() || pendingFiles.isNotEmpty()) {
                                                        val allUris = pendingPhotos + pendingFiles
                                                        viewModel.sendPhotos(context, allUris, inputTextFieldValue.text)
                                                        pendingPhotos = emptyList()
                                                        pendingFiles = emptyList()
                                                    } else {
                                                        val text = inputTextFieldValue.text
                                                        if (text.isNotBlank()) {
                                                            viewModel.sendMessage(text)
                                                        }
                                                    }
                                                    inputTextFieldValue = TextFieldValue("")
                                                    viewModel.onTextChanged("")
                                                    return@awaitEachGesture
                                                }
                                                
                                                if (isLocked) {
                                                    // Locked mode: tap to send voice
                                                    down.consume()
                                                    val file = recorderHelper.stopRecording()
                                                    if (file != null && recordingDuration > 500) {
                                                        viewModel.sendVoiceMessage(context, file, recordingDuration)
                                                    } else {
                                                        file?.delete()
                                                    }
                                                    isLocked = false
                                                    dragOffset = 0f
                                                    dragOffsetY = 0f
                                                    return@awaitEachGesture
                                                }
                                                
                                                // Not canSend, not locked → long-press to record
                                                var longPressTriggered = false
                                                var cancelled = false
                                                val touchSlop = viewConfiguration.touchSlop
                                                
                                                val upEvent = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                                                    var currentEvent: androidx.compose.ui.input.pointer.PointerEvent
                                                    var distance = 0f
                                                    do {
                                                        currentEvent = awaitPointerEvent()
                                                        val ptr = currentEvent.changes.firstOrNull { it.id == down.id }
                                                        if (ptr != null) {
                                                            distance += ptr.positionChange().getDistance()
                                                        }
                                                        if (distance > touchSlop) {
                                                            return@withTimeoutOrNull currentEvent
                                                        }
                                                    } while (currentEvent.changes.any { it.pressed })
                                                    currentEvent
                                                }
                                                
                                                if (upEvent == null) {
                                                    // Timeout reached without lifting -> Long Press!
                                                    longPressTriggered = true
                                                    if (androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                                                        recorderHelper.startRecording()
                                                    } else {
                                                        recordAudioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                                                        return@awaitEachGesture
                                                    }
                                                    
                                                    // Now track drag until UP
                                                    while (true) {
                                                        val event = awaitPointerEvent()
                                                        val pointer = event.changes.firstOrNull { it.id == down.id } ?: break
                                                        
                                                        if (!pointer.pressed) {
                                                            pointer.consume()
                                                            break
                                                        }
                                                        
                                                        val posChange = pointer.positionChange()
                                                        dragOffset += posChange.x
                                                        dragOffsetY += posChange.y
                                                        
                                                        // Swipe left to cancel (> 100dp approx)
                                                        if (dragOffset < -300f) {
                                                            cancelled = true
                                                            val file = recorderHelper.stopRecording()
                                                            file?.delete()
                                                            dragOffset = 0f
                                                            dragOffsetY = 0f
                                                            break
                                                        }
                                                        
                                                        // Swipe UP to lock (> 80dp approx)
                                                        if (dragOffsetY < -200f && !isLocked) {
                                                            isLocked = true
                                                            dragOffset = 0f
                                                            dragOffsetY = 0f
                                                        }
                                                    }
                                                    
                                                    if (!cancelled && !isLocked) {
                                                        val file = recorderHelper.stopRecording()
                                                        if (file != null && recordingDuration > 500) {
                                                            viewModel.sendVoiceMessage(context, file, recordingDuration)
                                                        } else {
                                                            file?.delete()
                                                        }
                                                        dragOffset = 0f
                                                        dragOffsetY = 0f
                                                    }
                                                } else {
                                                    // Released before long press timeout -> do nothing
                                                }
                                            }
                                        }
                                        .clip(CircleShape)
                                        .background(VibePrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (canSendTextOrPhoto || isLocked) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                                        contentDescription = if (canSendTextOrPhoto || isLocked) strings.sendBtn else "Record Voice",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = strings.userRestrictedMessaging,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            }
        }
        
        // Emoji / Sticker / GIF panel is now inline (rendered in the Column above the input bar).

        // Attachment ModalBottomSheet (redesigned)
        if (showAttachmentMenu) {
            val attachSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { showAttachmentMenu = false },
                sheetState = attachSheetState,
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, top = 8.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = strings.attachTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable {
                                showAttachmentMenu = false
                                photoPickerLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(
                                        androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo
                                    )
                                )
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VibePrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = VibePrimary, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(strings.attachPhotoVideo, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                            Text("JPG, PNG, MP4, MOV...", fontSize = 12.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable {
                                showAttachmentMenu = false
                                documentPickerLauncher.launch(arrayOf("*/*"))
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VibeWarning.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = VibeWarning, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(strings.attachFile, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                            Text("PDF, DOC, ZIP...", fontSize = 12.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
        
        // Link insertion dialog
        if (showLinkInputDialog) {
            var linkText by remember(showLinkInputDialog) { mutableStateOf(linkInputInitialText) }
            var linkUrl by remember(showLinkInputDialog) { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showLinkInputDialog = false },
                title = { Text(strings.formatLink, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = linkText,
                            onValueChange = { linkText = it },
                            label = { Text("Текст ссылки") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibePrimary, cursorColor = VibePrimary)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = linkUrl,
                            onValueChange = { linkUrl = it },
                            label = { Text(strings.formatLinkUrlHint) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibePrimary, cursorColor = VibePrimary)
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (linkText.isNotBlank() && linkUrl.isNotBlank()) {
                            val start = minOf(linkInputSelection.start, linkInputSelection.end).coerceIn(0, inputTextFieldValue.text.length)
                            val end = maxOf(linkInputSelection.start, linkInputSelection.end).coerceIn(0, inputTextFieldValue.text.length)
                            val prefix = inputTextFieldValue.text.substring(0, start)
                            val suffix = inputTextFieldValue.text.substring(end)
                            val formattedLink = "[$linkText]($linkUrl)"
                            val newText = prefix + formattedLink + suffix
                            val newCursor = start + formattedLink.length
                            inputTextFieldValue = TextFieldValue(newText, TextRange(newCursor))
                        }
                        showLinkInputDialog = false
                    }) {
                        Text("OK", color = VibePrimary, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLinkInputDialog = false }) {
                        Text(strings.cancelBtn, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            )
        }

        // Color text dialog
        if (showColorInputDialog) {
            var colorHex by remember(showColorInputDialog) { mutableStateOf("#FF5733") }
            var colorText by remember(showColorInputDialog) { mutableStateOf(colorInputInitialText) }
            AlertDialog(
                onDismissRequest = { showColorInputDialog = false },
                title = { Text(strings.formatTextColor, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = colorText,
                            onValueChange = { colorText = it },
                            label = { Text("Текст") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibePrimary, cursorColor = VibePrimary)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = colorHex,
                            onValueChange = { colorHex = it },
                            label = { Text(strings.formatColorHint) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VibePrimary, cursorColor = VibePrimary)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val previewColor = try {
                            Color(android.graphics.Color.parseColor(colorHex))
                        } catch (_: Exception) { Color.Gray }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(strings.formatPreview + ": ", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            Box(modifier = Modifier.size(20.dp).clip(RoundedCornerShape(4.dp)).background(previewColor))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(colorText.ifEmpty { "Text" }, color = previewColor, fontWeight = FontWeight.SemiBold)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (colorText.isNotBlank() && colorHex.startsWith("#")) {
                            val start = minOf(colorInputSelection.start, colorInputSelection.end).coerceIn(0, inputTextFieldValue.text.length)
                            val end = maxOf(colorInputSelection.start, colorInputSelection.end).coerceIn(0, inputTextFieldValue.text.length)
                            val prefix = inputTextFieldValue.text.substring(0, start)
                            val suffix = inputTextFieldValue.text.substring(end)
                            val formattedColor = "{{$colorHex:$colorText}}"
                            val newText = prefix + formattedColor + suffix
                            val newCursor = start + formattedColor.length
                            inputTextFieldValue = TextFieldValue(newText, TextRange(newCursor))
                        }
                        showColorInputDialog = false
                    }) {
                        Text("OK", color = VibePrimary, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showColorInputDialog = false }) {
                        Text(strings.cancelBtn, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            )
        }

        if (showDeleteDialog) {
            val anyMine = selectedMessages.any { id -> messages.find { it.id == id }?.senderId == viewModel.myUserId }
            val allMine = selectedMessages.all { id -> messages.find { it.id == id }?.senderId == viewModel.myUserId }
            var deleteForEveryone by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = strings.deleteMessagesTitle, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(text = strings.deleteMessagesText(selectedMessages.size))
                        if (anyMine) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { deleteForEveryone = !deleteForEveryone }) {
                                Checkbox(checked = deleteForEveryone, onCheckedChange = { deleteForEveryone = it }, colors = CheckboxDefaults.colors(checkedColor = VibePrimary))
                                Spacer(modifier = Modifier.width(8.dp))
                                val textLabel = if (allMine) strings.deleteForEveryone(displayName) else strings.deleteForEveryoneAlsoMine(displayName)
                                Text(text = textLabel)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteMessages(selectedMessages.toList(), deleteForEveryone)
                        selectedMessages.clear()
                        showDeleteDialog = false
                    }) {
                        Text(strings.deleteBtn, color = VibeError, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(strings.cancelBtn, color = VibePrimary)
                    }
                },
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                textContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )
        }

        var forwardSearchQuery by remember { mutableStateOf("") }

        if (showForwardSheet) {
            val recentChats by viewModel.recentChats.collectAsState()
            ModalBottomSheet(
                onDismissRequest = { showForwardSheet = false },
                sheetState = forwardSheetState,
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, top = 8.dp)
                ) {
                    Text(
                        text = strings.forwardMessageTitle,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    androidx.compose.material3.OutlinedTextField(
                        value = forwardSearchQuery,
                        onValueChange = { forwardSearchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text(strings.searchPlaceholder) },
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VibePrimary,
                            unfocusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val filteredChats = if (forwardSearchQuery.isBlank()) {
                        recentChats
                    } else {
                        recentChats.filter {
                            val name = it.name ?: ""
                            val username = it.username ?: ""
                            name.contains(forwardSearchQuery, ignoreCase = true) || 
                            username.contains(forwardSearchQuery, ignoreCase = true)
                        }
                    }

                    if (filteredChats.isEmpty()) {
                        Text(
                            text = strings.noRecentChats,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(filteredChats) { chatUser ->
                                val chatDisplayName = when {
                                    chatUser.chat.isBanned -> strings.accountDeleted
                                    chatUser.chat.isFreezed -> strings.accountFrozen
                                    else -> chatUser.name ?: chatUser.username ?: strings.deletedAcc
                                }
                                val isBlocked = chatUser.chat.isBlockedByUser || chatUser.chat.isBanned || chatUser.chat.isFreezed
                                val isBlockedByMe = chatUser.chat.isBlockedByMe
                                val isOnline = chatUser.isOnline == true && !isBlocked && !isBlockedByMe && chatUser.isBot != true
                                val displayAvatarUrl = if (isBlocked) null else chatUser.avatarUrl

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.forwardMessages(chatUser.chat.interlocutorId, selectedMessages.toList())
                                            selectedMessages.clear()
                                            showForwardSheet = false
                                            forwardSearchQuery = ""
                                            scope.launch { forwardSheetState.hide() }
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!displayAvatarUrl.isNullOrBlank()) {
                                        coil.compose.AsyncImage(
                                            model = displayAvatarUrl,
                                            contentDescription = chatDisplayName,
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(VibePrimary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (chatDisplayName.isNotEmpty()) chatDisplayName.take(1).uppercase() else "",
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = chatDisplayName,
                                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f, fill = false)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            com.flasskdev.vibe.ui.components.UserBadgesRow(
                                                isVerified = chatUser.chat.isVerified,
                                                isDeveloper = chatUser.chat.isDeveloper,
                                                isBot = chatUser.isBot == true,
                                                isBanned = chatUser.chat.isBanned,
                                                isFreezed = chatUser.chat.isFreezed,
                                                badgeSize = 14.dp
                                            )
                                        }
                                        
                                        val statusText = if (isBlocked) {
                                            strings.lastSeenLongAgo
                                        } else if (isBlockedByMe) {
                                            "Заблокирован"
                                        } else if (chatUser.isBot == true) {
                                            strings.statusBot
                                        } else if (isOnline) {
                                            strings.statusOnline
                                        } else {
                                            formatLastSeen(chatUser.lastSeen, strings)
                                        }
                                        
                                        Text(
                                            text = statusText,
                                            color = if (isBlockedByMe) com.flasskdev.vibe.ui.theme.VibeError 
                                                else if (isOnline) com.flasskdev.vibe.ui.theme.VibeOnlineGreen 
                                                else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                            fontSize = 13.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    if (messageToPin != null) {
        AlertDialog(
            onDismissRequest = { messageToPin = null },
            title = { Text(strings.pinMessage ?: "Закрепить сообщение") },
            text = {
                Column {
                    Text(strings.pinMessageConfirm ?: "Вы действительно хотите закрепить это сообщение?")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { unpinForBoth = !unpinForBoth }) {
                        Checkbox(checked = unpinForBoth, onCheckedChange = { unpinForBoth = it })
                        Text(strings.forBoth(displayName) ?: "Также для $displayName")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.pinMessage(messageToPin!!.id, unpinForBoth)
                    messageToPin = null
                }) {
                    Text(strings.pin ?: "Закрепить")
                }
            },
            dismissButton = {
                TextButton(onClick = { messageToPin = null }) {
                    Text(strings.cancelBtn)
                }
            }
        )
    }

    if (messageToUnpin != null) {
        AlertDialog(
            onDismissRequest = { messageToUnpin = null },
            title = { Text(strings.unpinMessage ?: "Открепить сообщение") },
            text = {
                Column {
                    Text(strings.unpinMessageConfirm ?: "Вы действительно хотите открепить это сообщение?")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { unpinForBoth = !unpinForBoth }) {
                        Checkbox(checked = unpinForBoth, onCheckedChange = { unpinForBoth = it })
                        Text(strings.forBoth(displayName) ?: "Также для $displayName")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.unpinMessage(messageToUnpin!!.id, unpinForBoth)
                    messageToUnpin = null
                }) {
                    Text(strings.unpin ?: "Открепить")
                }
            },
            dismissButton = {
                TextButton(onClick = { messageToUnpin = null }) {
                    Text(strings.cancelBtn)
                }
            }
        )
    }

    if (showUnpinAllDialog) {
        AlertDialog(
            onDismissRequest = { showUnpinAllDialog = false },
            title = { Text(strings.unpinAll ?: "Открепить все") },
            text = {
                Column {
                    Text(strings.unpinAllConfirm ?: "Открепить все сообщения в этом чате?")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { unpinForBoth = !unpinForBoth }) {
                        Checkbox(checked = unpinForBoth, onCheckedChange = { unpinForBoth = it })
                        Text(strings.forBoth(displayName) ?: "Также для $displayName")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.unpinAllMessages(unpinForBoth)
                    showUnpinAllDialog = false
                    showPinnedMessagesModal = false
                }) {
                    Text(strings.unpin ?: "Открепить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnpinAllDialog = false }) {
                    Text(strings.cancelBtn)
                }
            }
        )
    }
    
    if (viewingMessage != null) {
        val attachments = viewingMessage!!.attachments ?: emptyList()
        if (attachments.isNotEmpty()) {
            val pagerState = androidx.compose.foundation.pager.rememberPagerState(
                initialPage = viewingPhotoIndex,
                pageCount = { attachments.size }
            )
            
            val msgTime = remember(viewingMessage!!.timestamp) {
                java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(viewingMessage!!.timestamp))
            }
            val senderN = if (viewingMessage!!.senderId == viewModel.myUserId) strings.you ?: "Вы" else displayName
            
            var isZoomed by remember { mutableStateOf(false) }

            androidx.compose.ui.window.Dialog(
                onDismissRequest = { viewingMessage = null },
                properties = androidx.compose.ui.window.DialogProperties(
                    usePlatformDefaultWidth = false,
                    decorFitsSystemWindows = false,
                    dismissOnBackPress = true
                )
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                    androidx.compose.foundation.pager.HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        modifier = Modifier.fillMaxSize(),
                        pageSpacing = 16.dp,
                        userScrollEnabled = !isZoomed
                    ) { page ->
                        var targetZoomScale by remember { mutableFloatStateOf(1f) }
                        var targetZoomOffsetX by remember { mutableFloatStateOf(0f) }
                        var targetZoomOffsetY by remember { mutableFloatStateOf(0f) }
                        var isPinching by remember { mutableStateOf(false) }

                        val animatedZoomScale by androidx.compose.animation.core.animateFloatAsState(
                            targetValue = targetZoomScale,
                            animationSpec = if (isPinching) androidx.compose.animation.core.snap() else androidx.compose.animation.core.spring()
                        )
                        val animatedZoomOffsetX by androidx.compose.animation.core.animateFloatAsState(
                            targetValue = targetZoomOffsetX,
                            animationSpec = if (isPinching) androidx.compose.animation.core.snap() else androidx.compose.animation.core.spring()
                        )
                        val animatedZoomOffsetY by androidx.compose.animation.core.animateFloatAsState(
                            targetValue = targetZoomOffsetY,
                            animationSpec = if (isPinching) androidx.compose.animation.core.snap() else androidx.compose.animation.core.spring()
                        )

                        LaunchedEffect(animatedZoomScale) {
                            if (page == pagerState.currentPage) {
                                isZoomed = animatedZoomScale > 1f
                            }
                        }

                        LaunchedEffect(pagerState.currentPage) {
                            if (page != pagerState.currentPage) {
                                targetZoomScale = 1f
                                targetZoomOffsetX = 0f
                                targetZoomOffsetY = 0f
                            }
                        }

                                                val attachmentPath = attachments[page]
                        val isVideo = com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(attachmentPath)
                        val isLocal = attachmentPath.startsWith("/") || attachmentPath.startsWith("content://") || attachmentPath.contains("cacheDir")

                        val model = if (isLocal) {
                            java.io.File(attachmentPath)
                        } else if (attachmentPath.startsWith("http")) {
                            attachmentPath
                        } else {
                            "https://flasskdev.alwaysdata.net/api/upload/file/$attachmentPath"
                        }
                        
                        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        val diff = 1f - kotlin.math.abs(pageOffset).coerceIn(0f, 1f)
                        val baseAlpha = 0.3f + 0.7f * diff
                        val baseScale = 0.85f + 0.15f * diff
                        
                        var componentSize by remember { mutableStateOf(androidx.compose.ui.unit.IntSize.Zero) }
                        
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .onSizeChanged { componentSize = it }
                                .graphicsLayer {
                                    this.alpha = baseAlpha
                                    if (page == pagerState.currentPage) {
                                        scaleX = baseScale * animatedZoomScale
                                        scaleY = baseScale * animatedZoomScale
                                        translationX = animatedZoomOffsetX
                                        translationY = animatedZoomOffsetY
                                    } else {
                                        scaleX = baseScale
                                        scaleY = baseScale
                                    }
                                }
                                .pointerInput(page == pagerState.currentPage) {
                                                                        if (!isVideo && page == pagerState.currentPage) {
                                        awaitEachGesture {

                                            awaitFirstDown(requireUnconsumed = false)
                                            isPinching = true
                                            do {
                                                val event = awaitPointerEvent()
                                                val zoom = event.calculateZoom()
                                                val pan = event.calculatePan()
                                                
                                                if (targetZoomScale > 1f || event.changes.size > 1) {
                                                    event.changes.forEach { it.consume() }
                                                    targetZoomScale = (targetZoomScale * zoom).coerceIn(1f, 5f)
                                                    
                                                    if (targetZoomScale > 1f) {
                                                        val maxX = (targetZoomScale - 1) * componentSize.width / 2
                                                        val maxY = (targetZoomScale - 1) * componentSize.height / 2
                                                        targetZoomOffsetX = (targetZoomOffsetX + pan.x * 2.5f).coerceIn(-maxX, maxX)
                                                        targetZoomOffsetY = (targetZoomOffsetY + pan.y * 2.5f).coerceIn(-maxY, maxY)
                                                    } else {
                                                        targetZoomOffsetX = 0f
                                                        targetZoomOffsetY = 0f
                                                    }
                                                }
                                            } while (event.changes.any { it.pressed })
                                            isPinching = false
                                        }
                                    }
                                }
                                .pointerInput(page == pagerState.currentPage) {
                                                                        if (!isVideo && page == pagerState.currentPage) {
                                        detectTapGestures(

                                            onDoubleTap = {
                                                isPinching = false
                                                if (targetZoomScale > 1f) {
                                                    targetZoomScale = 1f
                                                    targetZoomOffsetX = 0f
                                                    targetZoomOffsetY = 0f
                                                } else {
                                                    targetZoomScale = 2.5f
                                                }
                                            }
                                        )
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isVideo) {
                                com.flasskdev.vibe.ui.components.InlineVideoPlayer(
                                    attachmentPath = attachmentPath,
                                    onClose = { viewingMessage = null },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.8f)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            } else {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(model)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Photo view",
                                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f).clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Fit
                                )
                            }

                        }
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                            ))
                            .padding(top = 40.dp, bottom = 24.dp, start = 8.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewingMessage = null }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = senderN, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = msgTime, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        }
                    }
                    
                    if (viewingMessage!!.content.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                ))
                                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 48.dp)
                        ) {
                            if (com.flasskdev.vibe.utils.TextFormatting.hasFormatting(viewingMessage!!.content)) {
                                FormattedText(
                                    text = viewingMessage!!.content,
                                    baseColor = Color.White,
                                    fontSize = 15.sp,
                                    lineHeight = 20.sp,
                                    isMine = true
                                )
                            } else {
                                Text(
                                    text = viewingMessage!!.content,
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    }
    
    if (reportMessage != null) {
        ReportDialog(
            onDismiss = { reportMessage = null },
            onSubmit = { reason, comment ->
                webSocket.reportMessage(
                    theme = reason,
                    fromUser = viewModel.myUserId,
                    toUser = reportMessage!!.senderId,
                    messageId = reportMessage!!.id,
                    comment = comment
                )
                reportMessage = null
            }
        )
    }

    if (spamblockErrorMsg != null) {
        AlertDialog(
            onDismissRequest = { spamblockErrorMsg = null },
            title = { Text("Ограничение", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
            text = { Text(spamblockErrorMsg!!, color = MaterialTheme.colorScheme.onSurface) },
            confirmButton = {
                TextButton(onClick = { spamblockErrorMsg = null }) {
                    Text("Понятно", color = VibePrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    waitingForSpamInfo = true
                    webSocket.searchUsers("SpamInfo", viewModel.myUserId)
                }) {
                    Text("Почему?", color = VibePrimary)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        viewModel.jumpToDate(
                            targetTimestamp = selectedMillis,
                            messageList = messages,
                            onNotFound = {
                                triggerToast("Сообщений за эту дату не найдено")
                            }
                        )
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = VibePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = "Выберите дату",
                        modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface,
                    weekdayContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    subheadContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    yearContentColor = MaterialTheme.colorScheme.onSurface,
                    currentYearContentColor = VibePrimary,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = VibePrimary,
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = VibePrimary,
                    todayContentColor = VibePrimary,
                    todayDateBorderColor = VibePrimary
                )
            )
        }
    }

    val reactionSheetData by viewModel.reactionSheetState.collectAsState()
    if (reactionSheetData != null) {
        ReactionDetailsBottomSheet(
            sheetData = reactionSheetData!!,
            onSelectEmoji = { viewModel.selectReactionEmojiTab(it) },
            onLoadMore = { viewModel.loadMoreReactionUsers() },
            onDismiss = { viewModel.closeReactionDetails() },
            onProfileClick = onProfileClick
        )
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

    if (botAlertText != null) {
        AlertDialog(
            onDismissRequest = { botAlertText = null },
            title = {
                Text(
                    text = effectiveUser?.name ?: "Сообщение от бота",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = botAlertText!!,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { botAlertText = null }) {
                    Text("OK", color = VibePrimary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MessageBubble(
    modifier: Modifier = Modifier,
    message: MessageEntity,
    repliedMessage: MessageEntity? = null,
    isMine: Boolean, 
    isNewerSameSender: Boolean = false,
    isOlderSameSender: Boolean = false,
    strings: com.flasskdev.vibe.ui.theme.VibeStrings,
    isHighlighted: Boolean = false,
    onReply: (MessageEntity) -> Unit,
    onReplyClick: (Int) -> Unit,
    onEditClick: (MessageEntity) -> Unit,
    isPinned: Boolean = false,
    onPinRequest: (MessageEntity) -> Unit = {},
    onUnpinRequest: (MessageEntity) -> Unit = {},
    isSelected: Boolean = false,
    selectionMode: Boolean = false,
    onSelect: (() -> Unit)? = null,
    onProfileClick: (Int, String) -> Unit,
    onShowCopyToast: () -> Unit = {},
    onImageClick: (MessageEntity, Int) -> Unit = { _, _ -> },
    onReportClick: (MessageEntity) -> Unit = {},
    onRetryUpload: (Int) -> Unit = {},
        myAvatarUrl: String?,
    partnerAvatarUrl: String?,
    partnerName: String? = null,
    myDisplayName: String? = null,
    myUserId: Int = 0,

    onReactionToggle: (MessageEntity, String) -> Unit = { _, _ -> },
        onReactionLongClick: (MessageEntity, String) -> Unit = { _, _ -> },
    inlineKeyboardEnabled: Boolean = true,
    pendingInlineCallbackData: String? = null,
    onInlineButtonClick: (MessageEntity, com.flasskdev.vibe.data.local.InlineKeyboardButton) -> Unit = { _, _ -> },
    chatPlaylist: List<com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo> = emptyList(),

    liquidState: io.github.fletchmckee.liquid.LiquidState? = null
) {
    val audioPlayer = LocalGlobalAudioPlayer.current
    val currentPlayingTrack by audioPlayer.currentTrack.collectAsState()
    val isPlayingAudio by audioPlayer.isPlaying.collectAsState()
    val audioProgress by audioPlayer.progress.collectAsState()
    val audioCurrentPos by audioPlayer.currentPosition.collectAsState()

    val timeFormatted = remember(message.timestamp) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date(message.timestamp))
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "swipe")
    var showMenu by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val highlightAnim = remember { Animatable(0f) }
    LaunchedEffect(isHighlighted) {
        if (isHighlighted) {
            highlightAnim.snapTo(0f)
            highlightAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
            )
            kotlinx.coroutines.delay(500)
            highlightAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1100, easing = LinearOutSlowInEasing)
            )
        } else if (highlightAnim.value > 0f) {
            highlightAnim.snapTo(0f)
        }
    }

    val isVoiceMessage = message.content.startsWith("duration:")
    val isVideoMessage = message.content.startsWith("video_message:")
    val isSticker = message.content.startsWith("sticker:")
    val isGif = message.content.startsWith("gif:")

    Row(
                modifier = modifier
            .fillMaxWidth()
            .offset(x = animatedOffsetX.dp),

        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        val isDark = androidx.compose.material3.MaterialTheme.colorScheme.background == VibeBackgroundDark
        val myBubbleColor = if (isDark) VibeBubbleMine else VibeBubbleMineLight
        val theirBubbleColor = if (isDark) VibeBubbleTheirsDark else VibeBubbleTheirs
        val bubbleBg = if (isMine) myBubbleColor else theirBubbleColor

        val images = message.attachments?.filter { com.flasskdev.vibe.utils.AttachmentUtils.isImage(it) } ?: emptyList()
        val videos = message.attachments?.filter { com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(it) } ?: emptyList()
        val mediaAttachments = images + videos
        
        val files = message.attachments?.filter { 
            !com.flasskdev.vibe.utils.AttachmentUtils.isImage(it) && 
            !com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(it) && 
            !com.flasskdev.vibe.utils.AttachmentUtils.isPlayableAudio(it) 
        } ?: emptyList()
        
        val audios = message.attachments?.filter { com.flasskdev.vibe.utils.AttachmentUtils.isPlayableAudio(it) } ?: emptyList()
        
        val isOnlyImagesAndText = mediaAttachments.isNotEmpty() && files.isEmpty() && audios.isEmpty() && !isVoiceMessage && !isVideoMessage && message.replyToId == null && message.forwardedFromId == null

        val actualBubbleBg = if (isOnlyImagesAndText || isVideoMessage || isSticker || isGif) Color.Transparent else bubbleBg
        val paddingX = if (isOnlyImagesAndText || isVideoMessage || isSticker || isGif) 0.dp else 14.dp
        val paddingY = if (isOnlyImagesAndText || isVideoMessage || isSticker || isGif) 0.dp else 8.dp

        val bubbleShape = RoundedCornerShape(
            topStart = if (isMine) 20.dp else (if (isOlderSameSender) 4.dp else 20.dp),
            topEnd = if (!isMine) 20.dp else (if (isOlderSameSender) 4.dp else 20.dp),
            bottomStart = if (isMine) 20.dp else (if (isNewerSameSender) 4.dp else 20.dp),
            bottomEnd = if (!isMine) 20.dp else (if (isNewerSameSender) 4.dp else 20.dp)
        )

        val rowHighlightBg = if (isSelected) {
            VibePrimary.copy(alpha = 0.12f)
        } else if (highlightAnim.value > 0.005f) {
            VibePrimary.copy(alpha = highlightAnim.value * 0.22f)
        } else {
            Color.Transparent
        }

        val scale = 1f + (highlightAnim.value * 0.025f)
        val highlightOverlayColor = if (highlightAnim.value > 0.005f) {
            if (isMine) Color.White.copy(alpha = highlightAnim.value * 0.22f)
            else VibePrimary.copy(alpha = highlightAnim.value * 0.20f)
        } else {
            Color.Transparent
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(rowHighlightBg)
                .fillMaxWidth()
                .padding(horizontal = if (isSelected) 6.dp else 2.dp, vertical = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(if (isMine) Alignment.CenterEnd else Alignment.CenterStart)
                    .widthIn(min = 180.dp, max = 290.dp),
                horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
            ) {
                Box(
                                        modifier = Modifier
                        // Keep reply-swipe handling on the message body only. The old parent-level
                        // gesture detector consumed slight horizontal motion from inline button taps.
                        .pointerInput(message.id) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (offsetX > 60f || offsetX < -60f) onReply(message)
                                    offsetX = 0f
                                },
                                onDragCancel = { offsetX = 0f },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetX = (offsetX + dragAmount * 0.5f).coerceIn(-100f, 100f)
                                }
                            )
                        }
                        .graphicsLayer {

                            scaleX = scale
                            scaleY = scale
                        }
                        .widthIn(max = 280.dp)
                        .clip(bubbleShape)
                        .background(actualBubbleBg)
                        .border(
                            width = 1.5.dp,
                            color = if (highlightAnim.value > 0.005f) VibePrimary.copy(alpha = highlightAnim.value * 0.85f) else Color.Transparent,
                            shape = bubbleShape
                        )
                        .drawWithContent {
                            drawContent()
                            if (highlightAnim.value > 0.005f) {
                                drawRect(highlightOverlayColor)
                            }
                        }
                        .combinedClickable(
                            onLongClick = { 
                                if (!selectionMode) onSelect?.invoke() 
                            },
                            onClick = {
                                if (selectionMode) {
                                    onSelect?.invoke()
                                } else {
                                    showMenu = true
                                }
                            }
                        )
                        .padding(horizontal = paddingX, vertical = paddingY)
                ) {
                    Column {
                        if (message.replyToId != null) {
                    val replyBg = if (isMine) Color.White.copy(alpha = 0.15f) else androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    val barColor = if (isMine) Color.White else VibePrimary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(replyBg)
                            .clickable { onReplyClick(message.replyToId!!) }
                            .padding(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(34.dp)
                                .background(barColor, RoundedCornerShape(1.5.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                                                        val senderName = message.replyToSenderName
                                ?: repliedMessage?.let { sourceMessage ->
                                    when {
                                        sourceMessage.senderId == myUserId ->
                                            myDisplayName?.takeIf { it.isNotBlank() } ?: strings.you
                                        sourceMessage.senderType.equals("bot", ignoreCase = true) ->
                                            partnerName?.takeIf { it.isNotBlank() } ?: "Бот"
                                        else -> partnerName?.takeIf { it.isNotBlank() }
                                            ?: (strings.replyTo ?: "Ответ")
                                    }
                                }
                                ?: (strings.replyTo ?: "Ответ")

                            Text(
                                text = senderName, 
                                color = barColor, 
                                fontSize = 13.sp, 
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (repliedMessage != null) {
                                MessagePreviewBlock(
                                    message = repliedMessage,
                                    textColor = if (isMine) Color.White.copy(alpha = 0.95f) else androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp,
                                    isMine = isMine
                                )
                            } else if (message.replyToContent != null) {
                                val repText = com.flasskdev.vibe.utils.MessageUtils.formatMessagePreview(message.replyToContent, null).replace("\n", " ")
                                val repColor = if (isMine) Color.White.copy(alpha = 0.95f) else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                                if (com.flasskdev.vibe.utils.TextFormatting.hasFormatting(repText)) {
                                    FormattedText(
                                        text = repText,
                                        baseColor = repColor,
                                        fontSize = 13.sp,
                                        lineHeight = 16.sp,
                                        maxLines = 1,
                                        interactive = false,
                                        isMine = isMine
                                    )
                                } else {
                                    Text(
                                        text = repText, 
                                        color = repColor, 
                                        fontSize = 13.sp, 
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                if (message.forwardedFromId != null) {
                    val fwdId = message.forwardedFromId
                    val fwdName = message.forwardedFromName ?: "Пользователь"
                    val actualContent = message.content
                    
                    val replyBg = if (isMine) Color.White.copy(alpha = 0.15f) else androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    val barColor = if (isMine) Color.White else VibePrimary
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(replyBg)
                            .clickable { 
                                if (fwdId == -1) {
                                    onShowCopyToast()
                                } else {
                                    onProfileClick(fwdId, fwdName)
                                }
                            }
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Reply,
                            contentDescription = "Forwarded",
                            tint = barColor,
                            modifier = Modifier.size(16.dp).scale(scaleX = -1f, scaleY = 1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = strings.forwardedFrom(fwdName),
                            color = barColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                val contentToRender = if (message.forwardedFromId != null) message.content else message.content
                if (isSticker) {
                    com.flasskdev.vibe.ui.components.StickerMessage(
                        stickerId = com.flasskdev.vibe.ui.components.StickerRepository.idFromContent(message.content),
                        timeText = timeFormatted,
                        isMine = isMine,
                        isRead = message.isRead,
                        isPending = message.id < 0
                    )
                } else if (isGif) {
                    com.flasskdev.vibe.ui.components.GifMessage(
                        url = message.attachments?.firstOrNull() ?: "",
                        meta = message.content,
                        timeText = timeFormatted,
                        isMine = isMine,
                        isRead = message.isRead,
                        isPending = message.id < 0,
                        onClick = { onImageClick(message, 0) }
                    )
                } else if (isVideoMessage) {
                    val videoUrl = message.attachments?.firstOrNull() ?: ""
                    val ms = message.content.substringAfter("video_message:").toLongOrNull() ?: 0L
                    val totalSec = ms / 1000
                    val durationString = String.format("%d:%02d", totalSec / 60, totalSec % 60)
                    com.flasskdev.vibe.ui.components.VideoMessageBubble(
                        videoUrl = videoUrl,
                        durationFormatted = durationString,
                        onClick = { onImageClick(message, 0) }
                    )
                } else if (isVoiceMessage) {
                    val audioUrl = message.attachments?.firstOrNull() ?: ""
                    val isThisPlaying = currentPlayingTrack?.id == message.id.toString()
                    val durationString = if (message.content.startsWith("duration:")) {
                        val ms = message.content.substringAfter("duration:").toLongOrNull() ?: 0L
                        val totalSec = ms / 1000
                        String.format("%d:%02d", totalSec / 60, totalSec % 60)
                    } else {
                        "Голосовое"
                    }
                    val formattedPos = if (isThisPlaying && isPlayingAudio) {
                        val sec = audioCurrentPos / 1000
                        String.format("%d:%02d", sec / 60, sec % 60)
                    } else {
                        durationString
                    }
                    VoiceMessageBubble(
                        isPlaying = isThisPlaying && isPlayingAudio,
                        progress = if (isThisPlaying) audioProgress else 0f,
                        isMine = isMine,
                        messageId = message.id,
                        durationFormatted = formattedPos,
                        onPlayClick = {
                            audioPlayer.playAudio(
                                AudioTrackInfo(
                                    id = message.id.toString(),
                                    url = audioUrl,
                                    title = if (isMine) "Вы (Голосовое)" else "Голосовое сообщение",
                                    avatarUrl = if (isMine) myAvatarUrl else partnerAvatarUrl
                                )
                            )
                        }
                    )
                } else {
                    if (mediaAttachments.isNotEmpty()) {
                        MessageAttachmentsGrid(attachments = mediaAttachments, onImageClick = { idx -> onImageClick(message, idx) })
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    if (audios.isNotEmpty()) {
                        MessageAudioList(
                            audios = audios,
                            message = message,
                            audioPlayer = audioPlayer,
                            currentPlayingTrack = currentPlayingTrack,
                            isPlayingAudio = isPlayingAudio,
                            isMine = isMine,
                            bubbleBg = bubbleBg,
                            myAvatarUrl = myAvatarUrl,
                            partnerAvatarUrl = partnerAvatarUrl,
                            chatPlaylist = chatPlaylist
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    if (files.isNotEmpty()) {
                        MessageFilesList(files = files, context = context, isMine = isMine, bubbleBg = bubbleBg)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    
                    val innerModifier = if (isOnlyImagesAndText) {
                        Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bubbleBg)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    } else {
                        Modifier
                    }
                    
                    Box(modifier = innerModifier) {
                        Column {
                            val plainContent = if (message.content.startsWith("duration:") || message.content.startsWith("video_message:")) "" else contentToRender
                            if (plainContent.isNotEmpty()) {
                                if (TextFormatting.hasFormatting(plainContent)) {
                                    FormattedText(
                                        text = plainContent,
                                        baseColor = if (isMine || isOnlyImagesAndText) Color.White else androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                        fontSize = 15.sp,
                                        lineHeight = 20.sp,
                                        onMentionClick = { username ->
                                            // Could navigate to user profile by username
                                        },
                                        onProfileClick = onProfileClick,
                                        isMine = isMine
                                    )
                                } else {
                                    Text(
                                        text = plainContent,
                                        color = if (isMine || isOnlyImagesAndText) Color.White else androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                        fontSize = 15.sp,
                                        lineHeight = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                            Row(
                                modifier = Modifier.align(Alignment.End),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = timeFormatted + if (message.isEdited) strings.editedLabel else "",
                                    color = if (isMine || isOnlyImagesAndText) Color.White.copy(alpha = 0.7f) else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                    fontSize = 11.sp
                                )
                                if (isPinned) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Filled.PushPin,
                                        contentDescription = "Pinned",
                                        tint = if (isMine || isOnlyImagesAndText) Color.White.copy(alpha = 0.8f) else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                                if (isMine) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    if (message.uploadStatus == "UPLOADING") {
                                        Text(
                                            text = "${message.uploadProgress ?: 0}%",
                                            color = Color.White.copy(alpha = 0.7f),
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                        androidx.compose.material3.CircularProgressIndicator(
                                            progress = { (message.uploadProgress ?: 0) / 100f },
                                            modifier = Modifier.size(12.dp),
                                            color = Color.White,
                                            trackColor = Color.White.copy(alpha = 0.3f),
                                            strokeWidth = 2.dp
                                        )
                                    } else if (message.uploadStatus == "FAILED") {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = "Retry",
                                            tint = VibeError,
                                            modifier = Modifier.size(14.dp).clickable { onRetryUpload(message.id) }
                                        )
                                    } else if (message.id < 0) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = "Pending",
                                            tint = Color.White.copy(alpha = 0.7f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = if (message.isRead) Icons.Default.DoneAll else Icons.Default.Check,
                                            contentDescription = "Read status",
                                            tint = if (message.isRead) Color(0xFF81D4FA) else Color.White.copy(alpha = 0.7f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (!message.reactions.isNullOrEmpty()) {
                    MessageReactionsRow(
                        reactions = message.reactions!!,
                        myUserId = myUserId,
                        isMine = isMine,
                        onReactionClick = { emoji -> onReactionToggle(message, emoji) },
                        onReactionLongClick = { emoji -> onReactionLongClick(message, emoji) }
                    )
                }
            }
        }

        if (message.replyMarkup != null && message.replyMarkup.inlineKeyboard.isNotEmpty()) {
            Spacer(modifier = Modifier.height(5.dp))
            com.flasskdev.vibe.ui.components.InlineKeyboard(
                replyMarkup = message.replyMarkup,
                                onButtonClick = { btn -> onInlineButtonClick(message, btn) },
                liquidState = liquidState,
                isInteractionEnabled = inlineKeyboardEnabled,
                pendingCallbackData = pendingInlineCallbackData,
                modifier = Modifier.fillMaxWidth()

            )
        }
    }
}
}
            
if (showMenu) {
                ModalBottomSheet(
                    onDismissRequest = { showMenu = false },
                    sheetState = sheetState,
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp, top = 8.dp, start = 16.dp, end = 16.dp)
                    ) {
                        QuickReactionsBar(
                            currentReactions = message.reactions ?: emptyList(),
                            myUserId = myUserId,
                            onSelectEmoji = { emoji ->
                                showMenu = false
                                onReactionToggle(message, emoji)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val hasTextContent = !isVoiceMessage && !isVideoMessage &&
                            message.content.isNotBlank() &&
                            !message.content.startsWith("duration:") &&
                            !message.content.startsWith("video_message:")
                        
                        val canEdit = isMine && 
                            (System.currentTimeMillis() - message.timestamp <= 24 * 60 * 60 * 1000) &&
                            !isVoiceMessage && !isVideoMessage &&
                            (hasTextContent || (!message.attachments.isNullOrEmpty() && message.content.isNotBlank() && !message.content.startsWith("duration:") && !message.content.startsWith("video_message:")))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .clickable {
                                    showMenu = false
                                    onReply(message)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = null, tint = VibePrimary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(strings.replyTo ?: "Ответить", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                        }
                        
                        if (hasTextContent) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable {
                                        showMenu = false
                                        val plainText = com.flasskdev.vibe.utils.TextFormatting.stripFormatting(message.content)
                                        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                        clipboard.setPrimaryClip(android.content.ClipData.newPlainText("message", plainText))
                                        onShowCopyToast()
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = VibePrimary)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(strings.actionCopy, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        
                        if (canEdit) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable {
                                        showMenu = false
                                        onEditClick(message)
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = VibePrimary)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(strings.edit ?: "Изменить", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .clickable {
                                    showMenu = false
                                    if (isPinned) onUnpinRequest(message) else onPinRequest(message)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(if (isPinned) Icons.Outlined.PushPin else Icons.Default.PushPin, contentDescription = null, tint = VibePrimary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(if (isPinned) (strings.unpinMessage ?: "Открепить") else (strings.pinMessage ?: "Закрепить"), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                        }

                        if (!message.attachments.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable {
                                        showMenu = false
                                        val atts = message.attachments!!
                                        val filesToDownload = atts.map { att ->
                                            val url = com.flasskdev.vibe.utils.DownloadHelper.resolveUrl(att)
                                            val filename = com.flasskdev.vibe.utils.AttachmentUtils.getFilename(att)
                                            url to filename
                                        }
                                        if (filesToDownload.size == 1) {
                                            com.flasskdev.vibe.utils.DownloadHelper.downloadFile(
                                                context, filesToDownload[0].first, filesToDownload[0].second
                                            )
                                        } else {
                                            com.flasskdev.vibe.utils.DownloadHelper.downloadFiles(context, filesToDownload)
                                        }
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = VibePrimary)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = if (message.attachments!!.size > 1) strings.actionDownloadSelected else strings.actionDownload,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        if (!isMine) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable {
                                        showMenu = false
                                        onReportClick(message)
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red.copy(alpha = 0.8f))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(strings.actionReport, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Red.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }
}

@Composable
fun DateSeparator(
    modifier: Modifier = Modifier,
    dateMillis: Long,
    strings: com.flasskdev.vibe.ui.theme.VibeStrings
) {
    val dateText = remember(dateMillis) {
        val now = Calendar.getInstance()
        val msgDate = Calendar.getInstance().apply { timeInMillis = dateMillis }
        
        when {
            now.get(Calendar.YEAR) == msgDate.get(Calendar.YEAR) &&
                    now.get(Calendar.DAY_OF_YEAR) == msgDate.get(Calendar.DAY_OF_YEAR) -> strings.dateToday
            
            now.get(Calendar.YEAR) == msgDate.get(Calendar.YEAR) &&
                    now.get(Calendar.DAY_OF_YEAR) - msgDate.get(Calendar.DAY_OF_YEAR) == 1 -> strings.dateYesterday
            
            else -> {
                val pattern = if (now.get(Calendar.YEAR) == msgDate.get(Calendar.YEAR)) {
                    "d MMMM"
                } else {
                    "d MMMM yyyy"
                }
                val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                sdf.format(Date(dateMillis))
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            shape = RoundedCornerShape(14.dp),
            tonalElevation = 0.dp
        ) {
            Text(
                text = dateText,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}



private fun formatLastSeen(lastSeenTimestamp: Long?, strings: com.flasskdev.vibe.ui.theme.VibeStrings): String {
    if (lastSeenTimestamp == null) return strings.lastSeenRecently

    val now = Calendar.getInstance()
    val lastSeen = Calendar.getInstance().apply { timeInMillis = lastSeenTimestamp }

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeStr = timeFormat.format(Date(lastSeenTimestamp))

    return when {
        // Сегодня
        now.get(Calendar.YEAR) == lastSeen.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == lastSeen.get(Calendar.DAY_OF_YEAR) -> {
            strings.lastSeenToday(timeStr)
        }
        // Вчера
        now.get(Calendar.YEAR) == lastSeen.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - lastSeen.get(Calendar.DAY_OF_YEAR) == 1 -> {
            strings.lastSeenYesterday(timeStr)
        }
        // Больше года назад
        now.get(Calendar.YEAR) > lastSeen.get(Calendar.YEAR) -> {
            strings.lastSeenLongAgo
        }
        // В остальных случаях (позавчера и далее в пределах года)
        else -> {
            val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
            strings.lastSeenDate(dateFormat.format(Date(lastSeenTimestamp)), timeStr)
        }
    }
}

@Composable
fun MessageAttachmentsGrid(attachments: List<String>, onImageClick: (Int) -> Unit) {
    val itemsPerRow = if (attachments.size == 1) 1 else if (attachments.size in 2..4) 2 else 3
    val spacing = 4.dp
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing),
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
    ) {
        val rows = attachments.chunked(itemsPerRow)
        var globalIndex = 0
        rows.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { attachmentPath ->
                    val currentIndex = globalIndex++
                    val isLocal = attachmentPath.startsWith("/") || attachmentPath.startsWith("content://") || attachmentPath.contains("cacheDir")
                    val model = if (isLocal) {
                        java.io.File(attachmentPath)
                    } else if (attachmentPath.startsWith("http")) {
                        attachmentPath
                    } else {
                        "https://flasskdev.alwaysdata.net/api/upload/file/$attachmentPath"
                    }
                                        val isVideo = com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(attachmentPath)

                    Box(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isVideo) androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                        .clickable { onImageClick(currentIndex) }
                    ) {
                                                if (isVideo) {
                            com.flasskdev.vibe.ui.components.VideoCover(
                                source = model,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            AsyncImage(
                                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                    .data(model)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Вложение",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    }
                }
                // Fill empty slots in row
                val emptySlots = itemsPerRow - rowItems.size
                for (i in 0 until emptySlots) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MessageAudioList(
    audios: List<String>, 
    message: MessageEntity,
    audioPlayer: com.flasskdev.vibe.ui.viewmodels.GlobalAudioPlayerViewModel,
    currentPlayingTrack: com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo?,
    isPlayingAudio: Boolean,
    isMine: Boolean, 
    bubbleBg: Color,
    myAvatarUrl: String?,
    partnerAvatarUrl: String?,
    chatPlaylist: List<com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo> = emptyList()
) {
    val audioProgress by audioPlayer.progress.collectAsState()
    val audioCurrentPos by audioPlayer.currentPosition.collectAsState()
    val audioDuration by audioPlayer.duration.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        audios.forEach { attachmentPath ->
            val isLocal = attachmentPath.startsWith("/") || attachmentPath.startsWith("content://") || attachmentPath.contains("cacheDir")
            val audioUrl = if (isLocal) attachmentPath else if (attachmentPath.startsWith("http")) attachmentPath else "https://flasskdev.alwaysdata.net/api/upload/file/$attachmentPath"
            val trackId = "${message.id}_${attachmentPath.hashCode()}"

            val initialMeta = androidx.compose.runtime.remember(audioUrl) {
                com.flasskdev.vibe.utils.AudioMetadataHelper.getCachedMetadata(audioUrl)
            }
            var audioMeta by androidx.compose.runtime.remember(audioUrl) { 
                androidx.compose.runtime.mutableStateOf(initialMeta) 
            }
            androidx.compose.runtime.LaunchedEffect(audioUrl) {
                val meta = com.flasskdev.vibe.utils.AudioMetadataHelper.getMetadata(audioUrl)
                audioMeta = meta
                val tTitle = if (meta.displayArtist.isNotBlank()) "${meta.displayArtist} — ${meta.displayTitle}" else meta.displayTitle
                audioPlayer.updateTrackTitle(trackId, tTitle)
            }
            
            val displayTitle = audioMeta?.displayTitle ?: "Аудиозапись"
            val displayArtist = audioMeta?.displayArtist?.takeIf { it.isNotBlank() } ?: ""
            val displayDuration = audioMeta?.durationMs ?: 0L
            
            val isThisPlaying = currentPlayingTrack?.id == trackId
            val textColor = if (isMine) Color.White else androidx.compose.material3.MaterialTheme.colorScheme.onBackground
            val subColor = textColor.copy(alpha = 0.55f)

            // Play/Pause row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        if (isThisPlaying) {
                            if (isPlayingAudio) audioPlayer.pause() else audioPlayer.resume()
                        } else {
                            val trackTitle = if (displayArtist.isNotBlank()) "$displayArtist — $displayTitle" else displayTitle
                            val currentTrackInfo = com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo(
                                id = trackId,
                                url = audioUrl,
                                title = trackTitle,
                                avatarUrl = if (isMine) myAvatarUrl else partnerAvatarUrl
                            )
                            val finalPlaylist = if (chatPlaylist.isNotEmpty()) {
                                chatPlaylist.map { if (it.id == trackId) currentTrackInfo else it }
                            } else {
                                listOf(currentTrackInfo)
                            }
                            audioPlayer.playAudio(currentTrackInfo, finalPlaylist)
                        }
                    }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album art / Play icon
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    val coverArtBytes = audioMeta?.coverArt
                    if (coverArtBytes != null) {
                        val bitmap = androidx.compose.runtime.remember(coverArtBytes) {
                            android.graphics.BitmapFactory.decodeByteArray(coverArtBytes, 0, coverArtBytes.size)
                        }
                        if (bitmap != null) {
                            androidx.compose.foundation.Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Cover",
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp))
                            )
                            // Overlay play/pause
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.35f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isThisPlaying && isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize().background(VibePrimary, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isThisPlaying && isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = displayTitle,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp
                    )
                    if (isThisPlaying && isPlayingAudio) {
                        // Progress bar while playing
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(subColor.copy(alpha = 0.2f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(fraction = audioProgress.coerceIn(0f, 1f))
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(VibePrimary)
                            )
                        }
                    } else {
                        Text(
                            text = displayArtist,
                            color = subColor,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    // Timer
                    val posStr = if (isThisPlaying) {
                        val sec = audioCurrentPos / 1000
                        String.format("%d:%02d", sec / 60, sec % 60)
                    } else "0:00"
                    
                    val durToUse = if (audioDuration > 0) audioDuration else displayDuration
                    val durStr = if (durToUse > 0) {
                        val sec = durToUse / 1000
                        String.format("%d:%02d", sec / 60, sec % 60)
                    } else {
                        "--:--"
                    }
                    Text(
                        text = if (isThisPlaying) "$posStr / $durStr" else durStr,
                        color = subColor,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MessageFilesList(files: List<String>, context: android.content.Context, isMine: Boolean, bubbleBg: androidx.compose.ui.graphics.Color) {
    val textColor = if (isMine) Color.White else androidx.compose.material3.MaterialTheme.colorScheme.onBackground
    val subColor = textColor.copy(alpha = 0.5f)

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        files.forEach { attachmentPath ->
            val isLocal = attachmentPath.startsWith("/") || attachmentPath.startsWith("content://") || attachmentPath.contains("cacheDir")
            val fullFilename = if (isLocal) java.io.File(attachmentPath).name else attachmentPath.substringAfterLast("/")
            val nameWithoutExt = fullFilename.substringBeforeLast(".")
            val extension = "." + fullFilename.substringAfterLast(".", "")
            val downloadUrl = if (isLocal) attachmentPath else if (attachmentPath.startsWith("http")) attachmentPath else "https://flasskdev.alwaysdata.net/api/upload/file/$attachmentPath"

            var fileSizeText by androidx.compose.runtime.remember(downloadUrl) { androidx.compose.runtime.mutableStateOf("Загрузка...") }
            androidx.compose.runtime.LaunchedEffect(downloadUrl) {
                fileSizeText = com.flasskdev.vibe.utils.AttachmentUtils.getFileSizeAsync(downloadUrl)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isMine) Color.White.copy(alpha = 0.12f) else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // File icon
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(VibePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = nameWithoutExt.ifBlank { "File" },
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = extension,
                            color = subColor,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier.size(3.dp).background(subColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = fileSizeText,
                            color = subColor,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                // Native download button
                IconButton(onClick = {
                    com.flasskdev.vibe.utils.DownloadHelper.downloadFile(context, downloadUrl, fullFilename)
                }) {
                    Icon(Icons.Default.Download, contentDescription = "Download", tint = VibePrimary)
                }
            }
        }
    }
}


@Composable
fun MessagePreviewBlock(
    message: com.flasskdev.vibe.data.local.MessageEntity,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    isMine: Boolean = false
) {
    val rawPreview = com.flasskdev.vibe.utils.MessageUtils.formatMessagePreview(message.content, message.attachments)
    val isAudio = message.attachments?.any { com.flasskdev.vibe.utils.AttachmentUtils.isPlayableAudio(it) } == true
    val isVideo = message.attachments?.any { com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(it) } == true
    val isImage = message.attachments?.any { com.flasskdev.vibe.utils.AttachmentUtils.isImage(it) } == true
    val isFile = message.attachments?.any { !com.flasskdev.vibe.utils.AttachmentUtils.isImage(it) && !com.flasskdev.vibe.utils.AttachmentUtils.isPlayableVideo(it) && !com.flasskdev.vibe.utils.AttachmentUtils.isPlayableAudio(it) } == true
    val isVoice = message.content.startsWith("duration:")
    val isVideoMsg = message.content.startsWith("video_message:")

    val icon = when {
        isVideoMsg -> androidx.compose.material.icons.Icons.Default.Videocam
        isVoice -> androidx.compose.material.icons.Icons.Default.Mic
        isImage -> androidx.compose.material.icons.Icons.Default.Image
        isVideo -> androidx.compose.material.icons.Icons.Default.Videocam
        isAudio -> androidx.compose.material.icons.Icons.Default.MusicNote
        isFile -> androidx.compose.material.icons.Icons.Default.InsertDriveFile
        else -> null
    }

    var dynamicAudioTitle by androidx.compose.runtime.remember(message.attachments) { androidx.compose.runtime.mutableStateOf<String?>(null) }
    
    androidx.compose.runtime.LaunchedEffect(message.attachments) {
        if (isAudio && !message.attachments.isNullOrEmpty()) {
            val firstAtt = message.attachments.first { com.flasskdev.vibe.utils.AttachmentUtils.isPlayableAudio(it) }
            val isLocal = firstAtt.startsWith("/") || firstAtt.startsWith("content://") || firstAtt.contains("cacheDir")
            val url = if (isLocal) firstAtt else if (firstAtt.startsWith("http")) firstAtt else "https://flasskdev.alwaysdata.net/api/upload/file/$firstAtt"
            val meta = com.flasskdev.vibe.utils.AudioMetadataHelper.getMetadata(url)
            dynamicAudioTitle = if (meta.displayArtist != "Unknown Artist") "${meta.displayArtist} — ${meta.displayTitle}" else meta.displayTitle
        }
    }

    val cleanText = rawPreview.removePrefix("\uD83D\uDCF9 ").removePrefix("\uD83C\uDFA4 ").removePrefix("\uD83D\uDDBC ").removePrefix("\uD83C\uDFAC ").removePrefix("\uD83C\uDFB5 ").removePrefix("\uD83D\uDCCE ").removePrefix("+")
    
    val displayText = when {
        isAudio && (message.content.isBlank() || message.content.startsWith("Музыка")) -> dynamicAudioTitle ?: "Музыка..."
        else -> cleanText.replace("\n", " ")
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = VibePrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (com.flasskdev.vibe.utils.TextFormatting.hasFormatting(displayText)) {
            FormattedText(
                text = displayText,
                baseColor = textColor,
                fontSize = fontSize,
                lineHeight = (fontSize.value + 4).sp,
                maxLines = 1,
                interactive = false,
                isMine = isMine
            )
        } else {
            Text(
                text = displayText,
                color = textColor,
                fontSize = fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
private fun FormatButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Icon(
            imageVector = icon,
            contentDescription = null,
            tint = VibePrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun insertFormatMarker(tfv: TextFieldValue, prefix: String, suffix: String): TextFieldValue {
    val selStart = tfv.selection.min
    val selEnd = tfv.selection.max
    
    if (selStart != selEnd) {
        // Text is selected — wrap the selection with format markers
        val before = tfv.text.substring(0, selStart)
        val selected = tfv.text.substring(selStart, selEnd)
        val after = tfv.text.substring(selEnd)
        val newText = before + prefix + selected + suffix + after
        val newCursorPos = selStart + prefix.length + selected.length + suffix.length
        return TextFieldValue(newText, TextRange(newCursorPos))
    } else {
        // No selection — insert markers at cursor position
        val before = tfv.text.substring(0, selStart)
        val after = tfv.text.substring(selStart)
        val newText = before + prefix + suffix + after
        val newCursorPos = selStart + prefix.length
        return TextFieldValue(newText, TextRange(newCursorPos))
    }
}

@Composable
fun QuickReactionsBar(
    currentReactions: List<com.flasskdev.vibe.data.local.ReactionItem>,
    myUserId: Int,
    onSelectEmoji: (String) -> Unit
) {
    val emojis = remember {
        listOf(
            "❤️", "👍", "👎", "🔥", "😂", "🥰", "😮", "😢", "😭", "🙏",
            "👏", "🎉", "🤩", "🤔", "🤬", "🤯", "💩", "👀", "💯", "😍",
            "🥳", "😱", "🤝", "😴", "😎", "🫡", "💔", "⚡", "✨", "🕊️",
            "🍓", "🍾", "🏆", "😇", "🤡", "👾"
        )
    }
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val myReactionEmoji = remember(currentReactions, myUserId) {
        currentReactions.find { it.userIds.contains(myUserId) }?.emoji
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f))
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        emojis.forEach { emoji ->
            val isSelected = emoji == myReactionEmoji
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.22f else 1.0f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "emoji_scale"
            )

            Box(
                modifier = Modifier
                    .scale(scale)
                    .clip(CircleShape)
                    .background(if (isSelected) VibePrimary.copy(alpha = 0.22f) else Color.Transparent)
                    .border(
                        width = if (isSelected) 1.5.dp else 0.dp,
                        color = if (isSelected) VibePrimary else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onSelectEmoji(emoji)
                    }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 22.sp
                )
            }
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MessageReactionsRow(
    reactions: List<com.flasskdev.vibe.data.local.ReactionItem>,
    myUserId: Int,
    isMine: Boolean,
    onReactionClick: (String) -> Unit,
    onReactionLongClick: (String) -> Unit = {}
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        reactions.forEach { reaction ->
            val isSelected = reaction.userIds.contains(myUserId)
            val chipBg = if (isSelected) {
                if (isMine) Color.White.copy(alpha = 0.28f) else VibePrimary.copy(alpha = 0.18f)
            } else {
                if (isMine) Color.White.copy(alpha = 0.14f) else androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            }

            val borderColor = if (isSelected) {
                if (isMine) Color.White.copy(alpha = 0.6f) else VibePrimary.copy(alpha = 0.5f)
            } else Color.Transparent

            val textColor = if (isMine) Color.White else androidx.compose.material3.MaterialTheme.colorScheme.onSurface

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(chipBg)
                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                    .combinedClickable(
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onReactionClick(reaction.emoji)
                        },
                        onLongClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onReactionLongClick(reaction.emoji)
                        }
                    )
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = reaction.emoji, fontSize = 13.sp)
                if (reaction.count > 1) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${reaction.count}",
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ReactionDetailsBottomSheet(
    sheetData: com.flasskdev.vibe.ui.viewmodels.ReactionSheetData,
    onSelectEmoji: (String?) -> Unit,
    onLoadMore: () -> Unit,
    onDismiss: () -> Unit,
    onProfileClick: (Int, String) -> Unit
) {
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            totalItems > 0 && lastVisibleIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && sheetData.hasMore && !sheetData.isLoading) {
            onLoadMore()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalSheetState,
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Реакции",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Emoji Filter Tabs
            val allReactions = sheetData.message.reactions ?: emptyList()
            val totalCount = allReactions.sumOf { it.count }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isAllSelected = sheetData.selectedEmoji == null
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isAllSelected) VibePrimary else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        .clickable { onSelectEmoji(null) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Все $totalCount",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isAllSelected) Color.White else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                }

                allReactions.forEach { r ->
                    val isSelected = sheetData.selectedEmoji == r.emoji
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) VibePrimary else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .clickable { onSelectEmoji(r.emoji) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = r.emoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${r.count}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 6.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            )

            // Stable height container to prevent bottom sheet jumping
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                if (sheetData.users.isEmpty() && sheetData.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = VibePrimary,
                            strokeWidth = 2.5.dp
                        )
                    }
                } else if (sheetData.users.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пока нет реакций",
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(sheetData.users, key = { "${it.userId}_${it.emoji}" }) { user ->
                            ReactionUserItem(
                                user = user,
                                onProfileClick = onProfileClick
                            )
                        }

                        if (sheetData.isLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = VibePrimary,
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReactionUserItem(
    user: com.flasskdev.vibe.data.ReactionUserDetail,
    onProfileClick: (Int, String) -> Unit
) {
    val timeFormatted = remember(user.timestamp) {
        if (user.timestamp <= 0L) "" else {
            val sdf = SimpleDateFormat("d MMM, HH:mm", Locale.getDefault())
            sdf.format(Date(user.timestamp))
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProfileClick(user.userId, user.username ?: "") }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(VibePrimary),
            contentAlignment = Alignment.Center
        ) {
            if (!user.avatarUrl.isNullOrEmpty()) {
                coil.compose.AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                val initial = (user.name?.take(1) ?: user.username?.take(1) ?: "?").uppercase()
                Text(
                    text = initial,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Name and username
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user.name ?: user.username ?: "Пользователь",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (user.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Verified",
                        tint = VibePrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            if (!user.username.isNullOrEmpty()) {
                Text(
                    text = "@${user.username}",
                    fontSize = 13.sp,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right side: Emoji + Date
        Column(horizontalAlignment = Alignment.End) {
            Text(text = user.emoji, fontSize = 20.sp)
            if (timeFormatted.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = timeFormatted,
                    fontSize = 11.sp,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                )
            }
        }
    }
}

fun buildChatAudioPlaylist(
    messages: List<com.flasskdev.vibe.data.local.MessageEntity>,
    myAvatarUrl: String?,
    partnerAvatarUrl: String?,
    myUserId: Int
): List<com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo> {
    val list = mutableListOf<com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo>()
    // Oldest to newest
    messages.sortedBy { it.timestamp }.forEach { msg ->
        val isVoice = msg.content.startsWith("duration:")
        if (!isVoice) {
            val atts = msg.attachments ?: emptyList()
            val audios = atts.filter { 
                com.flasskdev.vibe.utils.AttachmentUtils.getType(it, msg.content) == com.flasskdev.vibe.utils.AttachmentType.AUDIO 
            }
            audios.forEach { attPath ->
                val isLocal = attPath.startsWith("/") || attPath.startsWith("content://") || attPath.contains("cacheDir")
                val audioUrl = if (isLocal) attPath else if (attPath.startsWith("http")) attPath else "https://flasskdev.alwaysdata.net/api/upload/file/$attPath"
                val meta = com.flasskdev.vibe.utils.AudioMetadataHelper.getCachedMetadata(audioUrl)
                val trackId = "${msg.id}_${attPath.hashCode()}"
                val trackTitle = if (meta != null && meta.displayArtist.isNotBlank()) {
                    "${meta.displayArtist} — ${meta.displayTitle}"
                } else meta?.displayTitle ?: "Аудиозапись"

                list.add(
                    com.flasskdev.vibe.ui.viewmodels.AudioTrackInfo(
                        id = trackId,
                        url = audioUrl,
                        title = trackTitle,
                        avatarUrl = if (msg.senderId == myUserId) myAvatarUrl else partnerAvatarUrl
                    )
                )
            }
        }
    }
    return list
}


