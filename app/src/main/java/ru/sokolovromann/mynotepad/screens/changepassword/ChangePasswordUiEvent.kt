package ru.sokolovromann.mynotepad.screens.changepassword

sealed class ChangePasswordUiEvent {
    object OpenSettings : ChangePasswordUiEvent()
    object ShowPasswordChangedMessage : ChangePasswordUiEvent()
    object ShowNetworkErrorMessage : ChangePasswordUiEvent()
    object ShowUnknownErrorMessage : ChangePasswordUiEvent()
}
