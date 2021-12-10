package ru.sokolovromann.mynotepad.screens.settings

import ru.sokolovromann.mynotepad.data.local.settings.Settings

sealed class SettingsState {
    object Empty : SettingsState()
    object Loading : SettingsState()
    data class SettingsDisplay(val settings: Settings) : SettingsState()
}
