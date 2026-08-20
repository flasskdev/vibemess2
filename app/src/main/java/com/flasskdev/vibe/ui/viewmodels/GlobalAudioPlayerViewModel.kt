package com.flasskdev.vibe.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AudioTrackInfo(
    val id: String,
    val url: String,
    val title: String,            // обычно имя отправителя или название трека
    val avatarUrl: String? = null,
    val subtitle: String? = null, // исполнитель / чат / подпись
    val durationMs: Long? = null  // если известна заранее, показываем в списке
)

/** Режим повтора мини-плеера. */
enum class VibeRepeatMode { OFF, ALL, ONE }

class GlobalAudioPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private var exoPlayer: ExoPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _currentTrack = MutableStateFlow<AudioTrackInfo?>(null)
    val currentTrack: StateFlow<AudioTrackInfo?> = _currentTrack.asStateFlow()

    private val _playlist = MutableStateFlow<List<AudioTrackInfo>>(emptyList())
    val playlist: StateFlow<List<AudioTrackInfo>> = _playlist.asStateFlow()

    private val _currentIndex = MutableStateFlow(-1)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _bufferedProgress = MutableStateFlow(0f)
    val bufferedProgress: StateFlow<Float> = _bufferedProgress.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _isPlayerVisible = MutableStateFlow(false)
    val isPlayerVisible: StateFlow<Boolean> = _isPlayerVisible.asStateFlow()

    private val _repeatMode = MutableStateFlow(VibeRepeatMode.OFF)
    val repeatMode: StateFlow<VibeRepeatMode> = _repeatMode.asStateFlow()

    private val _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled.asStateFlow()

    private val _speed = MutableStateFlow(1f)
    val speed: StateFlow<Float> = _speed.asStateFlow()

    private val _hasNext = MutableStateFlow(false)
    val hasNext: StateFlow<Boolean> = _hasNext.asStateFlow()

    private val _hasPrevious = MutableStateFlow(false)
    val hasPrevious: StateFlow<Boolean> = _hasPrevious.asStateFlow()

    private var progressJob: Job? = null

    init {
        com.flasskdev.vibe.utils.AudioMetadataHelper.init(application)
        exoPlayer = ExoPlayer.Builder(application).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    _isPlaying.value = playing
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    _isBuffering.value = playbackState == Player.STATE_BUFFERING
                    when (playbackState) {
                        Player.STATE_READY -> syncDuration()
                        Player.STATE_ENDED -> {
                            _isPlaying.value = false
                            _progress.value = 1f
                            _currentPosition.value = _duration.value
                        }
                    }
                    syncNavFlags()
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val index = currentMediaItemIndex
                    if (index >= 0 && index < _playlist.value.size) {
                        _currentIndex.value = index
                        _currentTrack.value = _playlist.value[index]
                    }
                    _progress.value = 0f
                    _currentPosition.value = 0L
                    _duration.value = 0L
                    syncNavFlags()
                }
            })
        }
        // ИСПРАВЛЕНО: трекер работает всегда, а не только во время play().
        // Раньше при паузе/перемотке позиция и длительность залипали.
        startProgressTracker()
    }

    // ------------------------------------------------------------- управление

    fun updateTrackTitle(trackId: String, newTitle: String) {
        _playlist.value = _playlist.value.map {
            if (it.id == trackId && it.title != newTitle) it.copy(title = newTitle) else it
        }
        if (_currentTrack.value?.id == trackId && _currentTrack.value?.title != newTitle) {
            _currentTrack.value = _currentTrack.value?.copy(title = newTitle)
        }
    }

    fun playAudio(trackInfo: AudioTrackInfo, trackPlaylist: List<AudioTrackInfo> = listOf(trackInfo)) {
        if (_currentTrack.value?.id == trackInfo.id) {
            togglePlayPause()
            return
        }

        val effectivePlaylist =
            if (trackPlaylist.any { it.id == trackInfo.id }) trackPlaylist else listOf(trackInfo)
        _playlist.value = effectivePlaylist

        val index = effectivePlaylist.indexOfFirst { it.id == trackInfo.id }.takeIf { it >= 0 } ?: 0
        _currentIndex.value = index

        exoPlayer?.apply {
            setMediaItems(effectivePlaylist.map { MediaItem.fromUri(it.url) })
            seekTo(index, 0L)
            prepare()
            play()
        }

        _currentTrack.value = effectivePlaylist[index]
        _isPlayerVisible.value = true
        _progress.value = 0f
        _bufferedProgress.value = 0f
        _currentPosition.value = 0L
        _duration.value = 0L
        syncNavFlags()
    }

    fun playNext() {
        exoPlayer?.let { if (it.hasNextMediaItem()) it.seekToNextMediaItem() }
    }

    /** Стандартное поведение: до 3 секунд — в начало трека, дальше — предыдущий. */
    fun playPrevious() {
        val player = exoPlayer ?: return
        if (player.currentPosition > 3_000L || !player.hasPreviousMediaItem()) {
            player.seekTo(0L)
        } else {
            player.seekToPreviousMediaItem()
        }
    }

    fun pause() { exoPlayer?.pause() }

    fun resume() {
        val player = exoPlayer ?: return
        if (player.playbackState == Player.STATE_IDLE ||
            player.playbackState == Player.STATE_ENDED
        ) {
            player.seekTo(player.currentMediaItemIndex, 0L)
            player.prepare()
        }
        player.play()
    }

    fun togglePlayPause() {
        if (_isPlaying.value) pause() else resume()
    }

    fun seekTo(progressFraction: Float) {
        val player = exoPlayer ?: return
        val total = player.duration.takeIf { it != C.TIME_UNSET && it > 0 } ?: return
        val newPosition = (progressFraction.coerceIn(0f, 1f) * total).toLong()
        player.seekTo(newPosition)
        _progress.value = progressFraction.coerceIn(0f, 1f)
        _currentPosition.value = newPosition
    }

    fun seekToMs(positionMs: Long) {
        val player = exoPlayer ?: return
        val total = player.duration.takeIf { it != C.TIME_UNSET && it > 0 } ?: Long.MAX_VALUE
        val target = positionMs.coerceIn(0L, total)
        player.seekTo(target)
        _currentPosition.value = target
        if (total != Long.MAX_VALUE) _progress.value = (target.toFloat() / total).coerceIn(0f, 1f)
    }

    fun skipForward(ms: Long = 15_000L) = seekToMs((exoPlayer?.currentPosition ?: 0L) + ms)

    fun skipBackward(ms: Long = 15_000L) = seekToMs((exoPlayer?.currentPosition ?: 0L) - ms)

    fun cycleRepeatMode() {
        val next = when (_repeatMode.value) {
            VibeRepeatMode.OFF -> VibeRepeatMode.ALL
            VibeRepeatMode.ALL -> VibeRepeatMode.ONE
            VibeRepeatMode.ONE -> VibeRepeatMode.OFF
        }
        _repeatMode.value = next
        exoPlayer?.repeatMode = when (next) {
            VibeRepeatMode.OFF -> Player.REPEAT_MODE_OFF
            VibeRepeatMode.ALL -> Player.REPEAT_MODE_ALL
            VibeRepeatMode.ONE -> Player.REPEAT_MODE_ONE
        }
        syncNavFlags()
    }

    fun toggleShuffle() {
        val enabled = !_shuffleEnabled.value
        _shuffleEnabled.value = enabled
        exoPlayer?.shuffleModeEnabled = enabled
        syncNavFlags()
    }

    fun cycleSpeed() {
        val speeds = listOf(1f, 1.25f, 1.5f, 2f, 0.75f)
        val next = speeds[(speeds.indexOf(_speed.value).takeIf { it >= 0 }?.plus(1) ?: 1) % speeds.size]
        _speed.value = next
        exoPlayer?.playbackParameters = PlaybackParameters(next)
    }

    fun closePlayer() {
        exoPlayer?.stop()
        exoPlayer?.clearMediaItems()
        _isPlaying.value = false
        _isBuffering.value = false
        _isPlayerVisible.value = false
        viewModelScope.launch {
            delay(350)
            if (!_isPlayerVisible.value) {
                _currentTrack.value = null
                _playlist.value = emptyList()
                _progress.value = 0f
                _bufferedProgress.value = 0f
                _currentPosition.value = 0L
                _duration.value = 0L
                _currentIndex.value = -1
            }
        }
    }

    // ---------------------------------------------------------------- внутр.

    private fun syncDuration() {
        exoPlayer?.duration?.takeIf { it != C.TIME_UNSET && it > 0 }?.let { _duration.value = it }
    }

    private fun syncNavFlags() {
        _hasNext.value = exoPlayer?.hasNextMediaItem() == true
        _hasPrevious.value = exoPlayer?.hasPreviousMediaItem() == true
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                val player = exoPlayer
                if (player != null && _isPlayerVisible.value) {
                    syncDuration()
                    val total = _duration.value
                    val current = player.currentPosition.coerceAtLeast(0L)
                    _currentPosition.value = current
                    if (total > 0) {
                        _progress.value = (current.toFloat() / total).coerceIn(0f, 1f)
                        _bufferedProgress.value =
                            (player.bufferedPosition.toFloat() / total).coerceIn(0f, 1f)
                    }
                }
                // 30 fps во время проигрывания, 4 fps в покое — заметно экономит батарею.
                delay(if (_isPlaying.value) 33L else 250L)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }
}