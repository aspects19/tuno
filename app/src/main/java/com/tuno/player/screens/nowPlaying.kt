//nowPlaying.kt
package com.tuno.player.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuno.player.utils.SharedMusicViewModel
import com.tuno.player.utils.rememberPlayerController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

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
            Column(
               modifier =  Modifier
                   .fillMaxSize()
                   .systemBarsPadding(),
                verticalArrangement =  Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AlbumArtOrFallback(albumId = music.albumId,
                    containerModifier = Modifier.size(250.dp ),
                    imageModifier = Modifier.clip(RoundedCornerShape(20.dp))
                )
                Text(music.title,
                    modifier = Modifier
                        .padding( 0.dp, 14.dp,0.dp,0.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 21.sp
                )
                Text(music.artist, color = Color.White)

                var controller = rememberPlayerController(musicUri = music.contentUri)

                var  position = remember { mutableStateOf(0f) }
                var duration = controller.duration.takeIf { it > 0 }?.toFloat() ?: 1f
                var isPlaying = controller.isPlaying()

                LaunchedEffect(Unit) {
                    while (isActive) {
                        position.value = controller.currentPosition.toFloat()
                        delay(500)
                    }
                }

                Slider(
                    value = position.value.coerceIn(0f, duration),
                    onValueChange = {
                        position.value = it
                        controller.seekTo(it.toLong())
                    },
                    valueRange = 0f..duration,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 12.dp)
                )
                Box(modifier = Modifier
                    .background(Color.Red)
                    .size(20.dp)
                    .clickable(

                    )
                )
            }


        }
    } else {
        Text("No music selected.")
    }
}

