//nowPlaying.kt
package com.tuno.player.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuno.player.utils.SharedMusicViewModel
import com.tuno.player.utils.rememberPlayerController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.ui.res.painterResource
import com.tuno.player.R

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlaying(
        viewModel: SharedMusicViewModel,
        navController: NavController
) {
    val music = viewModel.selectedMusic.collectAsState().value
    val musicList = viewModel.musicList.collectAsState().value



    if (music != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(30.dp)
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterHorizontally),

                    horizontalArrangement = Arrangement.SpaceBetween,

                    content ={

                        Text("Back",
                           modifier =  Modifier
                               .clickable {
                                   navController.popBackStack()
                               }
                               .padding(
                                   start = 4.dp

                               ))
                        Text("Lyrics")
                    }
                )

                Column(
                   modifier =  Modifier
                       .padding(bottom = 13.dp)
                       .fillMaxSize(),
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

                    var controller = rememberPlayerController(
                        musicUri = music.contentUri,
                        musicUris = musicList.map { it.contentUri },
                        viewModel = viewModel

                    )

                    var  position = remember { mutableStateOf(0f) }
                    var duration = controller.duration.takeIf { it > 0 }?.toFloat() ?: 1f



                    LaunchedEffect(Unit) {
                        while (isActive) {
                            position.value = controller.currentPosition.toFloat()
                            delay(100)
                        }
                    }

                    Slider(
                        value = position.value.coerceIn(0f, duration),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        },

                        track = {
                            val fraction = derivedStateOf {  position.value / duration }

                                Box(Modifier.fillMaxWidth(1f)) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth(fraction.value)
                                            .align(Alignment.CenterStart)
                                            .height(3.dp)
                                            .background(Color.Green, CircleShape)
                                    )
//                                    Box(Modifier
//                                        .size(1.dp)
//                                        .background(Color.Red)
//                                    )
                                    Box(
                                        Modifier
                                            .fillMaxWidth((1f - fraction.value))
                                            .align(Alignment.CenterEnd)
                                            .height(1.dp)
                                            .background(Color.White, CircleShape)

                                    )
                                }


                        },

                        onValueChange = {
                            position.value = it
                            controller.seekTo(it.toLong())
                        },
                        valueRange = 0f..duration,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(top = 12.dp)
                            .requiredHeight(1.dp)

                    )

                    Row (
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(R.drawable.previous),
                            contentDescription = "Localized description",
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .size(27.dp)
                                .clickable() {
                                    controller.playPrevious()
                                }
                        )

                        Image(
                            painter = if (controller.isPlaying()) painterResource(R.drawable.pause) else painterResource(
                                R.drawable.play
                            ),
                            contentDescription = "Localized description",
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .size(30.dp)
                                .clickable {
                                    if (controller.isPlaying()) controller.pause() else controller.play()

                                }
                        )

                        Image(
                            painter = painterResource(R.drawable.next),
                            contentDescription = "Localized description",
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .size(27.dp)
                                .clickable() {
                                    controller.playNext()

                                }
                        )
                    }
                }

            }
        }
    } else {
        Text("No music selected.")
    }
}


