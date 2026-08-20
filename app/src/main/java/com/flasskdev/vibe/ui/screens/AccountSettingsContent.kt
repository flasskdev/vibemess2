package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.data.VibeWebSocket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import com.flasskdev.vibe.data.local.AppDatabase

@Composable
fun AccountSettingsContent(
    userPreferences: UserPreferences,
    webSocket: VibeWebSocket,
    onLogout: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToEditUsername: () -> Unit,
    onNavigateToEditNickname: () -> Unit,
    onNavigateToEditBio: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val userId = userPreferences.userId
    val user by db.chatDao().getUserById(userId).collectAsState(initial = null)
    
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, bottom = 80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
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
                text = "Аккаунт",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            // Group 1
            SettingsSection {
                AccountInfoItem(
                    icon = Icons.Rounded.AlternateEmail,
                    title = if (user?.username.isNullOrEmpty()) "Не задан" else "@${user?.username}",
                    subtitle = "Юзернейм",
                    onClick = onNavigateToEditUsername
                )
                AccountInfoItem(
                    icon = Icons.Rounded.Person,
                    title = if (user?.name.isNullOrEmpty()) "Без имени" else user?.name ?: "",
                    subtitle = "Никнейм",
                    onClick = onNavigateToEditNickname
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Group 2: O sebe
            Text(
                text = "О себе",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            SettingsSection {
                AccountInfoItem(
                    icon = Icons.Rounded.Info,
                    title = if (user?.about.isNullOrEmpty()) "Напишите немного о себе..." else user?.about ?: "",
                    subtitle = "Описание профиля",
                    onClick = onNavigateToEditBio
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Clickable info text
            Text(
                text = androidx.compose.ui.text.buildAnnotatedString {
                    append("Вы можете настроить отображение статуса О себе в ")
                    withStyle(style = androidx.compose.ui.text.SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("настройках")
                    }
                    append(".")
                },
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onNavigateToPrivacy() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Logout button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onLogout() }),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFF3B30).copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Logout,
                            contentDescription = null,
                            tint = Color(0xFFFF3B30),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Text(
                        text = "Выйти",
                        color = Color(0xFFFF3B30),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AccountInfoItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            modifier = Modifier.size(28.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 13.sp
            )
        }
    }
}
