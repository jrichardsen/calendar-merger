package com.gmail.jrichardsen.calendar_merger.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.jrichardsen.calendar_merger.entities.Settings
import com.gmail.jrichardsen.calendar_merger.usecases.GetSettingsUseCase
import com.gmail.jrichardsen.calendar_merger.usecases.SaveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val scope: CoroutineScope,
    getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getSettingsUseCase().collect { settings ->
                val option = uiState.value.syncIntervalOptions.find {
                    it.interval == settings.syncInterval
                }
                if (option != null) {
                    _uiState.update { it.copy(selectedSyncIntervalOption = option) }
                }
            }
        }
    }

    fun changeSyncInterval(syncIntervalOption: SyncIntervalOption) {
        _uiState.update {
            it.copy(selectedSyncIntervalOption = syncIntervalOption)
        }
        save()
    }

    private fun save() {
        scope.launch {
            val settings = Settings(
                syncInterval = uiState.value.selectedSyncIntervalOption.interval,
            )
            saveSettingsUseCase(settings)
        }
    }

}