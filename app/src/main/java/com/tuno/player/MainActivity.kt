package com.tuno.player

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tuno.player.ui.theme.TunoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_AUDIO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) launchUI() else showPermissionDenied()
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            launchUI()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun launchUI() {
        setContent {
            TunoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()) {
                        MusicListScreen(context = this@MainActivity)
                    }
                }
            }
        }
    }

    private fun showPermissionDenied() {
        setContent {
            TunoTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Permission required to load music.")
                }
            }
        }
    }
}

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
fun MusicListScreen(context: Context) {
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
            items(musicList) { music ->
                MusicItem(music)
            }
        }
    }
}

@Composable
fun MusicItem(music: Music) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
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
            modifier = Modifier.fillMaxSize(),

        )
    }
}

//@Composable
//fun FallbackMusicIcon() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            imageVector = Icons.Default.MusicNote,
//            contentDescription = "Music Icon",
//            modifier = Modifier.size(32.dp),
//            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
//        )
//    }
//}
