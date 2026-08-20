package com.flasskdev.vibe.utils

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaMetadataRetriever
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class AudioMetadata(
    val title: String,
    val artist: String,
    val durationMs: Long,
    val coverArt: ByteArray? = null
) {
    val displayTitle: String
        get() = if (title.isNotBlank() && !title.startsWith("audio_")) title else "Аудиозапись"
        
    val displayArtist: String
        get() = if (artist.isNotBlank() && artist != "Unknown Artist") artist else ""
}

object AudioMetadataHelper {
    private var prefs: SharedPreferences? = null
    private val cache = LruCache<String, AudioMetadata>(300)

    fun init(context: Context) {
        if (prefs == null) {
            try {
                prefs = context.applicationContext.getSharedPreferences("audio_metadata_cache_v2", Context.MODE_PRIVATE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCachedMetadata(url: String): AudioMetadata? {
        // 1. In-memory cache
        cache.get(url)?.let { return it }

        // 2. Persistent disk cache
        prefs?.let { p ->
            val jsonStr = p.getString(url, null)
            if (!jsonStr.isNullOrEmpty()) {
                try {
                    val obj = JSONObject(jsonStr)
                    val title = obj.optString("title", "")
                    val artist = obj.optString("artist", "")
                    val durationMs = obj.optLong("durationMs", 0L)
                    if (title.isNotBlank()) {
                        val meta = AudioMetadata(title = title, artist = artist, durationMs = durationMs)
                        cache.put(url, meta)
                        return meta
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    suspend fun getMetadata(url: String): AudioMetadata = withContext(Dispatchers.IO) {
        getCachedMetadata(url)?.let { cached ->
            if (cached.title.isNotBlank() && !cached.title.startsWith("audio_") && cached.durationMs > 0L) {
                return@withContext cached
            }
        }

        var retriever: MediaMetadataRetriever? = null
        try {
            retriever = MediaMetadataRetriever()
            if (url.startsWith("http")) {
                retriever.setDataSource(url, emptyMap())
            } else {
                retriever.setDataSource(url)
            }
            
            var title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)?.trim()
            var artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)?.trim()
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val durationMs = durationStr?.toLongOrNull() ?: 0L
            val coverArt = retriever.embeddedPicture

            val rawFilename = if (url.startsWith("http")) {
                url.substringAfterLast("/").substringBeforeLast(".")
            } else {
                java.io.File(url).nameWithoutExtension
            }

            if (title.isNullOrBlank()) {
                if (rawFilename.contains(" - ")) {
                    val parts = rawFilename.split(" - ", limit = 2)
                    if (artist.isNullOrBlank()) artist = parts[0].trim()
                    title = parts[1].trim()
                } else if (!rawFilename.startsWith("audio_") && !rawFilename.matches(Regex("^[0-9a-fA-F_-]{16,}$"))) {
                    title = rawFilename
                } else {
                    title = "Аудиозапись"
                }
            }
            if (artist.isNullOrBlank()) {
                artist = "Unknown Artist"
            }

            val metadata = AudioMetadata(
                title = title,
                artist = artist,
                durationMs = durationMs,
                coverArt = coverArt
            )
            cache.put(url, metadata)

            // Persist to disk
            try {
                prefs?.edit()?.apply {
                    val obj = JSONObject()
                    obj.put("title", metadata.title)
                    obj.put("artist", metadata.artist)
                    obj.put("durationMs", metadata.durationMs)
                    putString(url, obj.toString())
                    apply()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            metadata
        } catch (e: Exception) {
            val rawFilename = if (url.startsWith("http")) {
                url.substringAfterLast("/").substringBeforeLast(".")
            } else {
                java.io.File(url).nameWithoutExtension
            }
            val safeTitle = if (!rawFilename.startsWith("audio_") && !rawFilename.matches(Regex("^[0-9a-fA-F_-]{16,}$"))) rawFilename else "Аудиозапись"
            val metadata = AudioMetadata(safeTitle, "Unknown Artist", 0L, null)
            cache.put(url, metadata)
            metadata
        } finally {
            try {
                retriever?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
