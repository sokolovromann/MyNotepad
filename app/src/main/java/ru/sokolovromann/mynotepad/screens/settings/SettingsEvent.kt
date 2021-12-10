package ru.sokolovromann.mynotepad.screens.settings

sealed class SettingsEvent {
    data class OnAppNightThemeChange(val appNightTheme: Boolean) : SettingsEvent()
    object GitHubClick : SettingsEvent()
    data class OnNavigationMenuStateChange(val isOpen: Boolean) : SettingsEvent()
}