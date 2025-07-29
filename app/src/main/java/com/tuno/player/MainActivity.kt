//MainActivity.kt
package com.tuno.player

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tuno.player.screens.MusicListScreen
import com.tuno.player.screens.NowPlaying
import com.tuno.player.ui.theme.TunoTheme
import com.tuno.player.utils.SharedMusicViewModel
import kotlinx.serialization.Serializable

@Serializable
object MusicListScreen
@Serializable
data class NowPlayingScreen(
    val id: Long
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        MainController(statusBarPadding = innerPadding)
                    }
                }
            }
        }
    }

    @Composable
    private fun MainController(statusBarPadding: PaddingValues) {
        val navController = rememberNavController()
        val musicViewModel: SharedMusicViewModel = viewModel()
        NavHost(
            navController = navController,
            startDestination = MusicListScreen
            ) {
            composable<MusicListScreen> {
                MusicListScreen(
                    context = LocalContext.current,
                    statusBarPadding = statusBarPadding,
                    navController = navController,
                    viewmodel = musicViewModel
                )
            }
            composable<NowPlayingScreen> {
                NowPlaying(
                    viewModel = musicViewModel,
                    navController = navController
                ) }
        }
    }


    private fun showPermissionDenied() {
        setContent {
            TunoTheme {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    Text("Permission required to load music.")
                }
            }
        }
    }
}
