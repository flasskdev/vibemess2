package com.flasskdev.vibe.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyItems

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Gif
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.flasskdev.vibe.data.network.GifItem
import com.flasskdev.vibe.data.network.GiphyApi
import com.flasskdev.vibe.ui.theme.VibePrimary
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Returns the app-wide singleton [ImageLoader] configured in MainActivity.
 *
 * IMPORTANT: previously this built a brand-new ImageLoader on every call, which meant
 * stickers and GIFs got NO shared memory/disk cache and were re-decoded / re-downloaded
 * each time the panel opened (a big source of the "panel lag"). The singleton already
 * has animated GIF/WebP decoders + a 256MB disk cache, so we simply reuse it everywhere.
 */
@Composable
fun rememberAnimatedImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember(context) { context.imageLoader }
}

/* ============================ RECENTS STORE ============================ */

/**
 * Lightweight, persistent store of recently used emojis and stickers.
 * Backed by SharedPreferences and exposed as Compose-observable snapshot lists,
 * so the "Недавние" tab updates instantly without any network or DB round-trip.
 */
object RecentsStore {
    private const val PREFS = "emoji_recents"
    private const val KEY_EMOJI = "recent_emojis"
    private const val KEY_STICKER = "recent_stickers"
    private const val SEP = "\u0001"
    private const val MAX = 40

    private val _recentEmojis = mutableStateListOf<String>()
    private val _recentStickers = mutableStateListOf<String>()

    val recentEmojis: List<String> get() = _recentEmojis
    val recentStickers: List<String> get() = _recentStickers

    @Volatile private var loaded = false

    fun ensureLoaded(context: Context) {
        if (loaded) return
        synchronized(this) {
            if (loaded) return
            val p = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            p.getString(KEY_EMOJI, "").orEmpty().split(SEP).filter { it.isNotEmpty() }
                .let { _recentEmojis.addAll(it) }
            p.getString(KEY_STICKER, "").orEmpty().split(SEP).filter { it.isNotEmpty() }
                .let { _recentStickers.addAll(it) }
            loaded = true
        }
    }

    fun addEmoji(context: Context, emoji: String) {
        _recentEmojis.remove(emoji)
        _recentEmojis.add(0, emoji)
        while (_recentEmojis.size > MAX) _recentEmojis.removeAt(_recentEmojis.lastIndex)
        persist(context, KEY_EMOJI, _recentEmojis)
    }

    fun addSticker(context: Context, stickerId: String) {
        _recentStickers.remove(stickerId)
        _recentStickers.add(0, stickerId)
        while (_recentStickers.size > MAX) _recentStickers.removeAt(_recentStickers.lastIndex)
        persist(context, KEY_STICKER, _recentStickers)
    }

    private fun persist(context: Context, key: String, values: List<String>) {
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(key, values.joinToString(SEP))
            .apply()
    }
}

private enum class PanelTab { RECENT, EMOJI, STICKERS, GIF }

/**
 * Unified picker with four sections: recents, emojis, stickers and GIFs.
 * Designed to be embedded INLINE (below the input bar, in place of the keyboard),
 * not inside a ModalBottomSheet.
 */
@Composable
fun EmojiStickerGifPanel(
    modifier: Modifier = Modifier,
    onEmojiClick: (String) -> Unit,
    onStickerClick: (String) -> Unit,
    onGifClick: (GifItem) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { RecentsStore.ensureLoaded(context) }

    // Start on "Recent" only if the user already has recents, otherwise on emojis.
    var selectedTab by remember {
        mutableStateOf(
            if (RecentsStore.recentEmojis.isNotEmpty() || RecentsStore.recentStickers.isNotEmpty())
                PanelTab.RECENT else PanelTab.EMOJI
        )
    }

    // Wrappers that record usage before delegating to the caller.
    val recordEmoji: (String) -> Unit = { emoji ->
        RecentsStore.addEmoji(context, emoji)
        onEmojiClick(emoji)
    }
    val recordSticker: (String) -> Unit = { id ->
        RecentsStore.addSticker(context, id)
        onStickerClick(id)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PanelTabButton(
                icon = { Icon(Icons.Filled.History, contentDescription = "Недавние", modifier = Modifier.size(22.dp)) },
                label = "Недавние",
                selected = selectedTab == PanelTab.RECENT,
                onClick = { selectedTab = PanelTab.RECENT }
            )
            PanelTabButton(
                icon = { Icon(Icons.Filled.EmojiEmotions, contentDescription = "Эмодзи", modifier = Modifier.size(22.dp)) },
                label = "Эмодзи",
                selected = selectedTab == PanelTab.EMOJI,
                onClick = { selectedTab = PanelTab.EMOJI }
            )
            PanelTabButton(
                icon = { Text("🏷️", fontSize = 18.sp) },
                label = "Стикеры",
                selected = selectedTab == PanelTab.STICKERS,
                onClick = { selectedTab = PanelTab.STICKERS }
            )
            PanelTabButton(
                icon = { Icon(Icons.Filled.Gif, contentDescription = "GIF", modifier = Modifier.size(26.dp)) },
                label = "GIF",
                selected = selectedTab == PanelTab.GIF,
                onClick = { selectedTab = PanelTab.GIF }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
                PanelTab.RECENT -> RecentSection(
                    onEmojiClick = recordEmoji,
                    onStickerClick = recordSticker,
                    onGoToEmoji = { selectedTab = PanelTab.EMOJI }
                )
                PanelTab.EMOJI -> EmojiSection(onEmojiClick = recordEmoji)
                PanelTab.STICKERS -> StickerSection(onStickerClick = recordSticker)
                PanelTab.GIF -> GifSection(onGifClick = onGifClick)
            }
        }
    }
}

