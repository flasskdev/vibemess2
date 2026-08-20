package com.flasskdev.vibe.data.network

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.IOException

class ProgressRequestBody(
    private val file: File,
    private val contentType: MediaType?,
    private val listener: (progress: Int) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = file.length()

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(8192)
        file.inputStream().use { inputStream ->
            var uploaded: Long = 0
            var read: Int
            
            while (inputStream.read(buffer).also { read = it } != -1) {
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                
                val progress = ((uploaded.toDouble() / fileLength.toDouble()) * 100).toInt()
                listener(progress)
            }
        }
    }
}
