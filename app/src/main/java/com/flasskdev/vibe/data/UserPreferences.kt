package com.flasskdev.vibe.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Stores local account state in an Android-Keystore-backed encrypted preference file.
 * The previous plaintext file is migrated once and cleared after a successful migration.
 */
class UserPreferences(context: Context) {
    private val appContext = context.applicationContext
    private val prefs: SharedPreferences = createPreferences(appContext)

    init {
        migrateLegacyPreferences(appContext)
    }

    companion object {
        private const val PREFERENCES_FILE = "vibe_secure_user_prefs"
        private const val LEGACY_PREFERENCES_FILE = "vibe_user_prefs"
        private const val KEY_MIGRATION_COMPLETE = "secure_preferences_migrated"

        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
        private const val KEY_USERNAME = "username"
        private const val KEY_NAME = "name"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_IS_DARK_THEME = "is_dark_theme"
        private const val KEY_PASSCODE = "passcode"
        private const val KEY_TWO_FACTOR_PASSWORD = "two_factor_password"
        private const val KEY_TWO_FACTOR_HINT = "two_factor_hint"
        private const val KEY_DEVICE_ID = "device_id"

        private const val KEY_PRIVACY_ACTIVITY = "privacy_activity"
        private const val KEY_PRIVACY_AVATAR = "privacy_avatar"
        private const val KEY_PRIVACY_FORWARDED = "privacy_forwarded"
        private const val KEY_PRIVACY_MESSAGES = "privacy_messages"
        private const val KEY_PRIVACY_STATUS = "privacy_status"
        private const val KEY_PRIVACY_ACTIVITY_USERS = "privacy_activity_users"
        private const val KEY_PRIVACY_AVATAR_USERS = "privacy_avatar_users"
        private const val KEY_PRIVACY_FORWARDED_USERS = "privacy_forwarded_users"
        private const val KEY_PRIVACY_MESSAGES_USERS = "privacy_messages_users"
        private const val KEY_PRIVACY_STATUS_USERS = "privacy_status_users"

        private val migrationKeys = listOf(
            KEY_USER_ID,
            KEY_EMAIL,
            KEY_USERNAME,
            KEY_NAME,
            KEY_IS_LOGGED_IN,
            KEY_LANGUAGE,
            KEY_IS_DARK_THEME,
            KEY_PASSCODE,
            KEY_DEVICE_ID,
            KEY_PRIVACY_ACTIVITY,
            KEY_PRIVACY_AVATAR,
            KEY_PRIVACY_FORWARDED,
            KEY_PRIVACY_MESSAGES,
            KEY_PRIVACY_STATUS,
            KEY_PRIVACY_ACTIVITY_USERS,
            KEY_PRIVACY_AVATAR_USERS,
            KEY_PRIVACY_FORWARDED_USERS,
            KEY_PRIVACY_MESSAGES_USERS,
            KEY_PRIVACY_STATUS_USERS
        )
    }

    private fun createPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFERENCES_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun migrateLegacyPreferences(context: Context) {
        if (prefs.getBoolean(KEY_MIGRATION_COMPLETE, false)) return

        val legacy = context.getSharedPreferences(LEGACY_PREFERENCES_FILE, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        migrationKeys.forEach { key ->
            when (val value = legacy.all[key]) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Long -> editor.putLong(key, value)
                is Float -> editor.putFloat(key, value)
            }
        }
        editor.putBoolean(KEY_MIGRATION_COMPLETE, true).commit()
        // Do not leave a backupable plaintext copy of credentials/passcode after migration.
        legacy.edit().clear().apply()
    }

    var isDarkTheme: Boolean
        get() = prefs.getBoolean(KEY_IS_DARK_THEME, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_DARK_THEME, value).apply()

    val deviceId: String
        get() {
            val existingId = prefs.getString(KEY_DEVICE_ID, null)
            if (existingId != null) return existingId

            return java.util.UUID.randomUUID().toString().also { id ->
                prefs.edit().putString(KEY_DEVICE_ID, id).apply()
            }
        }

