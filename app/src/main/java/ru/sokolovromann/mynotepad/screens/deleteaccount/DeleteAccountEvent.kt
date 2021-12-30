package ru.sokolovromann.mynotepad.screens.deleteaccount

sealed class DeleteAccountEvent {
    data class OnPasswordChange(val newPassword: String) : DeleteAccountEvent()
    object DeleteClick : DeleteAccountEvent()
    object CloseClick : DeleteAccountEvent()
}
