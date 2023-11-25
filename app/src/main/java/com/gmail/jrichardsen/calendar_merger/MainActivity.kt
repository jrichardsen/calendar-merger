package com.gmail.jrichardsen.calendar_merger

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gmail.jrichardsen.calendar_merger.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

// TODO: add dialogs for confirmation of destructive actions
// TODO: add automatic and manual execution of synchronization
// TODO: add setting for synchronization interval
// TODO: new app icon
// TODO: add string resources and translation

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val calendarPermissionCode = 123 // You can choose any code you like

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppScreen()
            }
        }

        checkPermissions()
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            calendarPermissionCode -> {
                // TODO: proper handling of missing permissions
                // if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //     throw RuntimeException("I want the permission");
                // } else {
                //     Log.d("Calendar", "Permission granted")
                // }
            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
        )
        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            Log.d(TAG, "Missing permissions, requesting now")
            ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(),
                calendarPermissionCode
            )
        }
    }

}