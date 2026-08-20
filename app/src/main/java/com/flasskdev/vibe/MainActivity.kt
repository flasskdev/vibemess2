package com.flasskdev.vibe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.flasskdev.vibe.data.UserPreferences
import com.flasskdev.vibe.data.VibeWebSocket
import com.flasskdev.vibe.navigation.VibeNavGraph
import com.flasskdev.vibe.ui.theme.VibeTheme
import com.google.firebase.messaging.FirebaseMessaging
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flasskdev.vibe.ui.viewmodels.GlobalAudioPlayerViewModel
import com.flasskdev.vibe.ui.components.GlobalMediaPlayer
import com.flasskdev.vibe.ui.components.ExpandedAudioPlayerSheet
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import android.os.Build
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder

class MainActivity : ComponentActivity() {
    private val webSocket = VibeWebSocket()
    private lateinit var userPreferences: UserPreferences

    companion object {
        var isUnlocked = false
    }


    fun attemptWebSocketConnection() {
        if (!::userPreferences.isInitialized) return
        
        if (!userPreferences.isLoggedIn) {
            webSocket.connect()
            return
        }
        
        if (userPreferences.passcode != null && !isUnlocked) {
            // Do not connect yet, wait for passcode
            return
        }
        
        webSocket.connect()
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("VibeFCM", "FCM Token: $token")
            webSocket.authConnect(userPreferences.userId, token, userPreferences.deviceId, userPreferences.deviceName)
        }.addOnFailureListener {
            Log.e("VibeFCM", "Failed to get FCM token", it)
            webSocket.authConnect(userPreferences.userId, deviceId = userPreferences.deviceId, deviceName = userPreferences.deviceName)
        }
    }

    override fun onStart() {
        super.onStart()
        attemptWebSocketConnection()
    }

    override fun onStop() {
        super.onStop()
        webSocket.disconnect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val imageLoader = ImageLoader.Builder(this)
            .components {
                // >>> ГЛАВНОЕ ИСПРАВЛЕНИЕ <<<
                // Без VideoFrameDecoder вызов videoFrameMillis() в VideoCover.kt
                // просто игнорируется, Coil пытается декодировать .mp4 как картинку,
                // падает в onError -> и рисуется синий фон с иконкой видео.
                add(VideoFrameDecoder.Factory())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    // Было maxSizePercent(0.02) — это ~2% свободного места.
                    // VideoFrameDecoder для HTTP-видео обязан положить файл на диск,
                    // при таком крохотном кэше он постоянно вытесняется и обложка
                    // удалённого видео не декодируется вообще.
                    .maxSizeBytes(256L * 1024 * 1024)
                    .build()
            }
            .respectCacheHeaders(false)
            .crossfade(true)
            .build()

        Coil.setImageLoader(imageLoader)
        
        userPreferences = UserPreferences(this)

        setContent {
            var isDarkTheme by remember { mutableStateOf(userPreferences.isDarkTheme) }
            var currentLanguage by remember { mutableStateOf(userPreferences.language) }
            val currentStrings = if (currentLanguage == "RU") com.flasskdev.vibe.ui.theme.ruStrings else com.flasskdev.vibe.ui.theme.enStrings

            androidx.compose.runtime.CompositionLocalProvider(
                com.flasskdev.vibe.ui.theme.LocalVibeStrings provides currentStrings
            ) {
                com.flasskdev.vibe.ui.theme.VibeTheme(darkTheme = isDarkTheme) {
                    val navController = rememberNavController()
                    val audioPlayerViewModel: GlobalAudioPlayerViewModel = viewModel()
                    val hazeState = remember { HazeState() }
                    var showExpandedPlayer by remember { mutableStateOf(false) }

                    androidx.compose.runtime.CompositionLocalProvider(
                        LocalGlobalAudioPlayer provides audioPlayerViewModel
                    ) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
                        ) { _ ->
                            Box(modifier = Modifier.fillMaxSize().haze(hazeState)) {
                                VibeNavGraph(
                                    navController = navController, 
                                    webSocket = webSocket,
                                    userPreferences = userPreferences,
                                    isDarkTheme = isDarkTheme,
                                    onThemeToggle = { 
                                        isDarkTheme = !isDarkTheme 
                                        userPreferences.isDarkTheme = isDarkTheme
                                    },
                                    language = currentLanguage,
                                    onLanguageToggle = { 
                                        val newLang = if (currentLanguage == "RU") "EN" else "RU"
                                        currentLanguage = newLang
                                        userPreferences.language = newLang
                                    }
                                )
                            }
                            
                            if (showExpandedPlayer) {
                                ExpandedAudioPlayerSheet(
                                    viewModel = audioPlayerViewModel,
                                    hazeState = hazeState,
                                    onDismiss = { showExpandedPlayer = false }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket.disconnect()
    }
}