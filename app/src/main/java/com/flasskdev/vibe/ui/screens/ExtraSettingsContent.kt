package com.flasskdev.vibe.ui.screens

import android.content.Intent
import android.media.RingtoneManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.data.VibeWebSocketListener
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
internal fun preferenceRevision(prefs: UserPreferences): Int {
    var revision by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    DisposableEffect(prefs) {
        val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> scope.launch { revision++ } }
        prefs.observe(listener)
        onDispose { prefs.unobserve(listener) }
    }
    return revision
}

@Composable
internal fun SettingsPage(title: String, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    BackHandler { onBack() }
    Column(Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Назад / Back") }
            Text(title, style = MaterialTheme.typography.titleLarge)
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 100.dp), verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
    }
}
@Composable
private fun SettingSwitch(text: String, checked: Boolean, enabled: Boolean = true, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text, Modifier.weight(1f).padding(end = 12.dp))
        Switch(checked = checked, onCheckedChange = onChange, enabled = enabled)
    }
}

@Composable
fun NotificationSettingsContent(prefs: UserPreferences, ws: VibeWebSocket, onBack: () -> Unit) {
    val revision = preferenceRevision(prefs)
    val ru = prefs.language == "RU"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var ready by remember { mutableStateOf(false) }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val muteAll = remember(revision) { prefs.notificationMuteAll }
    val autoMute = remember(revision) { prefs.autoMuteNewChats }
    val sound = remember(revision) { prefs.notificationSound }
    DisposableEffect(ws) {
        val listener = object : VibeWebSocketListener {
            override fun onSettingsResponse(message: JSONObject) {
                if (message.optString("type") == "notification_settings_result") scope.launch {
                    busy = false; ready = message.optBoolean("success"); error = if (ready) null else message.optString("message")
                }
            }
            override fun onDisconnected() { scope.launch { busy = false; ready = false } }
            override fun onConnected() { ws.sendRawJson("{\"type\":\"get_notification_settings\"}") }
        }
        ws.addListener(listener); ws.sendRawJson("{\"type\":\"get_notification_settings\"}")
        onDispose { ws.removeListener(listener) }
    }
    LaunchedEffect(busy, ready) {
        if (busy || !ready) {
            kotlinx.coroutines.delay(15_000)
            busy = false
            if (!ready) error = if (ru) "Нет ответа сервера. Проверьте обновление сервера и соединение." else "No server response. Check the server update and connection."
        }
    }
    fun save(all: Boolean, auto: Boolean) {
        busy = true
        ws.sendRawJson(JSONObject().put("type", "set_notification_settings").put("mute_all", all).put("auto_mute_new", auto).toString())
        if (all) androidx.core.app.NotificationManagerCompat.from(context).cancelAll()
    }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            @Suppress("DEPRECATION")
            val uri = result.data?.getParcelableExtra<android.net.Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            prefs.notificationSound = uri?.toString() ?: "silent"
            com.flasskdev.vibe.utils.NotificationHelper.createNotificationChannel(context)
        }
    }
    SettingsPage(if (ru) "Уведомления" else "Notifications", onBack) {
        if (!ready || busy) LinearProgressIndicator(Modifier.fillMaxWidth())
        SettingSwitch(if (ru) "Заглушить все чаты" else "Mute all chats", muteAll, ready && !busy) { save(it, autoMute) }
        SettingSwitch(if (ru) "Автомут новых чатов" else "Automatically mute new chats", autoMute, ready && !busy) { save(muteAll, it) }
        Text(if (ru) "Новые чаты сразу без уведомлений. Существующие чаты не меняются. Включить уведомления можно в меню чата." else "New chats start muted. Existing chats are unchanged. Unmute a conversation in its chat menu.", style = MaterialTheme.typography.bodySmall)
        Button(onClick = {
            val selected = when (sound) { "silent" -> null; "default" -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); else -> android.net.Uri.parse(sound) }
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selected)
            runCatching { picker.launch(intent) }.onFailure { error = if (ru) "На устройстве нет выбора звуков." else "No sound picker is installed." }
        }) { Text(if (ru) "Выбрать звук уведомлений" else "Choose notification sound") }
        Text(when(sound) { "silent" -> if(ru) "Без звука" else "Silent"; "default" -> if(ru) "Системный звук" else "System default"; else -> runCatching { RingtoneManager.getRingtone(context, android.net.Uri.parse(sound))?.getTitle(context) }.getOrNull() ?: (if(ru) "Выбранный звук" else "Selected sound") })
        Text(if (ru) "Звук действует на этом устройстве. Настройки Android и режим «Не беспокоить» имеют приоритет." else "Sound applies to this device. Android notification settings and Do Not Disturb take priority.", style = MaterialTheme.typography.bodySmall)
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

