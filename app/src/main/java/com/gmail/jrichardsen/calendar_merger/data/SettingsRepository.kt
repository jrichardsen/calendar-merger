package com.gmail.jrichardsen.calendar_merger.data

import androidx.datastore.core.DataStore
import com.gmail.jrichardsen.calendar_merger.entities.Settings
import com.gmail.jrichardsen.calendar_merger.entities.toSettings
import com.gmail.jrichardsen.calendar_merger.entities.toSettingsMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsDataStore: DataStore<SettingsMessage>,
) {
    fun getSettings(): Flow<Settings> {
        return settingsDataStore.data.map(SettingsMessage::toSettings)
    }

    suspend fun saveSettings(settings: Settings) {
        settingsDataStore.updateData { settingsMessage ->
            settings.toSettingsMessage(update = settingsMessage)
        }
    }
}