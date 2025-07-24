package com.tuno.player.utils

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
}

@Composable
fun rememberPlayerController(
    context: Context = LocalContext.current,
    musicUri: Uri
): PlayerController {
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(musicUri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }
    return remember { PlayerController(player) }
}
