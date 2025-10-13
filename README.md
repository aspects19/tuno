# Tuno — The Most Beautiful Music Player

Tuno is a lightweight, modern **Android music player** built entirely with **Jetpack Compose** and **ExoPlayer**.  
It focuses on simplicity, beauty, and smooth performance — just play your local music, no fluff.

## Features

- **Play local music** — scans and lists songs on your device
- **Full playback controls** — play, pause, seek, next, and previous
- **Playlist navigation** — smoothly transitions between tracks
- **Modern UI** — built with Jetpack Compose
- **Lifecycle-safe playback** — cleans up ExoPlayer properly
- **Permission handling** — supports scoped storage (tested on Android 11+)
- **Stops playback when navigating back**

## Upcoming Features

Tuno is still in active development. Planned additions include:

- **Notification controls** (MediaStyle notification with play/pause/next/previous)
- **Persistent playback** when app is minimized
- **Audio focus handling** (pause when other apps play sound, resume afterward)
- **Custom icon & dynamic theming**
- **Bluetooth / headset control support**
- **Basic unit tests and UI tests**

## Architecture

Tuno uses a **simple MVVM structure**:

- **`SharedMusicViewModel`** — holds the list of songs and the selected music
- **`PlayerController`** — wraps and manages ExoPlayer playback
- **`NowPlaying`** screen — composable that renders the UI and binds to player state

This keeps logic separate, lifecycle-safe, and easy to extend.

---

## Permissions

Tuno requires the following permissions:

```xml
<uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