    val deviceName: String
        get() {
            val configuredName = runCatching {
                android.provider.Settings.Global.getString(
                    appContext.contentResolver,
                    android.provider.Settings.Global.DEVICE_NAME
                )
            }.getOrNull().orEmpty()

            if (configuredName.isNotBlank()) return configuredName

            val bluetoothName = runCatching {
                android.provider.Settings.Secure.getString(appContext.contentResolver, "bluetooth_name")
            }.getOrNull().orEmpty()

            if (bluetoothName.isNotBlank()) return bluetoothName

            val manufacturer = android.os.Build.MANUFACTURER.orEmpty()
            val model = android.os.Build.MODEL.orEmpty()
            return if (model.lowercase().startsWith(manufacturer.lowercase())) {
                model.replaceFirstChar { char -> char.titlecase(java.util.Locale.getDefault()) }
            } else {
                "${manufacturer.replaceFirstChar { char -> char.titlecase(java.util.Locale.getDefault()) }} $model".trim()
            }
        }

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "RU") ?: "RU"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    /** Stored encrypted; server-side verification is still required for a real app lock. */
    var passcode: String?
        get() = prefs.getString(KEY_PASSCODE, null)
        set(value) = prefs.edit().putString(KEY_PASSCODE, value).apply()

    var twoFactorPassword: String?
        get() = prefs.getString(KEY_TWO_FACTOR_PASSWORD, null)
        set(value) = prefs.edit().putString(KEY_TWO_FACTOR_PASSWORD, value).apply()

    var twoFactorHint: String?
        get() = prefs.getString(KEY_TWO_FACTOR_HINT, null)
        set(value) = prefs.edit().putString(KEY_TWO_FACTOR_HINT, value).apply()

    var userId: Int
        get() = prefs.getInt(KEY_USER_ID, 0)
        set(value) = prefs.edit().putInt(KEY_USER_ID, value).apply()

    var email: String
        get() = prefs.getString(KEY_EMAIL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_EMAIL, value).apply()

    var username: String
        get() = prefs.getString(KEY_USERNAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USERNAME, value).apply()

    var name: String
        get() = prefs.getString(KEY_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_NAME, value).apply()

    val isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false) && userId > 0

    var privacyActivity: String
        get() = prefs.getString(KEY_PRIVACY_ACTIVITY, "EVERYONE") ?: "EVERYONE"
        set(value) = prefs.edit().putString(KEY_PRIVACY_ACTIVITY, value).apply()

    var privacyAvatar: String
        get() = prefs.getString(KEY_PRIVACY_AVATAR, "EVERYONE") ?: "EVERYONE"
        set(value) = prefs.edit().putString(KEY_PRIVACY_AVATAR, value).apply()

    var privacyForwarded: String
        get() = prefs.getString(KEY_PRIVACY_FORWARDED, "EVERYONE") ?: "EVERYONE"
        set(value) = prefs.edit().putString(KEY_PRIVACY_FORWARDED, value).apply()

    var privacyMessages: String
        get() = prefs.getString(KEY_PRIVACY_MESSAGES, "EVERYONE") ?: "EVERYONE"
        set(value) = prefs.edit().putString(KEY_PRIVACY_MESSAGES, value).apply()

    var privacyStatus: String
        get() = prefs.getString(KEY_PRIVACY_STATUS, "EVERYONE") ?: "EVERYONE"
        set(value) = prefs.edit().putString(KEY_PRIVACY_STATUS, value).apply()

    var privacyActivityUsers: String
        get() = prefs.getString(KEY_PRIVACY_ACTIVITY_USERS, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PRIVACY_ACTIVITY_USERS, value).apply()

    var privacyAvatarUsers: String
        get() = prefs.getString(KEY_PRIVACY_AVATAR_USERS, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PRIVACY_AVATAR_USERS, value).apply()

    var privacyForwardedUsers: String
        get() = prefs.getString(KEY_PRIVACY_FORWARDED_USERS, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PRIVACY_FORWARDED_USERS, value).apply()

    var privacyMessagesUsers: String
        get() = prefs.getString(KEY_PRIVACY_MESSAGES_USERS, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PRIVACY_MESSAGES_USERS, value).apply()

    var privacyStatusUsers: String
        get() = prefs.getString(KEY_PRIVACY_STATUS_USERS, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PRIVACY_STATUS_USERS, value).apply()

    fun saveLogin(userId: Int, email: String, username: String = "", name: String = "") {
        prefs.edit()
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_EMAIL, email)
            .putString(KEY_USERNAME, username)
            .putString(KEY_NAME, name)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
