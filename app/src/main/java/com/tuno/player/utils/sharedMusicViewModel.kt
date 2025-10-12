// SharedMusicViewModel.kt
package com.tuno.player.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.tuno.player.screens.Music

class SharedMusicViewModel : ViewModel() {
    private val _musicList = MutableStateFlow<List<Music>>(emptyList())
    val musicList: StateFlow<List<Music>> = _musicList

    private val _selectedMusic = MutableStateFlow<Music?>(null)
    val selectedMusic: StateFlow<Music?> = _selectedMusic

    fun setMusicList(music: List<Music>) {
        _musicList.value = music
    }

    fun selectMusic(music: Music) {
        _selectedMusic.value = music
    }

    fun playNext() {
        val currentList = _musicList.value
        if (currentList.isEmpty()) {
            return
        }

        val currentIndex = selectedMusic.value?.let { currentList.indexOf(it) } ?: -1
        val nextIndex = if (currentIndex == -1 || currentIndex == currentList.lastIndex) {
            0
        } else {
            currentIndex + 1
        }
        _selectedMusic.value = currentList[nextIndex]
    }
}
