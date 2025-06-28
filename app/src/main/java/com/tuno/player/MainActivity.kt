package com.tuno.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tuno.player.ui.theme.TunoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TunoTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),


                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center

                    ) {
                        Greeting(
                            name = "Jeff"
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    for (i in 1..4) {
        Text(
            modifier = Modifier.padding(3.dp),
            text = "Hello $name and me!",
        )
        HorizontalDivider()
    }
    Text(
        modifier = Modifier.background(Color.Blue),
        text = "The end"
    )
}
