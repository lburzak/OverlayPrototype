package com.github.polydome.labs.overlayprototype.core

import android.app.NotificationManager
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Provides system services.
 */
@Module
@InstallIn(SingletonComponent::class)
object AndroidServicesModule {
    @Provides
    fun windowManager(@ApplicationContext context: Context): WindowManager
        = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    @Provides
    fun layoutInflater(@ApplicationContext context: Context): LayoutInflater
        = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @Provides
    fun notificationManager(@ApplicationContext context: Context): NotificationManager
        = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}