package ru.sokolovromann.mynotepad.screens.settings

sealed class SettingsUiEvent {
    object OpenGitHub : SettingsUiEvent()
    object OpenNavigationMenu : SettingsUiEvent()
    object CloseNavigationMenu : SettingsUiEvent()
    object OpenSignUp : SettingsUiEvent()
    object OpenSignIn : SettingsUiEvent()
    object OpenChangeEmail : SettingsUiEvent()
    object OpenChangePassword : SettingsUiEvent()
    object OpenWelcome : SettingsUiEvent()
    object ShowNetworkErrorMessage : SettingsUiEvent()
    object ShowUnknownErrorMessage : SettingsUiEvent()
}
