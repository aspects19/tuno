//nowPlaying.kt
package com.tuno.player.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tuno.player.utils.SharedMusicViewModel

@Composable
fun NowPlaying(viewModel: SharedMusicViewModel) {
    val music = viewModel.selectedMusic.collectAsState().value

    if (music != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Now Playing: ${music.title}")
            Text("Artist: ${music.artist}")
            Text("Album ID: ${music.albumId}")
        }
    } else {
        Text("No music selected.")
    }
}