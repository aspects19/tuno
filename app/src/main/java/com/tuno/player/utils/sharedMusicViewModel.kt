package com.tuno.player.utils

import android.net.Uri
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

    fun selectedMusicByUri(uri: Uri?) {
        val found = _musicList.value.find { it.contentUri == uri }
        _selectedMusic.value = found

    }

}
