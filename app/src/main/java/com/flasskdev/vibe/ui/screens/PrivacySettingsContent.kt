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
import com.flasskdev.vibe.ui.theme.LocalVibeStrings
import com.flasskdev.vibe.ui.theme.VibeStrings
import com.flasskdev.vibe.ui.theme.VibeTopGlow

fun formatBlockedCount(count: Int, strings: VibeStrings): String {
    val locale = java.util.Locale.getDefault()
    return when {
        count <= 0 -> "0"
        count < 1000 -> count.toString()
        count < 10_000 -> strings.unitCompactFormat(
            String.format(locale, "%.1f", count / 1000.0).replace(".0", "").replace(",0", ""),
            strings.unitThousandShort
        )
        count < 1_000_000 -> strings.unitCompactFormat(
            String.format(locale, "%d", count / 1000),
            strings.unitThousandShort
        )
        else -> strings.unitCompactFormat(
            String.format(locale, "%.1f", count / 1_000_000.0).replace(".0", "").replace(",0", ""),
            strings.unitMillionShort
        )
    }
}

@Composable
fun PrivacySettingsContent(
    onBack: () -> Unit,
    blockedCount: Int = 0,
    twoFactorEnabled: Boolean = false,
    passcodeEnabled: Boolean = false,
    onNavigateToBlockedUsers: () -> Unit,
    onNavigateToTwoFactor: () -> Unit = {},
    onNavigateToPasscodeSetup: () -> Unit,
    onNavigateToActivity: () -> Unit,
    onNavigateToAvatar: () -> Unit,
    onNavigateToForwarded: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToStatus: () -> Unit
) {
    val strings = LocalVibeStrings.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        VibeTopGlow(height = 380.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 8.dp)
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = strings.backBtn,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = strings.privacyScreenTitle,
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
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        ) {
            // Group 1
            SettingsSection {
                SettingsItem(
                    icon = Icons.Rounded.Security,
                    text = strings.privacyTwoFactor,
                    iconTint = Color(0xFF2196F3),
                    value = if (twoFactorEnabled) strings.twoFactorStatusEnabled else strings.twoFactorStatusDisabled,
                    onClick = onNavigateToTwoFactor
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Rounded.Password,
                    text = strings.privacyPasscodeLogin,
                    iconTint = Color(0xFF4CAF50),
                    value = if (passcodeEnabled) strings.twoFactorStatusEnabled else strings.twoFactorStatusDisabled,
                    onClick = onNavigateToPasscodeSetup
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Rounded.Block,
                    text = strings.privacyBlocked,
                    iconTint = Color(0xFFF44336),
                    value = formatBlockedCount(blockedCount, strings),
                    onClick = onNavigateToBlockedUsers
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Group 2
            SettingsSection {
                SettingsItem(
                    icon = Icons.Rounded.AccessTime,
                    text = strings.privacyActivityTitle,
                    iconTint = Color(0xFF9C27B0),
                    onClick = onNavigateToActivity
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Rounded.AccountBox,
                    text = strings.privacyAvatarTitle,
                    iconTint = Color(0xFFE91E63),
                    onClick = onNavigateToAvatar
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Rounded.Forward,
                    text = strings.privacyForwardedTitle,
                    iconTint = Color(0xFF00BCD4),
                    onClick = onNavigateToForwarded
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Rounded.Chat,
                    text = strings.privacyMessagesTitle,
                    iconTint = Color(0xFF4CAF50),
                    onClick = onNavigateToMessages
                )
                SettingsDivider()
                SettingsItem(
                    icon = Icons.Rounded.Info,
                    text = strings.privacyStatusTitle,
                    iconTint = Color(0xFFFFC107),
                    onClick = onNavigateToStatus
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
}