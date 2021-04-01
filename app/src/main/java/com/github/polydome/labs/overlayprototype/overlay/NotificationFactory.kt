package com.github.polydome.labs.overlayprototype.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.github.polydome.labs.overlayprototype.R
import javax.inject.Inject

/**
 * Allows for constructing notifications.
 */
class NotificationFactory @Inject constructor(private val notificationManager: NotificationManager) {
    private fun getChannelId(context: Context): String =
        "${context.packageName}.OVERLAY_CHANNEL"

    fun overlay(context: Context, intent: PendingIntent?): Notification {
        if (Build.VERSION.SDK_INT >= 26) {
            if (notificationManager.getNotificationChannel(getChannelId(context)) == null)
                createNotificationChannel(context)
        }

        return createOverlayNotification(context, intent)
    }

    private fun createOverlayNotification(context: Context, tapIntent: PendingIntent?) =
            NotificationCompat.Builder(context, getChannelId(context)).apply {
                setContentTitle("Overlay")
                setContentText("Tap to hide overlay")
                setContentIntent(tapIntent)
                setSmallIcon(R.mipmap.ic_launcher)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }.build()

    @RequiresApi(26)
    private fun createNotificationChannel(context: Context) {
        val name = "OverlayPrototype Foreground"
        val desc = "A channel containing foreground notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(getChannelId(context), name, importance).apply {
            description = desc
        }

        notificationManager.createNotificationChannel(channel)
    }
}