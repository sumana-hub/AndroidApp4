package com.trios2024evsd.superpodcast.service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat

class PodplayMediaService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()
        createMediaSession()

    }

    override fun onLoadChildren(parentId: String,
                                result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        // To be implemented
        if (parentId.equals(PODPLAY_EMPTY_ROOT_MEDIA_ID)) {
            result.sendResult(null)
        }

    }

    override fun onGetRoot(clientPackageName: String,
                           clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        // To be implemented
        return BrowserRoot(
            PODPLAY_EMPTY_ROOT_MEDIA_ID, null)

    }

    private fun createMediaSession() {
        // 1
        mediaSession = MediaSessionCompat(this, "PodplayMediaService")
        // 2
        setSessionToken(mediaSession.sessionToken)
        // 3
        // Assign Callback
        val callback = PodplayMediaCallback(this, mediaSession)
        mediaSession.setCallback(callback)

    }
    companion object {
        private const val PODPLAY_EMPTY_ROOT_MEDIA_ID =
            "podplay_empty_root_media_id"
    }


}
