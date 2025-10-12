package com.tuno.player.utils

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class PlayerController( private val player: ExoPlayer) {
    val currentPosition: Long
        get() = player.currentPosition

    val duration: Long
        get() = player.duration

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    fun play() {
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun isPlaying(): Boolean = player.isPlaying

    fun playNext() {
        player.seekToNextMediaItem()
    }
}

@Composable
fun rememberPlayerController(
    context: Context = LocalContext.current,
    musicUris: List<Uri>,
    musicUri: Uri
): PlayerController {
    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    LaunchedEffect(musicUris) {
        if (musicUris.isNotEmpty()) {
            val mediaItems = musicUris.map { MediaItem.fromUri(it) }
            player.setMediaItems(mediaItems)
            player.prepare()
            player.playWhenReady = true
        } else {
            player.stop()
            player.clearMediaItems()
        }
    }

    return remember(player) { PlayerController(player) }
}
