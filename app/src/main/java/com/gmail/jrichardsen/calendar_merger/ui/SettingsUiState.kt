package com.gmail.jrichardsen.calendar_merger.ui

import androidx.annotation.StringRes
import com.gmail.jrichardsen.calendar_merger.R
import java.time.Duration

data class SettingsUiState(
    val syncIntervalOptions: List<SyncIntervalOption> = SyncIntervalOption.getDefaultOptions(),
    val selectedSyncIntervalOption: SyncIntervalOption,
) {
    constructor() : this(
        selectedSyncIntervalOption = SyncIntervalOption.getDefaultOptions()[2],
    )
}

data class SyncIntervalOption(
    @StringRes val name: Int,
    val interval: Duration?,
) {
    companion object {
        fun getDefaultOptions(): List<SyncIntervalOption> {
            return listOf(
                SyncIntervalOption(R.string.sync_interval_never, null),
                SyncIntervalOption(R.string.sync_interval_15_minutes, Duration.ofMinutes(15)),
                SyncIntervalOption(R.string.sync_interval_1_hour, Duration.ofHours(1)),
                SyncIntervalOption(R.string.sync_interval_4_hours, Duration.ofHours(4)),
                SyncIntervalOption(R.string.sync_interval_12_hours, Duration.ofHours(12)),
                SyncIntervalOption(R.string.sync_interval_24_hours, Duration.ofHours(24)),
            )
        }
    }
}