package com.gmail.jrichardsen.calendar_merger.usecases

import com.gmail.jrichardsen.calendar_merger.data.SettingsRepository
import com.gmail.jrichardsen.calendar_merger.entities.Settings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<Settings> {
        return settingsRepository.getSettings()
    }
}