@Composable
private fun PanelTabButton(
    icon: @Composable () -> Unit,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) VibePrimary.copy(alpha = 0.15f) else Color.Transparent
    val tint = if (selected) VibePrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        androidx.compose.runtime.CompositionLocalProvider(
            androidx.compose.material3.LocalContentColor provides tint
        ) { icon() }
        Text(text = label, color = tint, fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
    }
}

/* ----------------------------- RECENT ----------------------------- */

@Composable
private fun RecentSection(
    onEmojiClick: (String) -> Unit,
    onStickerClick: (String) -> Unit,
    onGoToEmoji: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = rememberAnimatedImageLoader()
    val recentEmojis = RecentsStore.recentEmojis
    val recentStickers = RecentsStore.recentStickers

    if (recentEmojis.isEmpty() && recentStickers.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🕓", fontSize = 40.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "Здесь появятся недавние",
                fontSize = 15.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Отправляйте эмодзи и стикеры — они будут собираться тут",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { onGoToEmoji() }
            )
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 42.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (recentEmojis.isNotEmpty()) {
            item(span = { GridItemSpanMax() }) {
                SectionHeader("Недавние эмодзи")
            }
            items(recentEmojis, key = { "re_$it" }) { emoji ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onEmojiClick(emoji) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 24.sp)
                }
            }
        }
        if (recentStickers.isNotEmpty()) {
            item(span = { GridItemSpanMax() }) {
                SectionHeader("Недавние стикеры")
            }
            items(recentStickers, key = { "rs_$it" }) { stickerId ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onStickerClick(stickerId) },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(StickerRepository.assetUri(stickerId))
                            .crossfade(false)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = "Стикер",
                        modifier = Modifier.fillMaxSize().padding(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
    )
}

/* ----------------------------- EMOJI ----------------------------- */

@Composable
private fun EmojiSection(onEmojiClick: (String) -> Unit) {
    val categories = EmojiData.categories
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    val sectionStartIndex = remember(categories) {
        val map = HashMap<String, Int>()
        var index = 0
        categories.forEach { cat ->
            map[cat.id] = index
            index += 1 + cat.emojis.size // 1 header + emojis
        }
        map
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            lazyItems(categories, key = { it.id }) { cat ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable {
                            scope.launch { gridState.animateScrollToItem(sectionStartIndex[cat.id] ?: 0) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(cat.icon, fontSize = 20.sp)
                }
            }
        }

        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(minSize = 42.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            categories.forEach { cat ->
                item(span = { GridItemSpanMax() }, key = "h_${cat.id}") {
                    SectionHeader(cat.title)
                }
                items(cat.emojis, key = { "${cat.id}_$it" }) { emoji ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onEmojiClick(emoji) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

// Helper to make a grid item span the full width (header rows).
private fun androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope.GridItemSpanMax() =
    androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan)

/* ----------------------------- STICKERS ----------------------------- */

@Composable
private fun StickerSection(onStickerClick: (String) -> Unit) {
    val context = LocalContext.current
    val imageLoader = rememberAnimatedImageLoader()
    val packs = remember { StickerRepository.loadPacks(context) }
    val allStickers = remember(packs) { packs.flatMap { it.stickers } }

    if (allStickers.isEmpty()) {
        EmptyState(
            emoji = "🏷️",
            title = "Пока нет стикеров",
            subtitle = "Добавьте изображения в assets/stickers/<набор>/ и они появятся здесь"
        )
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 84.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(allStickers, key = { it }) { stickerId ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onStickerClick(stickerId) },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(StickerRepository.assetUri(stickerId))
                        .crossfade(false)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = "Стикер",
                    modifier = Modifier.fillMaxSize().padding(6.dp)
                )
            }
        }
    }
}

/* ----------------------------- GIF ----------------------------- */

@OptIn(FlowPreview::class)
@Composable
private fun GifSection(onGifClick: (GifItem) -> Unit) {
    val context = LocalContext.current
    val imageLoader = rememberAnimatedImageLoader()

    var query by remember { mutableStateOf("") }
    var gifs by remember { mutableStateOf<List<GifItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Debounced search / initial trending load. Using snapshotFlow + debounce avoids
    // spawning/cancelling a Job on every keystroke.
    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(350)
            .distinctUntilChanged()
            .collect { q ->
                isLoading = true
                gifs = if (q.isBlank()) GiphyApi.trending() else GiphyApi.search(q)
                isLoading = false
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp),
            placeholder = { Text("Поиск GIF на GIPHY", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Filled.Close, contentDescription = "Очистить", modifier = Modifier.size(18.dp))
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VibePrimary,
                cursorColor = VibePrimary
            )
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                isLoading && gifs.isEmpty() -> {
                    CircularProgressIndicator(color = VibePrimary, modifier = Modifier.size(32.dp))
                }
                gifs.isEmpty() -> {
                    EmptyState(
                        emoji = "🔍",
                        title = "Ничего не найдено",
                        subtitle = "Попробуйте другой запрос или проверьте API-ключ GIPHY"
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(gifs, key = { it.previewUrl }) { gif ->
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                    .clickable { onGifClick(gif) }
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(gif.previewUrl)
                                        .crossfade(true)
                                        .build(),
                                    imageLoader = imageLoader,
                                    contentDescription = "GIF",
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ----------------------------- SHARED ----------------------------- */

@Composable
private fun EmptyState(emoji: String, title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 40.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            title,
            fontSize = 15.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            subtitle,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}