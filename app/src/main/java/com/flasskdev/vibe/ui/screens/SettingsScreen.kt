package com.flasskdev.vibe.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable

import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.data.VibeWebSocket
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import androidx.activity.compose.BackHandler

@Composable
fun SettingsScreen(
    liquidState: LiquidState,
    userPreferences: UserPreferences,
    webSocket: VibeWebSocket,
    onLogout: () -> Unit,
    onNavigateToPasscodeSetup: () -> Unit,
    onProfileClick: ((userId: Int, username: String) -> Unit)? = null
) {
    var currentScreen by remember { mutableStateOf("main") }
    val scope = rememberCoroutineScope()
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current

    var blockedCount by remember { mutableIntStateOf(0) }

    var tempPrivacyActivity by remember { mutableStateOf<PrivacyType?>(null) }
    var tempPrivacyAvatar by remember { mutableStateOf<PrivacyType?>(null) }
    var tempPrivacyForwarded by remember { mutableStateOf<PrivacyType?>(null) }
    var tempPrivacyMessages by remember { mutableStateOf<PrivacyType?>(null) }
    var tempPrivacyStatus by remember { mutableStateOf<PrivacyType?>(null) }

    fun getScreenLevel(screen: String): Int = when {
        screen == "main" -> 0
        screen in listOf("privacy", "account", "devices") -> 1
        screen == "blocked_users" -> 2
        screen.startsWith("edit_") || screen.startsWith("privacy_") && !screen.startsWith("privacy_select_") -> 2
        screen.startsWith("privacy_select_") -> 3
        else -> 0
    }

    BackHandler(enabled = currentScreen != "main") {
        when {
            currentScreen == "blocked_users" -> currentScreen = "privacy"
            currentScreen.startsWith("edit_") -> currentScreen = "account"
            currentScreen.startsWith("privacy_select_") -> currentScreen = currentScreen.replace("privacy_select_", "privacy_")
            currentScreen.startsWith("privacy_") -> {
                tempPrivacyActivity = null
                tempPrivacyAvatar = null
                tempPrivacyForwarded = null
                tempPrivacyMessages = null
                tempPrivacyStatus = null
                currentScreen = "privacy"
            }
            else -> currentScreen = "main"
        }
    }

    fun saveAllPrivacySettings() {
        val settings = org.json.JSONObject().apply {
            put("activity", userPreferences.privacyActivity)
            put("activity_users", org.json.JSONArray(userPreferences.privacyActivityUsers.split(",").mapNotNull { it.trim().toIntOrNull() }))
            put("avatar", userPreferences.privacyAvatar)
            put("avatar_users", org.json.JSONArray(userPreferences.privacyAvatarUsers.split(",").mapNotNull { it.trim().toIntOrNull() }))
            put("forwarded", userPreferences.privacyForwarded)
            put("forwarded_users", org.json.JSONArray(userPreferences.privacyForwardedUsers.split(",").mapNotNull { it.trim().toIntOrNull() }))
            put("messages", userPreferences.privacyMessages)
            put("messages_users", org.json.JSONArray(userPreferences.privacyMessagesUsers.split(",").mapNotNull { it.trim().toIntOrNull() }))
            put("status", userPreferences.privacyStatus)
            put("status_users", org.json.JSONArray(userPreferences.privacyStatusUsers.split(",").mapNotNull { it.trim().toIntOrNull() }))
        }
        webSocket.updatePrivacySettings(userPreferences.userId, settings)
    }

    DisposableEffect(webSocket) {
        val listener = object : com.flasskdev.vibe.data.VibeWebSocketListener {
            override fun onPrivacySettingsResult(settings: org.json.JSONObject) {
                userPreferences.privacyActivity = settings.optString("activity", "EVERYONE")
                userPreferences.privacyActivityUsers = settings.optString("activity_users", "[]").trim('[', ']').replace("\"", "")
                userPreferences.privacyAvatar = settings.optString("avatar", "EVERYONE")
                userPreferences.privacyAvatarUsers = settings.optString("avatar_users", "[]").trim('[', ']').replace("\"", "")
                userPreferences.privacyForwarded = settings.optString("forwarded", "EVERYONE")
                userPreferences.privacyForwardedUsers = settings.optString("forwarded_users", "[]").trim('[', ']').replace("\"", "")
                userPreferences.privacyMessages = settings.optString("messages", "EVERYONE")
                userPreferences.privacyMessagesUsers = settings.optString("messages_users", "[]").trim('[', ']').replace("\"", "")
                userPreferences.privacyStatus = settings.optString("status", "EVERYONE")
                userPreferences.privacyStatusUsers = settings.optString("status_users", "[]").trim('[', ']').replace("\"", "")
            }

            override fun onBlockedCountResult(count: Int) {
                blockedCount = count
            }

            override fun onBlockUserSuccess(blockedId: Int) {
                webSocket.getBlockedCount(userPreferences.userId)
            }

            override fun onUnblockUserSuccess(blockedId: Int) {
                webSocket.getBlockedCount(userPreferences.userId)
            }
        }
        webSocket.addListener(listener)
        webSocket.getPrivacySettings(userPreferences.userId)
        webSocket.getBlockedCount(userPreferences.userId)
        onDispose { webSocket.removeListener(listener) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .liquefiable(liquidState)
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                val targetLevel = getScreenLevel(targetState)
                val initialLevel = getScreenLevel(initialState)
                if (targetLevel > initialLevel) {
                    slideInHorizontally(animationSpec = tween(300)) { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally(animationSpec = tween(300)) { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally(animationSpec = tween(300)) { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally(animationSpec = tween(300)) { width -> width } + fadeOut()
                }
            },
            label = "settings_navigation"
        ) { screen ->
            when (screen) {
                "main" -> MainSettingsContent(
                    onNavigateToPrivacy = {
                        webSocket.getBlockedCount(userPreferences.userId)
                        currentScreen = "privacy"
                    },
                    onNavigateToAccount = { currentScreen = "account" },
                    onNavigateToDevices = { currentScreen = "devices" }
                )
                "privacy" -> PrivacySettingsContent(
                    onBack = { currentScreen = "main" },
                    blockedCount = blockedCount,
                    onNavigateToBlockedUsers = { currentScreen = "blocked_users" },
                    onNavigateToPasscodeSetup = onNavigateToPasscodeSetup,
                    onNavigateToActivity = { currentScreen = "privacy_activity" },
                    onNavigateToAvatar = { currentScreen = "privacy_avatar" },
                    onNavigateToForwarded = { currentScreen = "privacy_forwarded" },
                    onNavigateToMessages = { currentScreen = "privacy_messages" },
                    onNavigateToStatus = { currentScreen = "privacy_status" }
                )
                "blocked_users" -> BlockedUsersScreen(
                    webSocket = webSocket,
                    onBack = {
                        webSocket.getBlockedCount(userPreferences.userId)
                        currentScreen = "privacy"
                    },
                    onProfileClick = { id, name ->
                        onProfileClick?.invoke(id, name)
                    }
                )
                "account" -> AccountSettingsContent(
                    userPreferences = userPreferences,
                    webSocket = webSocket,
                    onLogout = onLogout,
                    onNavigateToPrivacy = { currentScreen = "privacy" },
                    onNavigateToEditUsername = { currentScreen = "edit_username" },
                    onNavigateToEditNickname = { currentScreen = "edit_nickname" },
                    onNavigateToEditBio = { currentScreen = "edit_bio" },
                    onBack = { currentScreen = "main" }
                )
                "devices" -> DevicesScreenContent(
                    webSocket = webSocket,
                    userPreferences = userPreferences,
                    onBack = { currentScreen = "main" }
                )
                "edit_username" -> {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val db = remember { com.flasskdev.vibe.data.local.AppDatabase.getDatabase(context) }
                    val user by db.chatDao().getUserById(userPreferences.userId).collectAsState(initial = null)
                    
                    var usernameError by remember { mutableStateOf<String?>(null) }
                    var usernameSuccess by remember { mutableStateOf<String?>(null) }
                    
                    DisposableEffect(webSocket) {
                        val listener = object : com.flasskdev.vibe.data.VibeWebSocketListener {
                            override fun onAuthResponse(message: com.flasskdev.vibe.data.VibeMessage) {
                                if (message.username_taken != null) {
                                    if (message.username_taken == true) {
                                        usernameError = strings.usernameTaken
                                        usernameSuccess = null
                                    } else {
                                        usernameError = null
                                        usernameSuccess = strings.usernameAvailable
                                    }
                                }
                            }
                        }
                        webSocket.addListener(listener)
                        onDispose { webSocket.removeListener(listener) }
                    }
                    
                    EditProfileFieldContent(
                        title = strings.usernameLabel,
                        initialValue = user?.username ?: "",
                        description = strings.usernameDescription,
                        maxLength = 32,
                        icon = Icons.Rounded.AlternateEmail,
                        errorMessage = usernameError,
                        successMessage = usernameSuccess,
                        filter = { it ->
                            var newUsername = it
                            // Filter out non-English, non-digit, non-underscore
                            newUsername = newUsername.filter { char -> char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9' || char == '_' }
                            
                            // Prevent starting with digit or underscore
                            while (newUsername.isNotEmpty() && (newUsername.first().isDigit() || newUsername.first() == '_')) {
                                newUsername = newUsername.drop(1)
                            }
                            
                            // Prevent multiple underscores
                            if (newUsername.count { c -> c == '_' } > 1) {
                                val firstIndex = newUsername.indexOf('_')
                                newUsername = newUsername.filterIndexed { index, c -> c != '_' || index == firstIndex }
                            }
                            newUsername
                        },
                        onValueChange = { newValue ->
                            if (newValue.isNotEmpty() && newValue != user?.username) {
                                usernameSuccess = null
                                if (newValue.length < 4) {
                                    usernameError = strings.usernameMinLength
                                } else {
                                    usernameError = null
                                    webSocket.checkAvailability(email = "", username = newValue)
                                }
                            } else {
                                usernameError = null
                                usernameSuccess = null
                            }
                        },
                        onSave = { newValue ->
                            if (usernameError == null) {
                                scope.launch(Dispatchers.IO) {
                                    val currentUser = db.chatDao().getUserById(userPreferences.userId).firstOrNull()
                                    if (currentUser != null) {
                                        db.chatDao().insertUser(currentUser.copy(username = newValue))
                                    }
                                }
                                webSocket.updateProfile(userId = userPreferences.userId, username = newValue)
                                currentScreen = "account"
                            }
                        },
                        onBack = { currentScreen = "account" }
                    )
                }
                "edit_nickname" -> {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val db = remember { com.flasskdev.vibe.data.local.AppDatabase.getDatabase(context) }
                    val user by db.chatDao().getUserById(userPreferences.userId).collectAsState(initial = null)
                    
                    EditProfileFieldContent(
                        title = strings.nicknameLabel,
                        initialValue = user?.name ?: "",
                        description = strings.nicknameDescription,
                        maxLength = 32,
                        icon = Icons.Rounded.Person,
                        onSave = { newValue ->
                            scope.launch(Dispatchers.IO) {
                                val currentUser = db.chatDao().getUserById(userPreferences.userId).firstOrNull()
                                if (currentUser != null) {
                                    db.chatDao().insertUser(currentUser.copy(name = newValue))
                                }
                            }
                            webSocket.setNickname(email = "", nickname = newValue, userId = userPreferences.userId)
                            currentScreen = "account"
                        },
                        onBack = { currentScreen = "account" }
                    )
                }
                "edit_bio" -> {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val db = remember { com.flasskdev.vibe.data.local.AppDatabase.getDatabase(context) }
                    val user by db.chatDao().getUserById(userPreferences.userId).collectAsState(initial = null)
                    
                    EditProfileFieldContent(
                        title = strings.aboutLabel,
                        initialValue = user?.about ?: "",
                        description = strings.bioDescription,
                        maxLength = 64,
                        icon = Icons.Rounded.Info,
                        onSave = { newValue ->
                            scope.launch(Dispatchers.IO) {
                                val currentUser = db.chatDao().getUserById(userPreferences.userId).firstOrNull()
                                if (currentUser != null) {
                                    db.chatDao().insertUser(currentUser.copy(about = newValue))
                                }
                            }
                            webSocket.updateProfile(userId = userPreferences.userId, about = newValue)
                            currentScreen = "account"
                        },
                        onBack = { currentScreen = "account" }
                    )
                }
                "privacy_activity" -> {
                    val count = userPreferences.privacyActivityUsers.split(",").filter { it.isNotEmpty() }.size
                    PrivacyOptionScreen(
                        title = strings.privacyActivityTitle,
                        description = strings.privacyActivityDesc,
                        savedValue = PrivacyType.valueOf(userPreferences.privacyActivity),
                        initialValue = tempPrivacyActivity ?: PrivacyType.valueOf(userPreferences.privacyActivity),
                        selectedUsersCount = count,
                        onValueChange = { tempPrivacyActivity = it },
                        onNavigateToSelectUsers = { currentScreen = "privacy_select_activity" },
                        onSave = { 
                            userPreferences.privacyActivity = it.name
                            saveAllPrivacySettings()
                            tempPrivacyActivity = null
                            currentScreen = "privacy"
                        },
                        onBack = { 
                            tempPrivacyActivity = null
                            currentScreen = "privacy" 
                        }
                    )
                }
                "privacy_avatar" -> {
                    val count = userPreferences.privacyAvatarUsers.split(",").filter { it.isNotEmpty() }.size
                    PrivacyOptionScreen(
                        title = strings.privacyAvatarTitle,
                        description = strings.privacyAvatarDesc,
                        savedValue = PrivacyType.valueOf(userPreferences.privacyAvatar),
                        initialValue = tempPrivacyAvatar ?: PrivacyType.valueOf(userPreferences.privacyAvatar),
                        selectedUsersCount = count,
                        onValueChange = { tempPrivacyAvatar = it },
                        onNavigateToSelectUsers = { currentScreen = "privacy_select_avatar" },
                        onSave = { 
                            userPreferences.privacyAvatar = it.name
                            saveAllPrivacySettings()
                            tempPrivacyAvatar = null
                            currentScreen = "privacy"
                        },
                        onBack = { 
                            tempPrivacyAvatar = null
                            currentScreen = "privacy" 
                        }
                    )
                }
                "privacy_forwarded" -> {
                    val count = userPreferences.privacyForwardedUsers.split(",").filter { it.isNotEmpty() }.size
                    PrivacyOptionScreen(
                        title = strings.privacyForwardedTitle,
                        description = strings.privacyForwardedDesc,
                        savedValue = PrivacyType.valueOf(userPreferences.privacyForwarded),
                        initialValue = tempPrivacyForwarded ?: PrivacyType.valueOf(userPreferences.privacyForwarded),
                        selectedUsersCount = count,
                        onValueChange = { tempPrivacyForwarded = it },
                        onNavigateToSelectUsers = { currentScreen = "privacy_select_forwarded" },
                        onSave = { 
                            userPreferences.privacyForwarded = it.name
                            saveAllPrivacySettings()
                            tempPrivacyForwarded = null
                            currentScreen = "privacy"
                        },
                        onBack = { 
                            tempPrivacyForwarded = null
                            currentScreen = "privacy" 
                        }
                    )
                }
                "privacy_messages" -> {
                    val count = userPreferences.privacyMessagesUsers.split(",").filter { it.isNotEmpty() }.size
                    PrivacyOptionScreen(
                        title = strings.privacyMessagesTitle,
                        description = strings.privacyMessagesDesc,
                        savedValue = PrivacyType.valueOf(userPreferences.privacyMessages),
                        initialValue = tempPrivacyMessages ?: PrivacyType.valueOf(userPreferences.privacyMessages),
                        selectedUsersCount = count,
                        onValueChange = { tempPrivacyMessages = it },
                        onNavigateToSelectUsers = { currentScreen = "privacy_select_messages" },
                        onSave = { 
                            userPreferences.privacyMessages = it.name
                            saveAllPrivacySettings()
                            tempPrivacyMessages = null
                            currentScreen = "privacy"
                        },
                        onBack = { 
                            tempPrivacyMessages = null
                            currentScreen = "privacy" 
                        }
                    )
                }
                "privacy_status" -> {
                    val count = userPreferences.privacyStatusUsers.split(",").filter { it.isNotEmpty() }.size
                    PrivacyOptionScreen(
                        title = strings.privacyStatusTitle,
                        description = strings.privacyStatusDesc,
                        savedValue = PrivacyType.valueOf(userPreferences.privacyStatus),
                        initialValue = tempPrivacyStatus ?: PrivacyType.valueOf(userPreferences.privacyStatus),
                        selectedUsersCount = count,
                        onValueChange = { tempPrivacyStatus = it },
                        onNavigateToSelectUsers = { currentScreen = "privacy_select_status" },
                        onSave = { 
                            userPreferences.privacyStatus = it.name
                            saveAllPrivacySettings()
                            tempPrivacyStatus = null
                            currentScreen = "privacy"
                        },
                        onBack = { 
                            tempPrivacyStatus = null
                            currentScreen = "privacy" 
                        }
                    )
                }
                "privacy_select_activity" -> {
                    val currentIds = userPreferences.privacyActivityUsers.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toSet()
                    SelectPrivacyUsersScreen(
                        selectedUserIds = currentIds,
                        onUsersSelected = { set -> 
                            tempPrivacyActivity = PrivacyType.SELECTED
                            userPreferences.privacyActivity = PrivacyType.SELECTED.name
                            userPreferences.privacyActivityUsers = set.joinToString(",")
                            saveAllPrivacySettings()
                        },
                        onBack = { currentScreen = "privacy_activity" }
                    )
                }
                "privacy_select_avatar" -> {
                    val currentIds = userPreferences.privacyAvatarUsers.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toSet()
                    SelectPrivacyUsersScreen(
                        selectedUserIds = currentIds,
                        onUsersSelected = { set -> 
                            tempPrivacyAvatar = PrivacyType.SELECTED
                            userPreferences.privacyAvatar = PrivacyType.SELECTED.name
                            userPreferences.privacyAvatarUsers = set.joinToString(",")
                            saveAllPrivacySettings()
                        },
                        onBack = { currentScreen = "privacy_avatar" }
                    )
                }
                "privacy_select_forwarded" -> {
                    val currentIds = userPreferences.privacyForwardedUsers.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toSet()
                    SelectPrivacyUsersScreen(
                        selectedUserIds = currentIds,
                        onUsersSelected = { set -> 
                            tempPrivacyForwarded = PrivacyType.SELECTED
                            userPreferences.privacyForwarded = PrivacyType.SELECTED.name
                            userPreferences.privacyForwardedUsers = set.joinToString(",")
                            saveAllPrivacySettings()
                        },
                        onBack = { currentScreen = "privacy_forwarded" }
                    )
                }
                "privacy_select_messages" -> {
                    val currentIds = userPreferences.privacyMessagesUsers.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toSet()
                    SelectPrivacyUsersScreen(
                        selectedUserIds = currentIds,
                        onUsersSelected = { set -> 
                            tempPrivacyMessages = PrivacyType.SELECTED
                            userPreferences.privacyMessages = PrivacyType.SELECTED.name
                            userPreferences.privacyMessagesUsers = set.joinToString(",")
                            saveAllPrivacySettings()
                        },
                        onBack = { currentScreen = "privacy_messages" }
                    )
                }
                "privacy_select_status" -> {
                    val currentIds = userPreferences.privacyStatusUsers.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }.toSet()
                    SelectPrivacyUsersScreen(
                        selectedUserIds = currentIds,
                        onUsersSelected = { set -> 
                            tempPrivacyStatus = PrivacyType.SELECTED
                            userPreferences.privacyStatus = PrivacyType.SELECTED.name
                            userPreferences.privacyStatusUsers = set.joinToString(",")
                            saveAllPrivacySettings()
                        },
                        onBack = { currentScreen = "privacy_status" }
                    )
                }
            }
        }
    }
}