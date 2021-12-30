package ru.sokolovromann.mynotepad.screens.deleteaccount

sealed class DeleteAccountUiEvent {
    object OpenSettings : DeleteAccountUiEvent()
    object OpenWelcome : DeleteAccountUiEvent()
    object ShowNetworkErrorMessage : DeleteAccountUiEvent()
    object ShowUnknownErrorMessage : DeleteAccountUiEvent()
}
