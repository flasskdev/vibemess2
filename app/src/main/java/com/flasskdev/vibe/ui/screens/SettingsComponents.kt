package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.ui.theme.VibeSystemGray3

@Composable
fun SettingsSection(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 56.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    text: String,
    iconTint: Color,
    value: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(29.dp)
                .background(iconTint, RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )

        if (!value.isNullOrBlank()) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = VibeSystemGray3,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
        
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = VibeSystemGray3,
            modifier = Modifier.size(20.dp)
        )
    }
}