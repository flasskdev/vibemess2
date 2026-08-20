package com.flasskdev.vibe.data.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.flasskdev.vibe.data.local.AppDatabase
import com.flasskdev.vibe.data.local.FileCacheEntity
import com.flasskdev.vibe.utils.AttachmentUtils
import com.flasskdev.vibe.utils.VideoCoverGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class FileUploadWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)   // видео бывают большими
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val messageId = inputData.getInt("messageId", -1)
        if (messageId == -1) return@withContext Result.failure()

        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.chatDao()
        val message = dao.getMessageById(messageId) ?: return@withContext Result.failure()

        val attachments = message.attachments ?: return@withContext Result.success()

        val finalAttachmentIds = mutableListOf<String>()
        var allSuccess = true

        for (i in attachments.indices) {
            val filePath = attachments[i]

            if (filePath.startsWith("http") || filePath.startsWith("att_")) {
                finalAttachmentIds.add(filePath)
                continue
            }

            val file = File(filePath)
            if (!file.exists()) {
                allSuccess = false
                break
            }

            val hash = calculateMD5(file)
            val isVideo = AttachmentUtils.isPlayableVideo(file.name)

            // ИСПРАВЛЕНО: обложка генерируется ДО проверки кэша.
            // Раньше при попадании в кэш файла обложка не создавалась вообще,
            // и локальный кэш превью оставался пустым.
            val coverFile = if (isVideo) {
                runCatching { VideoCoverGenerator.create(applicationContext, file) }.getOrNull()
            } else null

            val cachedUrl = dao.getCachedFileUrl(hash)
            if (cachedUrl != null) {
                // Привязываем уже готовую локальную обложку к финальному URL,
                // чтобы VideoCover нашёл её мгновенно, без сети.
                if (coverFile != null) linkCoverToUrl(coverFile, cachedUrl)
                finalAttachmentIds.add(cachedUrl)
                continue
            }

            val mimeType = AttachmentUtils.getMimeType(file.name)
            val endpoint = AttachmentUtils.getUploadEndpointForFile(file.name)

            try {
                val progressBody = ProgressRequestBody(file, mimeType.toMediaTypeOrNull()) { progress ->
                    val overallProgress = ((i * 100) + progress) / attachments.size
                    // ИСПРАВЛЕНО: GlobalScope заменён на скоуп воркера.
                    MainScope().launch {
                        withContext(Dispatchers.IO) {
                            dao.updateUploadProgress(messageId, overallProgress)
                        }
                    }
                }

                val multipartBuilder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.name, progressBody)

                if (coverFile != null && coverFile.exists() && coverFile.length() > 0L) {
                    multipartBuilder.addFormDataPart(
                        "cover",
                        "${file.nameWithoutExtension}.cover.jpg",
                        // asRequestBody вместо readBytes(): не тянем весь JPEG в память
                        coverFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                }

                val uploadRequest = Request.Builder()
                    .url(endpoint)
                    .post(multipartBuilder.build())
                    .build()

                client.newCall(uploadRequest).execute().use { uploadResponse ->
                    val body = uploadResponse.body?.string()

                    if (uploadResponse.isSuccessful && body != null) {
                        val respObj = JSONObject(body)
                        if (respObj.optString("status") == "SUCCESS") {
                            val url = respObj.optString("url")
                            dao.insertFileCache(FileCacheEntity(hash, url))

                            // Ключевой момент: локальная обложка переименовывается
                            // под ключ финального URL, поэтому у отправителя превью
                            // появляется мгновенно и без единого запроса в сеть.
                            if (coverFile != null) linkCoverToUrl(coverFile, url)

                            finalAttachmentIds.add(url)
                        } else {
                            allSuccess = false
                        }
                    } else {
                        allSuccess = false
                    }
                }

                if (!allSuccess) break
            } catch (e: Exception) {
                e.printStackTrace()
                allSuccess = false
                break
            }
            // ИСПРАВЛЕНО: убран coverFile?.delete() из finally.
            // Он стирал только что сгенерированную обложку из кэша,
            // из-за чего превью приходилось каждый раз декодировать заново.
        }

        return@withContext if (allSuccess) {
            dao.updateUploadStatus(messageId, "SUCCESS", finalAttachmentIds)
            Result.success()
        } else {
            dao.updateUploadStatus(messageId, "FAILED", attachments)
            Result.retry()
        }
    }

    /** Копирует локально сгенерированную обложку под ключ, по которому её будет искать VideoCover. */
    private fun linkCoverToUrl(coverFile: File, url: String) {
        runCatching {
            val targetKey = VideoCoverGenerator.stableKey(url)
            val dir = File(applicationContext.cacheDir, "video_covers").apply { mkdirs() }
            val target = File(dir, "$targetKey.jpg")
            if (!target.exists() && coverFile.absolutePath != target.absolutePath) {
                coverFile.copyTo(target, overwrite = true)
            }
        }
    }

    private fun calculateMD5(file: File): String {
        val digest = java.security.MessageDigest.getInstance("MD5")
        file.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var read: Int
            while (input.read(buffer).also { r -> read = r } != -1) {
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}