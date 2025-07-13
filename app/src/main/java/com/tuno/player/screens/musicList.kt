package com.tuno.player.screens

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tuno.player.NowPlayingScreen


data class Music(
    val id: Long,
    val title: String,
    val artist: String,
    val albumId: Long
)

fun getMusicList(context: Context): List<Music> {
    val musicList = mutableListOf<Music>()
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM_ID
    )
    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

    val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

    cursor?.use {
        val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val albumIdCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

        while (it.moveToNext()) {
            val id = it.getLong(idCol)
            val title = it.getString(titleCol)
            val artist = it.getString(artistCol)
            val albumId = it.getLong(albumIdCol)
            musicList.add(Music(id, title, artist, albumId))
        }
    }

    return musicList
}

fun getAlbumArtUri(albumId: Long): Uri =
    Uri.parse("content://media/external/audio/albumart").buildUpon()
        .appendPath(albumId.toString())
        .build()

@Composable
fun MusicListScreen(context: Context, navController: NavController) {
    val musicList = remember { mutableStateListOf<Music>() }

    LaunchedEffect(Unit) {
        musicList.clear()
        musicList.addAll(getMusicList(context))
    }

    if (musicList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No music found.")
        }
    } else {
        LazyColumn {
            itemsIndexed(musicList) { index, music ->
                MusicItem(music, navController)
                if (index < musicList.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(0.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MusicItem(music: Music, navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = {
                navController.navigate( NowPlayingScreen("Macho"))
            })
    ) {
        AlbumArtOrFallback(albumId = music.albumId)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = music.title, fontWeight = FontWeight.Bold)
            Text(text = music.artist, style = MaterialTheme.typography.bodySmall)
        }

    }
}

@Composable
fun AlbumArtOrFallback(albumId: Long) {
    val uri = getAlbumArtUri(albumId)
    Box(
        modifier = Modifier
            .size(64.dp)

    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .build(),
            contentDescription = "Album Art",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp))

        )
    }
}