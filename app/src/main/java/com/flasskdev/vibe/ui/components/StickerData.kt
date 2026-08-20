package com.flasskdev.vibe.ui.components

import android.content.Context

/**
 * Sticker support backed by files bundled in the app assets.
 *
 * How to add stickers:
 *   1. Create the folder  app/src/main/assets/stickers/
 *   2. Inside it, create one folder per pack, e.g.  assets/stickers/cats/
 *   3. Drop the sticker images inside, e.g.  assets/stickers/cats/happy.webp
 *      (recommended: 512x512 .webp with transparent background, animated .webp also works)
 *
 * A sticker is sent as a normal message whose content is  "sticker:<pack>/<file>",
 * e.g. "sticker:cats/happy.webp". Both users must ship the same assets to see the image;
 * a missing sticker falls back to a placeholder.
 */
data class StickerPack(
    val id: String,
    val title: String,
    /** sticker ids in the form "<pack>/<file>", ready to be embedded in a message. */
    val stickers: List<String>
)

object StickerRepository {

    private const val ROOT = "stickers"
    private val imageExtensions = setOf("webp", "png", "gif", "jpg", "jpeg")

    @Volatile
    private var cache: List<StickerPack>? = null

    /**
     * Scans app assets for sticker packs. Result is cached after the first call.
     * Returns an empty list when the assets/stickers folder does not exist yet.
     */
    fun loadPacks(context: Context): List<StickerPack> {
        cache?.let { return it }
        val packs = try {
            val packDirs = context.assets.list(ROOT)?.toList().orEmpty()
            packDirs.mapNotNull { pack ->
                val files = context.assets.list("$ROOT/$pack")?.toList().orEmpty()
                    .filter { it.substringAfterLast('.', "").lowercase() in imageExtensions }
                    .sorted()
                if (files.isEmpty()) null
                else StickerPack(
                    id = pack,
                    title = pack.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    stickers = files.map { "$pack/$it" }
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
        cache = packs
        return packs
    }

    /** Builds a Coil-loadable asset uri from a sticker id ("<pack>/<file>"). */
    fun assetUri(stickerId: String): String = "file:///android_asset/$ROOT/$stickerId"

    /** Extracts the sticker id from a message content string ("sticker:<pack>/<file>"). */
    fun idFromContent(content: String): String = content.removePrefix("sticker:")
}