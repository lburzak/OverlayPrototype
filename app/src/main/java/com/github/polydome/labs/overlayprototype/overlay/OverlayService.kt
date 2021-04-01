package com.github.polydome.labs.overlayprototype.overlay

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.LayoutInflater
import com.github.polydome.labs.overlayprototype.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Allows for adding an overlay, while showing a notification until it's removed.
 * The notification can be tapped to remove overlay.
 */
@AndroidEntryPoint
class OverlayService : Service() {
    companion object {
        const val ACTION_HIDE_OVERLAY = "com.github.polydome.labs.overlayprototype.HIDE_OVERLAY"
    }

    @Inject lateinit var notificationFactory: NotificationFactory
    @Inject lateinit var overlayManager: OverlayManager
    @Inject lateinit var layoutInflater: LayoutInflater

    override fun onCreate() {
        super.onCreate()
        val hideOverlayIntent: PendingIntent =
                Intent(this, OverlayService::class.java).let { intent ->
                    intent.action = ACTION_HIDE_OVERLAY
                    PendingIntent.getService(this, 0, intent, 0)
                }

        val notification = notificationFactory.overlay(this, hideOverlayIntent)
        startForeground(1, notification)
    }

    @SuppressLint("InflateParams")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_HIDE_OVERLAY) {
            overlayManager.hide()
            stopSelf()
        } else if (!overlayManager.isOverlayVisible()) {
            val overlay = layoutInflater.inflate(R.layout.overlay_view, null)
            overlayManager.show(overlay)
        }

        return START_STICKY
    }

    override fun onDestroy() {}

    override fun onBind(p0: Intent?): IBinder? = null
}