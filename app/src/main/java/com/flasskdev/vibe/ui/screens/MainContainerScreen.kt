package com.flasskdev.vibe.ui.screens

import kotlinx.coroutines.launch
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Surface
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.ui.components.VibeBackgroundMesh
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.rememberLiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
import com.flasskdev.vibe.ui.theme.VibePrimary
import com.flasskdev.vibe.ui.theme.VibeOnBackground
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.data.local.AppDatabase
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.flasskdev.vibe.utils.ImageUtils
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.VolumeOff
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.flasskdev.vibe.ui.components.UserBadgesRow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flasskdev.vibe.ui.viewmodels.ChatViewModel
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow

enum class MainTab(val label: String) {
    CHATS("Чаты"),
    SETTINGS("Настройки"),
    PROFILE("Профиль")
}

@Composable
fun MainContainerScreen(
    webSocket: VibeWebSocket,
    onOpenChat: (interlocutorId: Int, interlocutorName: String) -> Unit,
    userPreferences: UserPreferences,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    language: String,
    onLanguageToggle: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToPasscodeSetup: () -> Unit,
    onProfileClick: ((userId: Int, username: String) -> Unit)? = null
) {
    var selectedTab by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(MainTab.CHATS) }
    var showLogoutToast by remember { mutableStateOf(false) }
    var logoutToastMessage by remember { mutableStateOf("") }
    
    val chatViewModel: ChatViewModel = viewModel()
    val chats by chatViewModel.chats.collectAsState()
    val totalUnreadCount = chats.filter { !it.chat.isMuted }.sumOf { it.chat.unreadCount }

    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current

    DisposableEffect(webSocket) {
        val listener = object : com.flasskdev.vibe.data.VibeWebSocketListener {
            override fun onForceLogout(reason: String) {
                scope.launch {
                    if (reason == "banned") {
                        logoutToastMessage = strings.accountBannedMessage
                        showLogoutToast = true
                    } else if (reason == "freezed") {
                        logoutToastMessage = strings.accountFrozenMessage
                        showLogoutToast = true
                    }
                    if (showLogoutToast) {
                        kotlinx.coroutines.delay(3000)
                    }
                    userPreferences.logout()
                    onLogout()
                }
            }
        }
        webSocket.addListener(listener)
        onDispose { webSocket.removeListener(listener) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    // Kept short and symmetric on purpose: a long transition keeps two
                    // media-heavy tab trees alive in offscreen alpha layers, which is what made
                    // the switch feel janky.
                    fadeIn(animationSpec = tween(130)) togetherWith
                        fadeOut(animationSpec = tween(130))
                },
                label = "tabTransition"
            ) { tab ->
                // ROOT CAUSE of the "Аккаунт" ghost: every tab used to share ONE LiquidState
                // whose liquefiable layer wrapped the whole screen. The `liquid()` refraction on
                // the Chats search field therefore sampled a snapshot that could still contain
                // the Settings tab, painting its "Аккаунт" row inside the search pill.
                // Each tab now owns its own layer, so a tab can only ever refract itself.
                val tabLiquidState = rememberLiquidState()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .liquefiable(tabLiquidState)
                ) {
                    when (tab) {
                        MainTab.CHATS -> ChatListScreen(
                            liquidState = tabLiquidState,
                            webSocket = webSocket,
                            onChatClick = onOpenChat,
                            viewModel = chatViewModel
                        )
                        MainTab.SETTINGS -> SettingsScreen(
                            liquidState = tabLiquidState,
                            userPreferences = userPreferences,
                            webSocket = webSocket,
                            onLogout = onLogout,
                            onNavigateToPasscodeSetup = onNavigateToPasscodeSetup,
                            onProfileClick = onProfileClick
                        )
                        MainTab.PROFILE -> ProfileScreen(
                            liquidState = tabLiquidState,
                            webSocket = webSocket,
                            userPreferences = userPreferences,
                            isDarkTheme = isDarkTheme,
                            onThemeToggle = onThemeToggle,
                            language = language,
                            onLanguageToggle = onLanguageToggle,
                            onLogout = onLogout
                        )
                    }
                }
            }
        }



        // НАВИГАЦИОННАЯ ПАНЕЛЬ
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(34.dp))
                    // The navigation shell must never share the screen LiquidState: otherwise
                    // refraction snapshots from a tab can remain over its labels after navigation.
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
                    .border(
                        width = 1.dp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(34.dp)
                    )
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    val tabWidth = maxWidth / MainTab.entries.size
                    
                    // The LEADER reaches the target quickly; the TAIL lags behind with a softer,
                    // lower-stiffness spring. Because we draw a single blob that spans from the
                    // tail to the leader, the indicator visually STRETCHES like a liquid droplet
                    // between the old and new tab, then collapses back into a pill as the tail
                    // catches up.
                    val leaderIndex by animateFloatAsState(
                        targetValue = selectedTab.ordinal.toFloat(),
                        animationSpec = spring(
                            dampingRatio = 0.72f,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "selectorLeader"
                    )
                    val tailIndex by animateFloatAsState(
                        targetValue = selectedTab.ordinal.toFloat(),
                        animationSpec = spring(
                            dampingRatio = 0.9f,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "selectorTail"
                    )

                    val leaderOffsetX = tabWidth * leaderIndex
                    val tailOffsetX = tabWidth * tailIndex

                    // The blob covers everything between the tail and the leader position.
                    val startX = if (leaderOffsetX < tailOffsetX) leaderOffsetX else tailOffsetX
                    val endX = (if (leaderOffsetX > tailOffsetX) leaderOffsetX else tailOffsetX) + tabWidth
                    val blobWidth = endX - startX
                    // 0f at rest, grows while the droplet is stretched mid-transition.
                    val elongation = ((blobWidth.value - tabWidth.value) / tabWidth.value).coerceIn(0f, 1f)

                    // VERTICAL deformation: a squash-and-stretch pulse fired on every tab change.
                    // Negative = flattened while the droplet is in flight, positive = it overshoots
                    // taller as it lands, then settles.
                    val verticalDeform = remember { Animatable(0f) }
                    LaunchedEffect(selectedTab) {
                        verticalDeform.snapTo(0f)
                        verticalDeform.animateTo(
                            targetValue = 0f,
                            animationSpec = keyframes {
                                durationMillis = 620
                                0f at 0 using FastOutSlowInEasing
                                -1f at 150 using FastOutSlowInEasing   // flattened in flight
                                0.6f at 360 using FastOutSlowInEasing  // overshoots tall on landing
                                -0.18f at 500 using FastOutSlowInEasing // small counter-bounce
                                0f at 620
                            }
                        )
                    }

                    val deform = verticalDeform.value

                    Box(
                        modifier = Modifier
                            .offset(x = startX)
                            .width(blobWidth)
                            .fillMaxHeight()
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .graphicsLayer {
                                // Horizontal droplet stretch (from the tail lag) combined with the
                                // vertical squash-stretch pulse. Volume is preserved: whatever it
                                // gains on one axis it loses on the other.
                                scaleX = (1f + elongation * 0.06f) * (1f - deform * 0.10f)
                                scaleY = (1f - elongation * 0.26f) * (1f + deform * 0.22f)
                            }
                    ) {
                         // LIQUID GLASS: a tinted translucent body + a specular sheen on the upper
                         // half + a gradient rim that is bright on top and dim at the bottom.
                         Box(
                             modifier = Modifier
                                 .matchParentSize()
                                 // A full capsule (percent = 50) reads as a droplet when stretched.
                                 .clip(androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                                 .background(
                                     Brush.linearGradient(
                                         colors = listOf(
                                             androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.30f),
                                             androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
                                             androidx.compose.material3.MaterialTheme.colorScheme.secondary.copy(alpha = 0.20f)
                                         )
                                     )
                                 )
                                 .border(
                                     width = 1.dp,
                                     brush = Brush.verticalGradient(
                                         colors = listOf(
                                             Color.White.copy(alpha = 0.42f),
                                             androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.28f),
                                             Color.White.copy(alpha = 0.10f)
                                         )
                                     ),
                                     shape = androidx.compose.foundation.shape.RoundedCornerShape(percent = 50)
                                 )
                         ) {
                             // Specular highlight: the "glass" catching light along its top edge.
                             Box(
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .fillMaxHeight(0.55f)
                                     .background(
                                         Brush.verticalGradient(
                                             colors = listOf(
                                                 Color.White.copy(alpha = 0.20f),
                                                 Color.White.copy(alpha = 0.05f),
                                                 Color.Transparent
                                             )
                                         )
                                     )
                             )
                         }
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MainTab.entries.forEach { tab ->
                            val isSelected = selectedTab == tab
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        if (selectedTab != tab) selectedTab = tab
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                    Icon(
                                        imageVector = when (tab) {
                                            MainTab.CHATS -> if (isSelected) Icons.Filled.Forum else Icons.Outlined.Forum
                                            MainTab.SETTINGS -> if (isSelected) Icons.Filled.Settings else Icons.Outlined.Settings
                                            MainTab.PROFILE -> if (isSelected) Icons.Filled.Person else Icons.Outlined.PersonOutline
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = if (isSelected) androidx.compose.material3.MaterialTheme.colorScheme.primary else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(
                                        text = tab.label,
                                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) androidx.compose.material3.MaterialTheme.colorScheme.primary else androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                    )
                                }

                                // Unclipped Badge in the top right corner
                                if (tab == MainTab.CHATS && totalUnreadCount > 0) {
                                    val formattedCount = when {
                                        totalUnreadCount >= 1_000_000 -> String.format(Locale.US, "%.1fm", totalUnreadCount / 1_000_000.0).replace(".0", "")
                                        totalUnreadCount >= 1_000 -> String.format(Locale.US, "%.1fk", totalUnreadCount / 1_000.0).replace(".0", "")
                                        else -> totalUnreadCount.toString()
                                    }
                                    
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .offset(x = 12.dp, y = (-12).dp)
                                            .background(androidx.compose.material3.MaterialTheme.colorScheme.error, CircleShape)
                                            .border(1.5.dp, androidx.compose.material3.MaterialTheme.colorScheme.surface, CircleShape)
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                            .defaultMinSize(minWidth = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = formattedCount,
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            com.flasskdev.vibe.ui.components.VibeToast(
                message = logoutToastMessage,
                isVisible = showLogoutToast,
                onDismiss = { showLogoutToast = false },
                modifier = Modifier.padding(bottom = 100.dp)
            )
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    liquidState: LiquidState,
    webSocket: VibeWebSocket,
    userPreferences: UserPreferences,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    language: String,
    onLanguageToggle: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val strings = com.flasskdev.vibe.ui.theme.LocalVibeStrings.current
    val db = remember { AppDatabase.getDatabase(context) }
    val userId = userPreferences.userId
    val user by db.chatDao().getUserById(userId).collectAsState(initial = null)

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showCopyToast by remember { mutableStateOf(false) }

    LaunchedEffect(showCopyToast) {
        if (showCopyToast) {
            kotlinx.coroutines.delay(2000)
            showCopyToast = false
        }
    }

    LaunchedEffect(userId) {
        webSocket.getUserInfo(userId)
    }

    val displayName = user?.name?.takeIf { it.isNotBlank() } ?: strings.userLabel
    val usernameText = "@${user?.username ?: displayName.lowercase(java.util.Locale.getDefault()).replace(" ", "")}"

    var showAvatarDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 16.dp, bottom = 80.dp) // space for bottom nav
        ) {
            // Top Bar - Pinned at the top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.profileTitle,
                    style = androidx.compose.material3.MaterialTheme.typography.displayMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onThemeToggle,
                        modifier = Modifier
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = strings.btnTheme,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = onLanguageToggle,
                        modifier = Modifier
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape)
                    ) {
                        Text(
                            text = language,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(com.flasskdev.vibe.ui.theme.VibePrimary)
                        .clickable { showAvatarDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (!user?.avatarUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(user?.avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = displayName.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                        .clickable { showAvatarDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(com.flasskdev.vibe.ui.theme.VibePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Edit Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = displayName,
                modifier = if (displayName.length > 24) Modifier.basicMarquee() else Modifier,
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                maxLines = 1
            )

            // Username
            Text(
                text = usernameText,
                modifier = Modifier.combinedClickable(
                    onClick = {},
                    onLongClick = {
                        clipboardManager.setText(AnnotatedString(usernameText))
                        showCopyToast = true
                    }
                ),
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Badges
            UserBadgesRow(
                isVerified = user?.isVerified == true,
                isDeveloper = user?.isDeveloper == true,
                isBot = user?.isBot == true,
                isBanned = user?.isBanned == true,
                isFreezed = user?.isFreezed == true
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!user?.about.isNullOrBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    shadowElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = strings.aboutLabel,
                            tint = com.flasskdev.vibe.ui.theme.VibePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = strings.aboutLabel,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = user?.about ?: "",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }



            // Logout Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { showLogoutDialog = true }),
                shape = RoundedCornerShape(24.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
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
                        text = strings.btnLogout,
                        color = Color(0xFFFF3B30),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        }

        if (showLogoutDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(text = strings.logoutConfirmTitle, fontWeight = FontWeight.Bold, color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground)
                },
                text = {
                    Text(text = strings.logoutConfirmText, color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
                },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }) {
                        Text(strings.logoutConfirm, color = com.flasskdev.vibe.ui.theme.VibeError, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text(strings.logoutCancel, color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground)
                    }
                },
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
            )
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            com.flasskdev.vibe.ui.components.VibeToast(
                message = strings.usernameCopied,
                isVisible = showCopyToast,
                onDismiss = { showCopyToast = false },
                modifier = Modifier.padding(bottom = 80.dp) // Offset above bottom nav bar
            )
        }
    }

    if (showAvatarDialog) {
        var cropScale by remember { mutableStateOf(1f) }
        var cropOffset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
        var boxWidthPx by remember { mutableStateOf(0f) }
        var boxHeightPx by remember { mutableStateOf(0f) }
        
        androidx.compose.ui.window.Dialog(onDismissRequest = { 
            showAvatarDialog = false 
            selectedImageUri = null 
        }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = strings.addAvatar,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Рамка с кружком
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                            .onGloballyPositioned { coordinates ->
                                boxWidthPx = coordinates.size.width.toFloat()
                                boxHeightPx = coordinates.size.height.toFloat()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                val state = androidx.compose.foundation.gestures.rememberTransformableState { zoomChange, offsetChange, _ ->
                                    cropScale = (cropScale * zoomChange).coerceIn(1f, 5f)
                                    cropOffset += offsetChange
                                }
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Preview",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .pointerInput(Unit) {
                                            detectDragGestures { _, dragAmount ->
                                                cropOffset += dragAmount
                                            }
                                        }
                                        .transformable(state = state)
                                        .graphicsLayer(
                                            scaleX = cropScale,
                                            scaleY = cropScale,
                                            translationX = cropOffset.x,
                                            translationY = cropOffset.y
                                        )
                                )
                            } else if (!user?.avatarUrl.isNullOrEmpty()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(user?.avatarUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Preview",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(140.dp).clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Choose photo",
                                    modifier = Modifier.size(48.dp),
                                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        // Затенение с вырезанным кругом (оверлей)
                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                            val radius = 70.dp.toPx()
                            val path = androidx.compose.ui.graphics.Path().apply {
                                addRect(androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.height))
                                addOval(androidx.compose.ui.geometry.Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius))
                                fillType = androidx.compose.ui.graphics.PathFillType.EvenOdd
                            }
                            drawPath(path, color = Color.Black.copy(alpha = 0.5f))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    androidx.compose.material3.Button(
                        onClick = {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(strings.choosePhoto, color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                    }
                    
                    if (selectedImageUri != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val density = androidx.compose.ui.platform.LocalDensity.current
                        androidx.compose.material3.Button(
                            onClick = {
                                val circleRadiusPx = with(density) { 70.dp.toPx() }
                                
                                val base64 = ImageUtils.compressAndEncodeImage(
                                    context, 
                                    selectedImageUri!!,
                                    scale = cropScale,
                                    offsetX = cropOffset.x,
                                    offsetY = cropOffset.y,
                                    boxWidthPx = boxWidthPx,
                                    boxHeightPx = boxHeightPx,
                                    circleRadiusPx = circleRadiusPx
                                )
                                if (base64 != null) {
                                    webSocket.uploadAvatar(userId, base64)
                                }
                                showAvatarDialog = false
                                selectedImageUri = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = com.flasskdev.vibe.ui.theme.VibePrimary)
                        ) {
                            Text(strings.doneBtn, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

private fun formatComplaintsCount(count: Int, language: String): String {
    if (count < 1000) return count.toString()
    
    val isRu = language.lowercase().startsWith("ru")
    val kSuffix = if (isRu) "тыс." else "K"
    val mSuffix = if (isRu) "млн." else "M"
    val bSuffix = if (isRu) "млрд." else "B"
    
    return when {
        count < 1_000_000 -> String.format(java.util.Locale.US, "%.1f %s", count / 1000.0, kSuffix)
        count < 1_000_000_000 -> String.format(java.util.Locale.US, "%.1f %s", count / 1_000_000.0, mSuffix)
        else -> String.format(java.util.Locale.US, "%.1f %s", count / 1_000_000_000.0, bSuffix)
    }.replace(".0", "")
}