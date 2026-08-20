package com.flasskdev.vibe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val interlocutorId: Int,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int = 0,
    val isLastMessageMine: Boolean = false,
    val isLastMessageRead: Boolean = false,
    val isDeveloper: Boolean = false,
    val isVerified: Boolean = false,
    val isBanned: Boolean = false,
    val isFreezed: Boolean = false,
    val isMuted: Boolean = false,
    val draft: String? = null,
    val canMessage: Boolean = true,
    val lastMessageAttachments: List<String>? = null,
    val pinned: Boolean = false,
    val isBlockedByMe: Boolean = false,
    val isBlockedByUser: Boolean = false
)

@Entity(tableName = "users_cache")
data class UserCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Long? = null,
    val isDeveloper: Boolean = false,
    val isVerified: Boolean = false,
    val isBanned: Boolean = false,
    val isFreezed: Boolean = false,
    val registerDate: Long? = null,
    val isBot: Boolean = false,
    val about: String? = null,
    val lastSeenStatus: String? = null,
    val canMessage: Boolean = true,
    val isBlockedByMe: Boolean = false,
    val isBlockedByUser: Boolean = false
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: Int, // Убрали autoGenerate = true
    val senderId: Int,
    val receiverId: Int,
    val senderType: String = "user", // "user" или "bot"
    val content: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val replyToId: Int? = null,
    val replyToContent: String? = null,
    val replyToSenderName: String? = null,
    val forwardedFromId: Int? = null,
    val forwardedFromName: String? = null,
    val isEdited: Boolean = false,
    val isPinned: Boolean = false,
    val attachments: List<String>? = null,
    val uploadStatus: String? = null,
    val uploadProgress: Int? = null,
    val reactions: List<ReactionItem>? = null,
    val replyMarkup: ReplyMarkup? = null
)

@kotlinx.serialization.Serializable
data class InlineKeyboardButton(
    val text: String,
    val callbackData: String? = null,
    val url: String? = null,
    val bgColor: String? = null,
    val textColor: String? = null
)

@kotlinx.serialization.Serializable
data class ReplyMarkup(
    val inlineKeyboard: List<List<InlineKeyboardButton>> = emptyList()
)

@kotlinx.serialization.Serializable
data class ReactionUser(
    val userId: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@kotlinx.serialization.Serializable
data class ReactionItem(
    val emoji: String,
    val count: Int,
    val userIds: List<Int> = emptyList(),
    val users: List<ReactionUser> = emptyList()
)

@Entity(tableName = "file_cache")
data class FileCacheEntity(
    @PrimaryKey val hash: String,
    val url: String
)