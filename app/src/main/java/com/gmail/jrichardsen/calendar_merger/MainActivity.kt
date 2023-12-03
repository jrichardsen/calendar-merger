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
import com.gmail.jrichardsen.calendar_merger.usecases.ScheduleSyncServiceUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: new app icon
// TODO: add string resources and translation
// TODO: make merged calendars read-only

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val calendarPermissionCode = 123 // You can choose any code you like

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var scheduleSyncServiceUseCase: ScheduleSyncServiceUseCase

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
                if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    throw RuntimeException("I want the permission")
                } else {
                    Log.d(TAG, "Permission granted")
                    scope.launch { scheduleSyncServiceUseCase() }
                }
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
        } else {
            scope.launch { scheduleSyncServiceUseCase() }
        }
    }

}