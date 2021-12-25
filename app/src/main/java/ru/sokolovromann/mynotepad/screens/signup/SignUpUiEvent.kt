package ru.sokolovromann.mynotepad.screens.signup

sealed class SignUpUiEvent {
    object OpenNotes : SignUpUiEvent()
    object OpenWelcome : SignUpUiEvent()
    object ShowNetworkErrorMessage : SignUpUiEvent()
    object ShowUnknownErrorMessage : SignUpUiEvent()
}
