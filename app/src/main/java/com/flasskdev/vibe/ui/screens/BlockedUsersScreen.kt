package com.flasskdev.vibe.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.LockOpen
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
import com.flasskdev.vibe.data.BlockedUserItem
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.data.VibeWebSocketListener
import com.flasskdev.vibe.data.local.AppDatabase
import com.flasskdev.vibe.ui.components.UserBadgesRow
import com.flasskdev.vibe.ui.components.VibeToast
import com.flasskdev.vibe.ui.theme.VibePrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BlockedUsersScreen(
    webSocket: VibeWebSocket,
    onBack: () -> Unit,
    onProfileClick: (userId: Int, username: String) -> Unit
) {
    val context = LocalContext.current
    val currentUserId = remember { UserPreferences(context).userId }
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var users by remember { mutableStateOf<List<BlockedUserItem>>(emptyList()) }
    var totalCount by remember { mutableIntStateOf(0) }
    var page by remember { mutableIntStateOf(1) }
    var hasMore by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isLoadingMore by remember { mutableStateOf(false) }

    var toastMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(showToast) {
        if (showToast) {
            delay(2500)
            showToast = false
        }
    }

    val listState = rememberLazyListState()

    DisposableEffect(webSocket, currentUserId) {
        val listener = object : VibeWebSocketListener {
            override fun onBlockedUsersResult(
                newUsers: List<BlockedUserItem>,
                total: Int,
                p: Int,
                more: Boolean
            ) {
                totalCount = total
                page = p
                hasMore = more
                isLoading = false
                isLoadingMore = false

                if (p == 1) {
                    users = newUsers
                } else {
                    val existingIds = users.map { it.id }.toSet()
                    val filtered = newUsers.filter { it.id !in existingIds }
                    users = users + filtered
                }
            }

            override fun onUnblockUserSuccess(blockedId: Int) {
                users = users.filter { it.id != blockedId }
                totalCount = maxOf(0, totalCount - 1)
                toastMessage = "Пользователь разблокирован"
                showToast = true

                scope.launch(Dispatchers.IO) {
                    db.chatDao().updateUserBlockedByMe(blockedId, false)
                    db.chatDao().updateChatBlockedByMe(blockedId, false)
                }
            }
        }
        webSocket.addListener(listener)
        onDispose {
            webSocket.removeListener(listener)
        }
    }

    // Debounced search / initial load
    LaunchedEffect(searchQuery, currentUserId) {
        if (currentUserId <= 0) return@LaunchedEffect
        delay(if (searchQuery.isEmpty()) 0 else 300)
        isLoading = true
        page = 1
        webSocket.getBlockedUsers(currentUserId, page = 1, limit = 30, query = searchQuery.trim())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, start = 8.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Заблокированные",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (totalCount > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "($totalCount)",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    cursorBrush = SolidColor(VibePrimary),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Поиск пользователей...",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    },
                    singleLine = true
                )
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (isLoading && users.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = VibePrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            } else if (users.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Block,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "Ничего не найдено" else "Нет заблокированных пользователей",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (searchQuery.isNotBlank())
                                "По запросу «$searchQuery» пользователей не найдено"
                            else
                                "Здесь будут отображаться пользователи, которых вы заблокировали",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    itemsIndexed(
                        items = users,
                        key = { _, item -> item.id }
                    ) { index, user ->
                        // Pagination trigger
                        if (index >= users.size - 4 && hasMore && !isLoadingMore) {
                            LaunchedEffect(index) {
                                isLoadingMore = true
                                webSocket.getBlockedUsers(
                                    currentUserId,
                                    page = page + 1,
                                    limit = 30,
                                    query = searchQuery.trim()
                                )
                            }
                        }

                        BlockedUserRow(
                            user = user,
                            onProfileClick = { onProfileClick(user.id, user.username ?: "") },
                            onUnblock = {
                                webSocket.unblockUser(currentUserId, user.id)
                            }
                        )

                        if (index < users.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 72.dp, end = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                                thickness = 0.8.dp
                            )
                        }
                    }

                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = VibePrimary,
                                    strokeWidth = 2.5.dp,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Toast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                VibeToast(
                    message = toastMessage,
                    isVisible = showToast,
                    onDismiss = { showToast = false }
                )
            }
        }
    }
}

@Composable
private fun BlockedUserRow(
    user: BlockedUserItem,
    onProfileClick: () -> Unit,
    onUnblock: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val displayName = user.name?.takeIf { it.isNotBlank() } ?: (user.username ?: "User #${user.id}")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(VibePrimary),
            contentAlignment = Alignment.Center
        ) {
            if (!user.avatarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = if (displayName.isNotEmpty()) displayName.take(1).uppercase() else "",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(4.dp))
                UserBadgesRow(
                    isVerified = user.isVerified,
                    isDeveloper = user.isDeveloper,
                    isBot = user.isBot,
                    isFreezed = user.isFreezed,
                    isBanned = user.isBanned,
                    badgeSize = 14.dp
                )
            }

            if (!user.username.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // 3-dots Menu
        Box {
            IconButton(
                onClick = { showMenu = true },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Опции",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Разблокировать", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        showMenu = false
                        onUnblock()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.LockOpen,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                )
            }
        }
    }
}
