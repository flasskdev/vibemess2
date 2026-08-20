package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun formatBlockedCount(count: Int): String {
    return when {
        count <= 0 -> "0"
        count < 1000 -> count.toString()
        count < 10_000 -> String.format(java.util.Locale.getDefault(), "%.1f тыс.", count / 1000.0).replace(".0", "")
        count < 1_000_000 -> String.format(java.util.Locale.getDefault(), "%d тыс.", count / 1000)
        else -> String.format(java.util.Locale.getDefault(), "%.1f млн", count / 1_000_000.0).replace(".0", "")
    }
}

@Composable
fun PrivacySettingsContent(
    onBack: () -> Unit,
    blockedCount: Int = 0,
    onNavigateToBlockedUsers: () -> Unit,
    onNavigateToPasscodeSetup: () -> Unit,
    onNavigateToActivity: () -> Unit,
    onNavigateToAvatar: () -> Unit,
    onNavigateToForwarded: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToStatus: () -> Unit
) {
    val scrollState = rememberScrollState()

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
                text = "Конфиденциальность",
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
                SettingsItem(
                    icon = Icons.Rounded.Security,
                    text = "Двойная аутентификация",
                    iconTint = Color(0xFF2196F3),
                    onClick = { /* TODO */ }
                )
                SettingsItem(
                    icon = Icons.Rounded.Password,
                    text = "Вход по коду",
                    iconTint = Color(0xFF4CAF50),
                    onClick = onNavigateToPasscodeSetup
                )
                SettingsItem(
                    icon = Icons.Rounded.Block,
                    text = "Заблокированные",
                    iconTint = Color(0xFFF44336),
                    value = formatBlockedCount(blockedCount),
                    onClick = onNavigateToBlockedUsers
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Group 2
            SettingsSection {
                SettingsItem(
                    icon = Icons.Rounded.AccessTime,
                    text = "Статус активности",
                    iconTint = Color(0xFF9C27B0),
                    onClick = onNavigateToActivity
                )
                SettingsItem(
                    icon = Icons.Rounded.AccountBox,
                    text = "Аватарка",
                    iconTint = Color(0xFFE91E63),
                    onClick = onNavigateToAvatar
                )
                SettingsItem(
                    icon = Icons.Rounded.Forward,
                    text = "Пересланные сообщения",
                    iconTint = Color(0xFF00BCD4),
                    onClick = onNavigateToForwarded
                )
                SettingsItem(
                    icon = Icons.Rounded.Chat,
                    text = "Сообщения",
                    iconTint = Color(0xFF4CAF50),
                    onClick = onNavigateToMessages
                )
                SettingsItem(
                    icon = Icons.Rounded.Info,
                    text = "Статус",
                    iconTint = Color(0xFFFFC107),
                    onClick = onNavigateToStatus
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
