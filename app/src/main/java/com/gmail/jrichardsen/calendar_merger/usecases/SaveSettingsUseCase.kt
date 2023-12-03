package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.data.SettingsRepository
import com.gmail.jrichardsen.calendar_merger.entities.Settings
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val scheduleSyncServiceUseCase: ScheduleSyncServiceUseCase,
) {
    suspend operator fun invoke(settings: Settings) {
        settingsRepository.saveSettings(settings)
        scheduleSyncServiceUseCase()
    }
}