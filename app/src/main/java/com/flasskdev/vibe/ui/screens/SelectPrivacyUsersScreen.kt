package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.flasskdev.vibe.data.local.AppDatabase
import com.flasskdev.vibe.data.local.UserCacheEntity
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import com.flasskdev.vibe.ui.theme.VibePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPrivacyUsersScreen(
    selectedUserIds: Set<Int>,
    onUsersSelected: (Set<Int>) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    
    // Load only users that the current user has chats with
    val chatsWithUsers by db.chatDao().getChatsWithUserInfo().collectAsState(initial = emptyList())
    val allUsers = remember(chatsWithUsers) {
        chatsWithUsers.map {
            UserCacheEntity(
                id = it.chat.interlocutorId,
                name = it.name ?: "",
                username = it.username ?: "",
                avatarUrl = it.avatarUrl,
                isOnline = it.isOnline ?: false,
                lastSeen = it.lastSeen,
                isBot = it.isBot ?: false,
                about = it.about,
                isDeveloper = false,
                isVerified = false,
                registerDate = null
            )
        }
    }
    
    // Mutable set to track changes before saving
    var currentSelected by remember { mutableStateOf(selectedUserIds) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Исключения",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = {
                    onUsersSelected(currentSelected)
                    onBack()
                }
            ) {
                Text(
                    text = "Готово",
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
        
        // Search Bar styled like ChatListScreen
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            ),
            cursorBrush = SolidColor(VibePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Поиск",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        // User List
        val filteredUsers = allUsers.filter {
            (it.name.contains(searchQuery, ignoreCase = true)) ||
            (it.username.contains(searchQuery, ignoreCase = true))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredUsers) { user ->
                val isSelected = currentSelected.contains(user.id)
                UserSelectionItem(
                    user = user,
                    isSelected = isSelected,
                    onClick = {
                        val newSet = currentSelected.toMutableSet()
                        if (isSelected) {
                            newSet.remove(user.id)
                        } else {
                            newSet.add(user.id)
                        }
                        currentSelected = newSet
                    }
                )
            }
            
            if (filteredUsers.isEmpty()) {
                item {
                    Text(
                        text = "Пользователи не найдены",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserSelectionItem(
    user: UserCacheEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        if (!user.avatarUrl.isNullOrEmpty()) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                val initial = if (user.name.isNotEmpty()) {
                    user.name.first().uppercase()
                } else {
                    user.username.firstOrNull()?.uppercase() ?: "?"
                }
                Text(
                    text = initial,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Name & Username
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name.ifEmpty { user.username },
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            if (user.name.isNotEmpty()) {
                Text(
                    text = "@${user.username}",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
        
        // Checkbox/Check Icon
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    if (isSelected) Color(0xFF2196F3) else Color.Transparent,
                    RoundedCornerShape(6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }
    }
}
