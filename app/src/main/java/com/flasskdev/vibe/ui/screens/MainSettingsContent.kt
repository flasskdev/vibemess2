package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun MainSettingsContent(
    onNavigateToPrivacy: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToDevices: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { com.flasskdev.vibe.data.local.AppDatabase.getDatabase(context) }
    val userPrefs = remember { com.flasskdev.vibe.data.UserPreferences(context) }
    val user by db.chatDao().getUserById(userPrefs.userId).collectAsState(initial = null)
    
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, bottom = 80.dp)
    ) {
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 24.dp, bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            // Account
            SettingsSection {
                SettingsItem(
                    icon = Icons.Rounded.AccountCircle,
                    text = "Аккаунт",
                    iconTint = Color(0xFF673AB7),
                    onClick = onNavigateToAccount
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Group 1
            SettingsSection {
                SettingsItem(
                    icon = Icons.Rounded.ChatBubble,
                    text = "Настройки чатов",
                    iconTint = Color(0xFF4CAF50),
                    onClick = { /* TODO */ }
                )
                SettingsItem(
                    icon = Icons.Rounded.Lock,
                    text = "Конфиденциальность",
                    iconTint = Color(0xFF2196F3),
                    onClick = onNavigateToPrivacy
                )
                SettingsItem(
                    icon = Icons.Rounded.Notifications,
                    text = "Уведомления",
                    iconTint = Color(0xFFF44336),
                    onClick = { /* TODO */ }
                )
                SettingsItem(
                    icon = Icons.Rounded.BatterySaver,
                    text = "Экономия энергии",
                    iconTint = Color(0xFFCDDC39),
                    onClick = { /* TODO */ }
                )
                SettingsItem(
                    icon = Icons.Rounded.Devices,
                    text = "Устройства",
                    iconTint = Color(0xFFFF9800),
                    onClick = onNavigateToDevices
                )
                SettingsItem(
                    icon = Icons.Rounded.Language,
                    text = "Язык",
                    iconTint = Color(0xFF9C27B0),
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            // Group 2
            SettingsSection {
                SettingsItem(
                    icon = Icons.Rounded.WorkspacePremium,
                    text = "Vibe Pro",
                    iconTint = Color(0xFFFFC107),
                    onClick = { /* TODO */ }
                )
                SettingsItem(
                    icon = Icons.Rounded.AutoAwesome,
                    text = "Vibes",
                    iconTint = Color(0xFFE91E63),
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Group 3
            SettingsSection {
                SettingsItem(
                    icon = Icons.Rounded.SupportAgent,
                    text = "Поддержка",
                    iconTint = Color(0xFF00BCD4),
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(32.dp))

            // Version
            Text(
                text = "Версия приложения 1.0.6",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}