@Composable
fun LanguageSettingsContent(prefs: UserPreferences, onBack: () -> Unit) {
    val revision = preferenceRevision(prefs)
    val current = remember(revision) { prefs.language }
    val ru = current == "RU"
    var query by remember { mutableStateOf("") }
    val languages = listOf(Triple("RU", "Русский", "Russian"), Triple("EN", "English", "Английский"))
    SettingsPage(if (ru) "Язык" else "Language", onBack) {
        OutlinedTextField(query, { query = it }, label = { Text(if (ru) "Поиск языка" else "Search languages") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        val filtered = languages.filter { (it.first + it.second + it.third).contains(query.trim(), ignoreCase = true) }
        filtered.forEach { (code, name, _) ->
            Row(Modifier.fillMaxWidth().clickable { prefs.language = code }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(name, Modifier.weight(1f))
                RadioButton(selected = code == current, onClick = { prefs.language = code })
            }
        }
        if (filtered.isEmpty()) Text(if (ru) "Язык не найден" else "No language found")
    }
}

@Composable
fun PowerSavingSettingsContent(prefs: UserPreferences, onBack: () -> Unit) {
    val revision = preferenceRevision(prefs)
    val ru = prefs.language == "RU"
    // Reading revision makes every setting reactive, including battery-triggered updates.
    val threshold = remember(revision) { prefs.powerThreshold }
    var slider by remember(threshold) { mutableFloatStateOf(threshold.toFloat()) }
    SettingsPage(if (ru) "Экономия энергии" else "Power saving", onBack) {
        SettingSwitch(if(ru) "Включить экономию сейчас" else "Enable power saving now", prefs.powerSaving) { prefs.powerSaving = it }
        SettingSwitch(if(ru) "Включать по уровню батареи" else "Enable at battery threshold", prefs.powerAutomatic) { prefs.powerAutomatic = it }
        Text(if(ru) "Порог: ${slider.toInt()}%" else "Threshold: ${slider.toInt()}%")
        Slider(value = slider, onValueChange = { slider = it }, valueRange = 1f..100f, steps = 98, onValueChangeFinished = { prefs.powerThreshold = slider.toInt() }, enabled = prefs.powerAutomatic)
        Text(if(ru) "Что отключать в режиме экономии:" else "Disable while power saving is active:")
        SettingSwitch(if(ru) "Жидкое стекло" else "Liquid glass", prefs.powerDisableLiquid) { prefs.powerDisableLiquid = it }
        SettingSwitch(if(ru) "Размытие панелей" else "Panel blur", prefs.powerDisableBlur) { prefs.powerDisableBlur = it }
        SettingSwitch(if(ru) "Фоновое сияние" else "Background glow", prefs.powerDisableGlow) { prefs.powerDisableGlow = it }
        SettingSwitch(if(ru) "Анимации в выборе GIF и стикеров" else "Animated GIF and sticker previews", prefs.powerDisablePreviews) { prefs.powerDisablePreviews = it }
        Text(if(ru) "При заряде выше порога эффекты возвращаются, если экономия не включена вручную." else "Effects return above the threshold unless power saving is enabled manually.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ServerTwoFactorSettingsContent(prefs: UserPreferences, ws: VibeWebSocket, onBack: () -> Unit) {
    val revision = preferenceRevision(prefs)
    val enabled = remember(revision) { prefs.twoFactorEnabled }
    val ru = prefs.language == "RU"
    val scope = rememberCoroutineScope()
    var current by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf(prefs.twoFactorHint.orEmpty()) }
    var busy by remember { mutableStateOf(true) }
    var ready by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf<String?>(null) }
    var confirmDisable by remember { mutableStateOf(false) }
    DisposableEffect(ws) {
        val listener = object : VibeWebSocketListener {
            override fun onSettingsResponse(message: JSONObject) {
                if (message.optString("type") == "two_factor_result") scope.launch {
                    busy = false
                    if (message.optBoolean("success")) {
                        ready = true; current = ""; password = ""; confirmation = ""
                        hint = prefs.twoFactorHint.orEmpty(); status = if (ru) "Настройки подтверждены сервером" else "Settings confirmed by server"
                    } else status = message.optString("message")
                }
            }
            override fun onDisconnected() { scope.launch { busy = false; ready = false; status = if (ru) "Нет соединения" else "Disconnected" } }
            override fun onConnected() { ws.sendRawJson("{\"type\":\"get_two_factor\"}") }
        }
        ws.addListener(listener); ws.sendRawJson("{\"type\":\"get_two_factor\"}")
        onDispose { ws.removeListener(listener) }
    }
    LaunchedEffect(busy) {
        if (busy) {
            kotlinx.coroutines.delay(15_000)
            busy = false
            status = if (ru) "Нет ответа сервера. Настройки не подтверждены." else "No server response. Settings are not confirmed."
        }
    }
    fun send(op: String) {
        if(op == "set" && (password != confirmation || password.toByteArray().size !in 6..72)) {
            status = if (ru) "Пароли должны совпадать; длина от 6 до 72 байт UTF-8." else "Passwords must match and be 6 to 72 UTF-8 bytes."; return
        }
        busy = true; status = null
        ws.sendRawJson(JSONObject().put("type", "set_two_factor").put("operation", op).put("current_password", current).put("password", password).put("hint", hint).toString())
    }
    SettingsPage(if(ru) "Двухфакторная защита" else "Two-step verification", onBack) {
        Text(if (!ready) { if(ru) "Проверка состояния на сервере" else "Checking server state" } else if (enabled) { if(ru) "Включена на сервере" else "Enabled on server" } else { if(ru) "Выключена" else "Disabled" })
        Text(if(ru) "После кода из почты потребуется этот пароль. Изменение пароля или отключение защиты завершит другие сеансы." else "This password is required after the email code. Changing or disabling it signs out other sessions.")
        if(busy) LinearProgressIndicator(Modifier.fillMaxWidth())
        if(enabled) OutlinedTextField(current,{current=it},label={Text(if(ru) "Текущий пароль" else "Current password")},visualTransformation=PasswordVisualTransformation(),singleLine=true,modifier=Modifier.fillMaxWidth())
        OutlinedTextField(password,{password=it},label={Text(if(ru) "Новый пароль" else "New password")},visualTransformation=PasswordVisualTransformation(),singleLine=true,modifier=Modifier.fillMaxWidth())
        OutlinedTextField(confirmation,{confirmation=it},label={Text(if(ru) "Повторите пароль" else "Repeat password")},visualTransformation=PasswordVisualTransformation(),singleLine=true,modifier=Modifier.fillMaxWidth())
        OutlinedTextField(hint,{hint=it.take(128)},label={Text(if(ru) "Подсказка, не сам пароль" else "Hint, not your password")},modifier=Modifier.fillMaxWidth())
        Button(onClick={send("set")},enabled=ready && !busy && password.isNotBlank() && (!enabled || current.isNotBlank())) { Text(if(ru) "Сохранить пароль" else "Save password") }
        if(enabled) {
            TextButton(onClick={send("hint")},enabled=ready && !busy && current.isNotBlank()) { Text(if(ru) "Изменить только подсказку" else "Change hint only") }
            TextButton(onClick={confirmDisable=true},enabled=ready && !busy && current.isNotBlank()) { Text(if(ru) "Отключить защиту" else "Disable protection") }
        }
        status?.let { Text(it) }
    }
    if(confirmDisable) AlertDialog(onDismissRequest={confirmDisable=false},title={Text(if(ru) "Отключить второй фактор?" else "Disable second factor?")},text={Text(if(ru) "Для следующих входов останется только код из почты." else "Future sign-ins will only require the email code.")},confirmButton={TextButton(onClick={confirmDisable=false;send("disable")}){Text(if(ru) "Отключить" else "Disable")}},dismissButton={TextButton(onClick={confirmDisable=false}){Text(if(ru) "Отмена" else "Cancel")}})
}
