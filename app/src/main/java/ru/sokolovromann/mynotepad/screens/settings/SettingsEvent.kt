package ru.sokolovromann.mynotepad.screens.settings

sealed class SettingsEvent {
    data class OnAppNightThemeChange(val appNightTheme: Boolean) : SettingsEvent()
    object GitHubClick : SettingsEvent()
    data class OnNavigationMenuStateChange(val isOpen: Boolean) : SettingsEvent()
    object SignUpClick : SettingsEvent()
    object SignInClick : SettingsEvent()
    object SignOutClick : SettingsEvent()
    object ChangeEmailClick : SettingsEvent()
    object ChangePasswordClick : SettingsEvent()
    object DeleteAccountClick : SettingsEvent()
    object AccountWarningDeleteClick : SettingsEvent()
    object AccountWarningCancelClick : SettingsEvent()
}