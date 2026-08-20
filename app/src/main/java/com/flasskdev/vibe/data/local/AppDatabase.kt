package com.flasskdev.vibe.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)

@Database(
    entities = [
        ChatEntity::class,
        UserCacheEntity::class,
        MessageEntity::class,
        FileCacheEntity::class
    ],
    version = 22,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_5_6 = object : androidx.room.migration.Migration(5, 6) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE messages ADD COLUMN replyToId INTEGER")
                db.execSQL("ALTER TABLE messages ADD COLUMN replyToContent TEXT")
            }
        }

        private val MIGRATION_6_7 = object : androidx.room.migration.Migration(6, 7) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE messages ADD COLUMN replyToSenderName TEXT")
            }
        }

        private val MIGRATION_7_8 = object : androidx.room.migration.Migration(7, 8) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE messages ADD COLUMN isEdited INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_8_9 = object : androidx.room.migration.Migration(8, 9) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE chats ADD COLUMN isMuted INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_9_10 = object : androidx.room.migration.Migration(9, 10) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE messages ADD COLUMN forwardedFromId INTEGER")
                db.execSQL("ALTER TABLE messages ADD COLUMN forwardedFromName TEXT")
            }
        }

        private val MIGRATION_10_11 = object : androidx.room.migration.Migration(10, 11) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE chats ADD COLUMN draft TEXT")
            }
        }

        private val MIGRATION_11_12 = object : androidx.room.migration.Migration(11, 12) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE messages ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_12_13 = object : androidx.room.migration.Migration(12, 13) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users_cache ADD COLUMN lastSeenStatus TEXT")
            }
        }

        private val MIGRATION_13_14 = object : androidx.room.migration.Migration(13, 14) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE chats ADD COLUMN canMessage INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE users_cache ADD COLUMN canMessage INTEGER NOT NULL DEFAULT 1")
            }
        }

        private val MIGRATION_14_15 = object : androidx.room.migration.Migration(14, 15) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE messages ADD COLUMN attachments TEXT")
                db.execSQL("ALTER TABLE messages ADD COLUMN uploadStatus TEXT")
                db.execSQL("ALTER TABLE messages ADD COLUMN uploadProgress INTEGER")
            }
        }

        private val MIGRATION_15_16 = object : androidx.room.migration.Migration(15, 16) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE chats ADD COLUMN lastMessageAttachments TEXT")
            }
        }
        private val MIGRATION_16_17 = object : androidx.room.migration.Migration(16, 17) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE chats ADD COLUMN isBanned INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE chats ADD COLUMN isFreezed INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE users_cache ADD COLUMN isBanned INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE users_cache ADD COLUMN isFreezed INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_17_18 = object : androidx.room.migration.Migration(17, 18) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE chats ADD COLUMN pinned INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_18_19 = object : androidx.room.migration.Migration(18, 19) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `file_cache` (`hash` TEXT NOT NULL, `url` TEXT NOT NULL, PRIMARY KEY(`hash`))")
            }
        }

        private val MIGRATION_19_20 = object : androidx.room.migration.Migration(19, 20) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE messages ADD COLUMN reactions TEXT")
            }
        }

        private val MIGRATION_20_21 = object : androidx.room.migration.Migration(20, 21) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users_cache ADD COLUMN isBlockedByMe INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE users_cache ADD COLUMN isBlockedByUser INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE chats ADD COLUMN isBlockedByMe INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE chats ADD COLUMN isBlockedByUser INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_21_22 = object : androidx.room.migration.Migration(21, 22) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE messages ADD COLUMN replyMarkup TEXT")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vibe_database"
                )
                    // Явно указываем dropAllTables, чтобы убрать предупреждение Deprecated
                    .fallbackToDestructiveMigration(dropAllTables = false)
                    .addMigrations(MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12, MIGRATION_12_13, MIGRATION_13_14, MIGRATION_14_15, MIGRATION_15_16, MIGRATION_16_17, MIGRATION_17_18, MIGRATION_18_19, MIGRATION_19_20, MIGRATION_20_21, MIGRATION_21_22)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}