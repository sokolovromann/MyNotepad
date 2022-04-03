package ru.sokolovromann.mynotepad.screens.settings

import ru.sokolovromann.mynotepad.data.local.settings.NotesSyncPeriod

sealed class SettingsEvent {
    data class OnAppNightThemeChange(val appNightTheme: Boolean) : SettingsEvent()
    data class OnNotesSaveAndCloseChange(val notesSaveAndClose: Boolean) : SettingsEvent()
    object GitHubClick : SettingsEvent()
    data class OnNavigationMenuStateChange(val isOpen: Boolean) : SettingsEvent()
    object SignUpClick : SettingsEvent()
    object SignInClick : SettingsEvent()
    object SignOutClick : SettingsEvent()
    object ChangeEmailClick : SettingsEvent()
    object ChangePasswordClick : SettingsEvent()
    object DeleteAccountClick : SettingsEvent()
    object FeedbackClick : SettingsEvent()
    object TermsClick : SettingsEvent()
    object PrivacyPolicyClick : SettingsEvent()
    object BackClick : SettingsEvent()
    data class OnSyncPeriodDialogChange(val show: Boolean) : SettingsEvent()
    data class OnNotesSyncPeriodChange(val notesSyncPeriod: NotesSyncPeriod) : SettingsEvent()
}