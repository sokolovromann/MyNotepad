package ru.sokolovromann.mynotepad.screens.settings

sealed class SettingsUiEvent {
    object OpenGitHub : SettingsUiEvent()
    object OpenNavigationMenu : SettingsUiEvent()
    object CloseNavigationMenu : SettingsUiEvent()
}
