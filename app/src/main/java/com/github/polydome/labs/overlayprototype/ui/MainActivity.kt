package com.github.polydome.labs.overlayprototype.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.polydome.labs.overlayprototype.R
import com.github.polydome.labs.overlayprototype.overlay.OverlayManager
import com.github.polydome.labs.overlayprototype.overlay.OverlayService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Application entry point.
 * Allows for starting [OverlayService] if drawing over other apps is permitted.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var overlayPermissionPrompter: OverlayPermissionPrompter
    @Inject lateinit var overlayManager: OverlayManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.start).apply {
            setOnClickListener { CoroutineScope(Dispatchers.IO).launch { showOverlay() } }
        }
    }

    private suspend fun showOverlay() {
        if (!overlayManager.hasPermissions(this)) {
            if (!overlayPermissionPrompter.isAdded)
                withContext(Dispatchers.Main) { addPermissionPrompter() }

            val isPermissionGranted = overlayPermissionPrompter.promptPermissionsAsync().await()

            if (!isPermissionGranted) {
                Log.e("MainActivity", "Overlay permission denied")
                return
            }
        }

        Intent(this, OverlayService::class.java)
            .also(::startService)
    }

    @RequiresApi(23)
    private fun addPermissionPrompter() {
        supportFragmentManager.beginTransaction()
            .add(overlayPermissionPrompter, OverlayPermissionPrompter.TAG)
            .commitNow()
    }
}