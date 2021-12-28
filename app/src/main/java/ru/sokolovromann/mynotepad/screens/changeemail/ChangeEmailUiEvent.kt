package ru.sokolovromann.mynotepad.screens.changeemail

sealed class ChangeEmailUiEvent {
    object OpenSettings : ChangeEmailUiEvent()
    object ShowEmailChangedMessage : ChangeEmailUiEvent()
    object ShowNetworkErrorMessage : ChangeEmailUiEvent()
    object ShowUnknownErrorMessage : ChangeEmailUiEvent()
}
