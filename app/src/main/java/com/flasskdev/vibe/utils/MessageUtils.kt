package com.flasskdev.vibe.utils

object MessageUtils {
    fun formatMessagePreview(content: String, attachments: List<String>?): String {
        val hasAttachments = !attachments.isNullOrEmpty()
        
        // System messages
        if (content.startsWith("\$\$SYSTEM\$\$PINNED_MESSAGE|")) {
            val parts = content.substringAfter("\$\$SYSTEM\$\$PINNED_MESSAGE|").split("|")
            val senderN = parts.getOrNull(0) ?: "Someone"
            val msgContent = parts.getOrNull(1) ?: ""
            return "$senderN закрепил(а) сообщение: \"$msgContent\"" 
        }

        // Video messages (кружочки)
        if (content.startsWith("video_message:")) {
            val ms = content.substringAfter("video_message:").toLongOrNull() ?: 0L
            val totalSeconds = ms / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return "📹 Видеосообщение ${String.format("%d:%02d", minutes, seconds)}"
        }

        // Stickers
        if (content.startsWith("sticker:")) {
            return "🏷️ Стикер"
        }

        // GIFs
        if (content.startsWith("gif:")) {
            return "🎞️ GIF"
        }

        // Voice messages
        val isVoiceMessage = content.startsWith("duration:") || 
                             (hasAttachments && attachments!![0].let { it.endsWith(".m4a") || it.endsWith(".mp3") } && content.startsWith("duration:"))
        if (content.startsWith("duration:")) {
            val ms = content.substringAfter("duration:").toLongOrNull() ?: 0L
            val totalSeconds = ms / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return "🎤 Голосовое сообщение ${String.format("%d:%02d", minutes, seconds)}"
        }

        
        if (hasAttachments) {
            val images = attachments!!.filter { AttachmentUtils.isImage(it) }
            val videos = attachments.filter { AttachmentUtils.isPlayableVideo(it) }
            val audios = attachments.filter { AttachmentUtils.isPlayableAudio(it) }
            val files = attachments.filter { 
                !AttachmentUtils.isImage(it) && !AttachmentUtils.isPlayableVideo(it) && !AttachmentUtils.isPlayableAudio(it) 
            }
            
            val hasCaption = content.isNotBlank()
            val count = attachments.size
            
            // Single attachment
            if (count == 1) {
                val att = attachments[0]
                return when {
                    AttachmentUtils.isImage(att) -> if (hasCaption) "🖼 $content" else "🖼 Фотография"
                    AttachmentUtils.isPlayableVideo(att) -> if (hasCaption) "🎬 $content" else "🎬 Видео"
                    AttachmentUtils.isPlayableAudio(att) -> {
                        val filename = AttachmentUtils.getFilename(att)
                        if (hasCaption) "🎵 $content" else "🎵 $filename"
                    }
                    else -> {
                        val filename = AttachmentUtils.getFilename(att)
                        if (hasCaption) "📎 $content" else "📎 $filename"
                    }
                }
            }
            
            // Multiple attachments - mixed types
            if (images.isNotEmpty() && videos.isNotEmpty()) {
                // Media album (photos + videos)
                val mediaCount = images.size + videos.size
                return if (hasCaption) "+${mediaCount - 1} $content" else formatMediaCount(mediaCount)
            }
            
            if (images.isNotEmpty() && videos.isEmpty() && audios.isEmpty() && files.isEmpty()) {
                // Only photos
                val rem = count - 1
                val suffix = formatPhotoSuffix(rem)
                return if (hasCaption) "+$rem $content" else "+$rem $suffix"
            }
            
            if (videos.isNotEmpty() && images.isEmpty() && audios.isEmpty() && files.isEmpty()) {
                // Only videos
                val rem = count - 1
                return if (hasCaption) "+$rem $content" else formatVideoCount(count)
            }
            
            if (audios.isNotEmpty() && images.isEmpty() && videos.isEmpty() && files.isEmpty()) {
                // Only audio
                return if (hasCaption) "🎵 $content" else "🎵 ${count} аудиофайлов"
            }
            
            if (files.isNotEmpty() && images.isEmpty() && videos.isEmpty() && audios.isEmpty()) {
                // Only files
                return if (hasCaption) "📎 $content" else "📎 ${count} файлов"
            }
            
            // Mixed types
            return if (hasCaption) "+${count - 1} $content" else "+${count - 1} вложений"
        }
        
        return content
    }
    
    private fun formatPhotoSuffix(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "фотография"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "фотографии"
            else -> "фотографий"
        }
    }
    
    private fun formatMediaCount(count: Int): String {
        val suffix = when {
            count % 10 == 1 && count % 100 != 11 -> "медиафайл"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "медиафайла"
            else -> "медиафайлов"
        }
        return "🖼 $count $suffix"
    }
    
    private fun formatVideoCount(count: Int): String {
        val suffix = when {
            count % 10 == 1 && count % 100 != 11 -> "видео"
            else -> "видео"
        }
        return "🎬 $count $suffix"
    }
}