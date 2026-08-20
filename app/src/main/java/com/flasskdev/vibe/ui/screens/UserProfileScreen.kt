package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.data.local.AppDatabase
import com.flasskdev.vibe.ui.components.UserBadgesRow
import com.flasskdev.vibe.ui.theme.VibePrimary
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.utils.ImageUtils
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun UserProfileScreen(
    userId: Int,
    userName: String,
    webSocket: VibeWebSocket,
    onBack: () -> Unit,
    onNavigateToChat: ((partnerId: Int, messageId: Int) -> Unit)? = null
) {
    androidx.activity.compose.BackHandler {
        onBack()
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val db = remember { AppDatabase.getDatabase(context) }
    
    val currentUserId = remember { UserPreferences(context).userId }
    var resolvedUserId by remember(userId) { mutableIntStateOf(userId) }
    var isSearchingUser by remember(userId, userName) { mutableStateOf(userId == 0 && userName.isNotBlank()) }
    var isUserNotFound by remember(userId, userName) { mutableStateOf(false) }

    DisposableEffect(webSocket, userName) {
        val listener = object : com.flasskdev.vibe.data.VibeWebSocketListener {
            override fun onUsersSearchResult(users: List<com.flasskdev.vibe.data.UserSearchResult>) {
                val cleanUsername = userName.removePrefix("@").trim()
                val match = users.firstOrNull { 
                    it.username?.equals(cleanUsername, ignoreCase = true) == true 
                }
                if (match != null) {
                    resolvedUserId = match.id
                    isSearchingUser = false
                    isUserNotFound = false
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        db.chatDao().insertUser(
                            com.flasskdev.vibe.data.local.UserCacheEntity(
                                id = match.id,
                                name = match.name ?: cleanUsername,
                                username = match.username ?: cleanUsername,
                                avatarUrl = match.avatarUrl,
                                isVerified = match.isVerified,
                                isDeveloper = match.isDeveloper,
                                isBot = match.isBot,
                                isBanned = match.isBanned,
                                isFreezed = match.isFreezed
                            )
                        )
                    }
                } else {
                    isSearchingUser = false
                    isUserNotFound = true
                }
            }
        }
        webSocket.addListener(listener)
        onDispose {
            webSocket.removeListener(listener)
        }
    }

    LaunchedEffect(userId, userName) {
        if (userId == 0 && userName.isNotBlank()) {
            val cleanUsername = userName.removePrefix("@").trim()
            val found = db.chatDao().getUserByUsername(cleanUsername)
            if (found != null) {
                resolvedUserId = found.id
                isSearchingUser = false
                isUserNotFound = false
            } else {
                isSearchingUser = true
                isUserNotFound = false
                webSocket.searchUsers(cleanUsername, currentUserId)
                kotlinx.coroutines.delay(3500)
                if (resolvedUserId == 0 && isSearchingUser) {
                    isSearchingUser = false
                    isUserNotFound = true
                }
            }
        } else if (userId > 0) {
            isSearchingUser = false
            isUserNotFound = false
        }
    }
    
    val user by db.chatDao().getUserById(resolvedUserId).collectAsState(initial = null)
    val isCurrentUser = resolvedUserId == currentUserId
    
    val messagesWithAttachments by db.chatDao().getMessagesWithAttachments(currentUserId, resolvedUserId).collectAsState(initial = emptyList())

    var showAvatarDialog by remember { mutableStateOf(false) }
    var showAvatarViewer by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    LaunchedEffect(resolvedUserId) {
        if (resolvedUserId > 0) {
            webSocket.getUserInfo(resolvedUserId) // Ensure fresh data
        }
    }

    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val displayName = when {
        user?.isBanned == true -> strings.accountDeleted
        user?.isFreezed == true -> strings.accountFrozen
        else -> user?.name?.takeIf { it.isNotBlank() } ?: userName
    }
    val usernameText = "@${user?.username ?: userName.lowercase(Locale.getDefault()).replace(" ", "")}"

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showCopyToast by remember { mutableStateOf(false) }
    var showUnblockToast by remember { mutableStateOf(false) }

    LaunchedEffect(showCopyToast) {
        if (showCopyToast) {
            kotlinx.coroutines.delay(2000)
            showCopyToast = false
        }
    }

    LaunchedEffect(showUnblockToast) {
        if (showUnblockToast) {
            kotlinx.coroutines.delay(2500)
            showUnblockToast = false
        }
    }

    val isBlocked = user?.isBlockedByUser == true || user?.isBanned == true || user?.isFreezed == true
    val isBlockedByMe = user?.isBlockedByMe == true
    val isOnline = user?.isOnline == true && !isBlocked && !isBlockedByMe && user?.isBot != true

    val statusText = if (isBlocked) {
        strings.lastSeenLongAgo
    } else if (isBlockedByMe) {
        "Заблокирован"
    } else if (user?.isBot == true) {
        strings.statusBot
    } else if (isOnline) {
        strings.statusOnline
    } else if (user?.lastSeenStatus == "hidden" || user?.lastSeenStatus == "approximate" || user?.lastSeenStatus == "recently") {
        strings.lastSeenRecently
    } else if (user?.lastSeenStatus == "long_ago") {
        strings.lastSeenLongAgo
    } else if (user?.lastSeenStatus == "this_week") {
        "Был(а) на этой неделе"
    } else if (user?.lastSeenStatus == "this_month") {
        "Был(а) в этом месяце"
    } else {
        formatLastSeenProfile(user?.lastSeen, strings)
    }

    ModalBottomSheet(
        onDismissRequest = onBack,
        sheetState = sheetState,
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        if (isUserNotFound || (resolvedUserId == 0 && !isSearchingUser && user == null)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(com.flasskdev.vibe.ui.theme.VibeError.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = com.flasskdev.vibe.ui.theme.VibeError,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Пользователь не найден",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                val targetUsername = if (userName.startsWith("@")) userName else "@$userName"
                Text(
                    text = "Пользователь с юзернеймом $targetUsername не зарегистрирован в Vibe или удалил свой аккаунт.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = VibePrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Назад", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }
        } else if (user == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = VibePrimary,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Загрузка профиля...",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            
            val scrollState = rememberScrollState()
            val density = androidx.compose.ui.platform.LocalDensity.current
            val maxScrollPx = with(density) { 250.dp.toPx() }
            val progress by remember { derivedStateOf { (scrollState.value / maxScrollPx).coerceIn(0f, 1f) } }

            val configuration = androidx.compose.ui.platform.LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            
            val expandedAvatarSize = minOf(screenWidth - 32.dp, 360.dp)
            val collapsedAvatarSize = 44.dp
            
            val expandedAvatarX = (screenWidth - expandedAvatarSize) / 2
            val collapsedAvatarX = (screenWidth - collapsedAvatarSize) / 2
            
            val expandedAvatarY = 56.dp
            val collapsedAvatarY = 12.dp
            
            val avatarSize = androidx.compose.ui.unit.lerp(expandedAvatarSize, collapsedAvatarSize, progress)
            val avatarX = androidx.compose.ui.unit.lerp(expandedAvatarX, collapsedAvatarX, progress)
            val avatarY = androidx.compose.ui.unit.lerp(expandedAvatarY, collapsedAvatarY, progress)
            val avatarCornerRadius = androidx.compose.ui.unit.lerp(24.dp, collapsedAvatarSize / 2, progress)
            
            val expandedTextY = expandedAvatarY + expandedAvatarSize - 76.dp
            val collapsedTextY = collapsedAvatarY + collapsedAvatarSize + 4.dp
            val textY = androidx.compose.ui.unit.lerp(expandedTextY, collapsedTextY, progress)
            
            val nameSize = androidx.compose.ui.unit.lerp(26.sp, 18.sp, progress)
            val usernameSize = androidx.compose.ui.unit.lerp(16.sp, 13.sp, progress)
            
            val expandedNameColor = Color.White
            val collapsedNameColor = MaterialTheme.colorScheme.onBackground
            val nameColor = androidx.compose.ui.graphics.lerp(expandedNameColor, collapsedNameColor, progress)
            
            val expandedUsernameColor = Color.White.copy(alpha = 0.8f)
            val collapsedUsernameColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            val usernameColor = androidx.compose.ui.graphics.lerp(expandedUsernameColor, collapsedUsernameColor, progress)
            
            val collapsedHeaderHeight = 104.dp
            val expandedHeaderHeight = expandedAvatarY + expandedAvatarSize + 16.dp

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                
                // 1. Scrollable Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = expandedHeaderHeight)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isBlockedByMe)
                            com.flasskdev.vibe.ui.theme.VibeError
                        else if (isOnline)
                            com.flasskdev.vibe.ui.theme.VibeOnlineGreen
                        else MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    UserBadgesRow(
                        isVerified = user?.isVerified == true,
                        isDeveloper = user?.isDeveloper == true,
                        isBot = user?.isBot == true,
                        isBanned = user?.isBanned == true,
                        isFreezed = user?.isFreezed == true
                    )
                    
                    if (!isCurrentUser) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                onNavigateToChat?.invoke(resolvedUserId, 0)
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VibePrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Написать",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    val registerDateFmt = user?.registerDate?.let { ts ->
                        val now = Calendar.getInstance()
                        val reg = Calendar.getInstance().apply { timeInMillis = ts }
                        val day = reg.get(Calendar.DAY_OF_MONTH)
                        val monthIdx = reg.get(Calendar.MONTH)
                        val monthStr = strings.monthsShort.getOrNull(monthIdx) ?: ""
                        val year = reg.get(Calendar.YEAR)
                        
                        when {
                            now.get(Calendar.YEAR) == year &&
                            now.get(Calendar.DAY_OF_YEAR) == reg.get(Calendar.DAY_OF_YEAR) -> strings.dateToday
                            
                            now.get(Calendar.YEAR) == year &&
                            now.get(Calendar.DAY_OF_YEAR) - reg.get(Calendar.DAY_OF_YEAR) == 1 -> strings.dateYesterday
                            
                            now.get(Calendar.YEAR) == year -> "$day $monthStr"
                            else -> "$day $monthStr $year"
                        }
                    } ?: strings.statusUnknown

                    if (!user?.about.isNullOrBlank() && user?.isBanned != true && user?.isFreezed != true) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            shadowElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = strings.aboutLabel,
                                    tint = VibePrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = strings.aboutLabel,
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                if (user?.isBanned == true) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                } else if (user?.isFreezed == true) {
                                    Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = Color(0xFF87CEEB), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    text = user?.about ?: "",
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    if (user?.isBot != true) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            shadowElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = strings.registerDateLabel,
                                    tint = VibePrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = strings.registerDateLabel,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = registerDateFmt,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    softWrap = false,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (messagesWithAttachments.isNotEmpty()) {
                        com.flasskdev.vibe.ui.components.ProfileMediaTabs(
                            messages = messagesWithAttachments,
                            partnerAvatarUrl = user?.avatarUrl,
                            onNavigateToMessage = { messageId, partnerId ->
                                onNavigateToChat?.invoke(partnerId, messageId)
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(300.dp))
                }
                
                // 2. Sticky Header
                val currentScrollDp = with(density) { scrollState.value.toDp() }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(expandedHeaderHeight)
                        .offset(y = currentScrollDp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(collapsedHeaderHeight)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                            )
                    )
                    
                    Box(
                        modifier = Modifier
                            .offset(x = avatarX, y = avatarY)
                            .size(avatarSize)
                            .clip(RoundedCornerShape(avatarCornerRadius))
                            .background(VibePrimary)
                            .clickable(enabled = progress < 0.5f) { 
                                if (!user?.avatarUrl.isNullOrEmpty() && user?.isBanned != true && user?.isFreezed != true && user?.isBlockedByUser != true) {
                                    showAvatarViewer = true 
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!user?.avatarUrl.isNullOrEmpty() && user?.isBanned != true && user?.isFreezed != true && user?.isBlockedByUser != true) {
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(user?.avatarUrl).crossfade(true).build(),
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = if (displayName.isNotEmpty()) displayName.take(1).uppercase() else "",
                                color = Color.White,
                                fontSize = androidx.compose.ui.unit.lerp(120.sp, 20.sp, progress),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .height(120.dp)
                                .background(
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                    )
                                )
                                .alpha((1f - progress * 2).coerceIn(0f, 1f))
                        )
                        
                        if (isCurrentUser) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.4f))
                                    .alpha((1f - progress * 2).coerceIn(0f, 1f))
                                    .clickable { showAvatarDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Edit Avatar",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = textY)
                            .padding(horizontal = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            /* // Если аккаунт заблокирован - перед ником выводим иконку мусорки
                            if (user?.isBanned == true) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(nameSize.value.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                            } else if (user?.isFreezed == true) {
                                Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = Color(0xFF87CEEB), modifier = Modifier.size(nameSize.value.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                            }*/
                            Text(
                                text = displayName,
                                color = nameColor,
                                fontSize = nameSize,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                modifier = if (displayName.length > 20) Modifier.basicMarquee() else Modifier
                            )
                        }
                        Text(
                            text = usernameText,
                            color = usernameColor,
                            fontSize = usernameSize,
                            maxLines = 1,
                            modifier = Modifier.combinedClickable(
                                onClick = {},
                                onLongClick = {
                                    clipboardManager.setText(AnnotatedString(usernameText))
                                    showCopyToast = true
                                }
                            )
                        )
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 4.dp, end = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.backBtn,
                                tint = VibePrimary
                            )
                        }

                        if (!isCurrentUser && user?.isBot != true) {
                            var showProfileMenu by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { showProfileMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Options",
                                        tint = VibePrimary
                                    )
                                }
                                DropdownMenu(
                                    expanded = showProfileMenu,
                                    onDismissRequest = { showProfileMenu = false }
                                ) {
                                    if (user?.isBlockedByMe == true) {
                                        DropdownMenuItem(
                                            text = { Text("Разблокировать", color = MaterialTheme.colorScheme.onSurface) },
                                            onClick = {
                                                webSocket.unblockUser(currentUserId, resolvedUserId)
                                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                                                    db.chatDao().updateUserBlockedByMe(resolvedUserId, false)
                                                    db.chatDao().updateChatBlockedByMe(resolvedUserId, false)
                                                }
                                                showProfileMenu = false
                                                showUnblockToast = true
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
                                            text = { Text("Заблокировать", color = com.flasskdev.vibe.ui.theme.VibeError) },
                                            onClick = {
                                                webSocket.blockUser(currentUserId, resolvedUserId)
                                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                                                    db.chatDao().updateUserBlockedByMe(resolvedUserId, true)
                                                    db.chatDao().updateChatBlockedByMe(resolvedUserId, true)
                                                }
                                                showProfileMenu = false
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Outlined.Block,
                                                    contentDescription = null,
                                                    tint = com.flasskdev.vibe.ui.theme.VibeError
                                                )
                                            }
                                        )
                                    }
                                }
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
                    message = strings.usernameCopied,
                    isVisible = showCopyToast,
                    onDismiss = { showCopyToast = false }
                )
                com.flasskdev.vibe.ui.components.VibeToast(
                    message = "Пользователь разблокирован",
                    isVisible = showUnblockToast,
                    onDismiss = { showUnblockToast = false }
                )
            }
        }
    }

    if (showAvatarDialog) {
        var cropScale by remember { mutableStateOf(1f) }
        var cropOffset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
        var boxWidthPx by remember { mutableStateOf(0f) }
        var boxHeightPx by remember { mutableStateOf(0f) }
        
        androidx.compose.ui.window.Dialog(onDismissRequest = { 
            showAvatarDialog = false 
            selectedImageUri = null 
        }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = strings.addAvatar,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Рамка с кружком
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                            .onGloballyPositioned { coordinates ->
                                boxWidthPx = coordinates.size.width.toFloat()
                                boxHeightPx = coordinates.size.height.toFloat()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                val state = androidx.compose.foundation.gestures.rememberTransformableState { zoomChange, offsetChange, _ ->
                                    cropScale = (cropScale * zoomChange).coerceIn(1f, 5f)
                                    cropOffset += offsetChange
                                }
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Preview",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .pointerInput(Unit) {
                                            detectDragGestures { _, dragAmount ->
                                                cropOffset += dragAmount
                                            }
                                        }
                                        .transformable(state = state)
                                        .graphicsLayer(
                                            scaleX = cropScale,
                                            scaleY = cropScale,
                                            translationX = cropOffset.x,
                                            translationY = cropOffset.y
                                        )
                                )
                            } else if (!user?.avatarUrl.isNullOrEmpty()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(user?.avatarUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Preview",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(140.dp).clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Choose photo",
                                    modifier = Modifier.size(48.dp),
                                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        // Затенение с вырезанным кругом (оверлей)
                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                            val radius = 70.dp.toPx()
                            val path = androidx.compose.ui.graphics.Path().apply {
                                addRect(androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.height))
                                addOval(androidx.compose.ui.geometry.Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius))
                                fillType = androidx.compose.ui.graphics.PathFillType.EvenOdd
                            }
                            drawPath(path, color = Color.Black.copy(alpha = 0.5f))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(strings.choosePhoto, color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                    }
                    
                    if (selectedImageUri != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val density = androidx.compose.ui.platform.LocalDensity.current
                        Button(
                            onClick = {
                                val circleRadiusPx = with(density) { 70.dp.toPx() }
                                
                                val base64 = ImageUtils.compressAndEncodeImage(
                                    context, 
                                    selectedImageUri!!,
                                    scale = cropScale,
                                    offsetX = cropOffset.x,
                                    offsetY = cropOffset.y,
                                    boxWidthPx = boxWidthPx,
                                    boxHeightPx = boxHeightPx,
                                    circleRadiusPx = circleRadiusPx
                                )
                                if (base64 != null) {
                                    webSocket.uploadAvatar(userId, base64)
                                }
                                showAvatarDialog = false
                                selectedImageUri = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VibePrimary)
                        ) {
                            Text(strings.doneBtn, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
    }
    
    if (showAvatarViewer && !user?.avatarUrl.isNullOrEmpty() && user?.isBanned != true && user?.isFreezed != true) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showAvatarViewer = false },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                var zoom by remember { mutableStateOf(1f) }
                var offset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
                val state = rememberTransformableState { zoomChange, offsetChange, _ ->
                    zoom = (zoom * zoomChange).coerceIn(1f, 5f)
                    offset += offsetChange
                }
                
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(user?.avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Full Avatar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                                if (zoom > 1f) {
                                    offset += dragAmount
                                } else {
                                    showAvatarViewer = false
                                }
                            }
                        }
                        .transformable(state = state)
                        .graphicsLayer(
                            scaleX = zoom,
                            scaleY = zoom,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                )
                
                IconButton(
                    onClick = { showAvatarViewer = false },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 48.dp, start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Close", 
                        tint = Color.White
                    )
                }
            }
        }
    }
}



private fun formatLastSeenProfile(lastSeenTimestamp: Long?, strings: com.flasskdev.vibe.ui.theme.VibeStrings): String {
    if (lastSeenTimestamp == null) return strings.lastSeenRecently

    val now = Calendar.getInstance()
    val lastSeen = Calendar.getInstance().apply { timeInMillis = lastSeenTimestamp }

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeStr = timeFormat.format(Date(lastSeenTimestamp))

    return when {
        now.get(Calendar.YEAR) == lastSeen.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == lastSeen.get(Calendar.DAY_OF_YEAR) -> {
            strings.lastSeenToday(timeStr)
        }
        now.get(Calendar.YEAR) == lastSeen.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - lastSeen.get(Calendar.DAY_OF_YEAR) == 1 -> {
            strings.lastSeenYesterday(timeStr)
        }
        now.get(Calendar.YEAR) > lastSeen.get(Calendar.YEAR) -> {
            strings.lastSeenLongAgo
        }
        else -> {
            val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
            strings.lastSeenDate(dateFormat.format(Date(lastSeenTimestamp)), timeStr)
        }
    }
}
