package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.data.VibeWebSocketListener
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class SessionInfo(
    val deviceId: String,
    val deviceName: String,
    val osVersion: String,
    val location: String,
    val lastActive: Long
)

@Composable
fun DevicesScreenContent(
    webSocket: VibeWebSocket,
    userPreferences: UserPreferences,
    onBack: () -> Unit
) {
    var sessions by remember { mutableStateOf<List<SessionInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    DisposableEffect(webSocket) {
        val listener = object : VibeWebSocketListener {
            override fun onSessionsResult(sessionsArray: JSONArray) {
                val list = mutableListOf<SessionInfo>()
                for (i in 0 until sessionsArray.length()) {
                    val obj = sessionsArray.optJSONObject(i) ?: continue
                    list.add(
                        SessionInfo(
                            deviceId = obj.optString("device_id", ""),
                            deviceName = obj.optString("device_name", "Unknown"),
                            osVersion = obj.optString("os_version", "Unknown"),
                            location = obj.optString("location", "Unknown Location"),
                            lastActive = obj.optLong("last_active", 0L)
                        )
                    )
                }
                sessions = list
                isLoading = false
            }

            override fun onSessionTerminated(deviceId: String) {
                sessions = sessions.filter { it.deviceId != deviceId }
            }
        }
        webSocket.addListener(listener)
        webSocket.getSessions(userPreferences.userId)
        
        onDispose {
            webSocket.removeListener(listener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
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
                text = "Устройства",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val currentSession = sessions.find { it.deviceId == userPreferences.deviceId }
                val otherSessions = sessions.filter { it.deviceId != userPreferences.deviceId }

                if (currentSession != null) {
                    item {
                        Text(
                            text = "ТЕКУЩЕЕ УСТРОЙСТВО",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                        )
                        SessionItem(
                            session = currentSession,
                            isCurrent = true,
                            onTerminate = {}
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                if (otherSessions.isNotEmpty()) {
                    item {
                        Text(
                            text = "АКТИВНЫЕ СЕАНСЫ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                        )
                    }
                    items(otherSessions) { session ->
                        SessionItem(
                            session = session,
                            isCurrent = false,
                            onTerminate = {
                                webSocket.terminateSession(userPreferences.userId, session.deviceId)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else if (currentSession != null) {
                    item {
                        Text(
                            text = "Нет других активных сеансов.",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SessionItem(
    session: SessionInfo,
    isCurrent: Boolean,
    onTerminate: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }
    val dateString = if (session.lastActive > 0) formatter.format(Date(session.lastActive)) else "Неизвестно"
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Smartphone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.deviceName,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${session.osVersion} • ${session.location}",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Text(
                    text = if (isCurrent) "В сети" else "Был(а): $dateString",
                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            if (!isCurrent) {
                IconButton(onClick = onTerminate) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Завершить",
                        tint = Color(0xFFFF3B30)
                    )
                }
            }
        }
    }
}
