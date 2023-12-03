package com.gmail.jrichardsen.calendar_merger.entities

import com.gmail.jrichardsen.calendar_merger.data.SettingsMessage
import java.time.Duration

data class Settings(
    val syncInterval: Duration?,
) {
    companion object {
        fun getDefault(): Settings {
            return Settings(
                syncInterval = Duration.ofHours(1)
            )
        }
    }
}

fun SettingsMessage.toSettings(): Settings {
    val syncInterval = if (hasSyncIntervalMinutes()) {
        Duration.ofMinutes(syncIntervalMinutes)
    } else {
        null
    }
    return Settings(
        syncInterval = syncInterval,
    )
}

fun Settings.toSettingsMessage(update: SettingsMessage? = null): SettingsMessage {
    val builder = if (update != null) {
        SettingsMessage.newBuilder(update)
    } else {
        SettingsMessage.newBuilder()
    }
    if (syncInterval != null) {
        builder.syncIntervalMinutes = syncInterval.toMinutes()
    } else {
        builder.clearSyncIntervalMinutes()
    }
    return builder.build()
}
