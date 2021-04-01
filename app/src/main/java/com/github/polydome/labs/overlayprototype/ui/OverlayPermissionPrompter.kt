package com.github.polydome.labs.overlayprototype.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import javax.inject.Inject

/**
 * A headless fragment, that manages overlay permission prompting flow
 */
@RequiresApi(23)
class OverlayPermissionPrompter @Inject constructor() : Fragment() {
    companion object {
        const val TAG = "com.github.polydome.labs.PermissionPrompter"
    }

    private lateinit var overlayPromptLauncher: ActivityResultLauncher<Nothing>
    var settingsJob: CompletableDeferred<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overlayPromptLauncher =
            registerForActivityResult(PromptOverlayPermission()) { settingsJob?.complete(it) }
    }

    fun promptPermissionsAsync(): Deferred<Boolean> {
        settingsJob = CompletableDeferred()
        overlayPromptLauncher.launch(null)

        return settingsJob as Deferred<Boolean>
    }

    @RequiresApi(23)
    class PromptOverlayPermission : ActivityResultContract<Nothing, Boolean>() {
        private lateinit var context: Context

        override fun createIntent(context: Context, input: Nothing?): Intent {
            this.context = context

            return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:${context.packageName}")
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
            Settings.canDrawOverlays(context)
    }
}