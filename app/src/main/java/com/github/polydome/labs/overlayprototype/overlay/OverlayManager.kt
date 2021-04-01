package com.github.polydome.labs.overlayprototype.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Responsible for showing and hiding the overlay window.
 */
class OverlayManager @Inject constructor(private val windowManager: WindowManager) {
    private companion object {
        const val OVERLAY_FLAGS =
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
    }

    private var overlayView: WeakReference<View> = WeakReference(null)

    fun show(view: View) {
        overlayView = WeakReference(view)
        windowManager.addView(view, createFullScreenParams())
    }

    fun hide() = overlayView.get()?.let { windowManager.removeView(it) }

    fun isOverlayVisible(): Boolean = overlayView.get() != null

    fun hasPermissions(context: Context): Boolean =
            Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(context)

    @Suppress("DEPRECATION")
    private fun getWindowType(): Int = if (Build.VERSION.SDK_INT < 26)
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        else
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

    private fun createFullScreenParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                getWindowType(),
                OVERLAY_FLAGS,
                PixelFormat.TRANSLUCENT
        )
    }
}