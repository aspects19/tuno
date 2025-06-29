package com.tuno.player

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
        ) { isGranted: Boolean ->
            if (isGranted) {
                launchUI()
            } else {
                setContent {
                    TunoTheme {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Permission required to load music.")
                        }
                    }
                }
            }
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
}

data class Music(
    val id: Long,
    val title: String,
    val artist: String
)

fun getMusicList(context: Context): List<Music> {
    val musicList = mutableListOf<Music>()
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
    )
    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

    val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val title = it.getString(titleColumn)
            val artist = it.getString(artistColumn)
            musicList.add(Music(id, title, artist))
        }
    }

    return musicList
}

@Composable
fun MusicListScreen(context: Context) {
    val musicList = remember { mutableStateListOf<Music>() }

    LaunchedEffect(Unit) {
        val loaded = getMusicList(context)
        musicList.clear()
        musicList.addAll(loaded)
    }

    if (musicList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No music found.")
        }
    } else {
        LazyColumn {
            items(musicList) { music ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = music.title, fontWeight = FontWeight.Bold)
                    Text(text = music.artist, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
