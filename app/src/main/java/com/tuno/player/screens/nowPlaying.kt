//nowPlaying.kt
package com.tuno.player.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tuno.player.utils.SharedMusicViewModel

@Composable
fun NowPlaying(viewModel: SharedMusicViewModel) {
    val music = viewModel.selectedMusic.collectAsState().value

    if (music != null) {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(0.dp)
        ) {
            AlbumArtOrFallback(
                music.albumId,
                containerModifier = Modifier. fillMaxSize(),
                imageModifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .blur(50.dp)
                    .padding(0.dp)
            )


        }
    } else {
        Text("No music selected.")
    }
}