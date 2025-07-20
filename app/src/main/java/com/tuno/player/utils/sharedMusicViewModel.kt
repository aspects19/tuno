// SharedMusicViewModel.kt
package com.tuno.player.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.tuno.player.screens.Music

class SharedMusicViewModel : ViewModel() {
    private val _selectedMusic = MutableStateFlow<Music?>(null)
    val selectedMusic: StateFlow<Music?> = _selectedMusic

    fun selectMusic(music: Music) {
        _selectedMusic.value = music
    }
}
