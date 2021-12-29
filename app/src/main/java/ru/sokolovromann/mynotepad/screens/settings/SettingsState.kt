package ru.sokolovromann.mynotepad.screens.settings

import ru.sokolovromann.mynotepad.data.local.settings.Settings

data class SettingsState(
    val settings: Settings = Settings(),
    val deleteAccountWarning: Boolean = false,
    val loading: Boolean = false
)
