package ru.sokolovromann.mynotepad.screens.changeemail

sealed class ChangeEmailEvent {
    data class OnEmailChange(val newEmail: String) : ChangeEmailEvent()
    object ChangeClick : ChangeEmailEvent()
    object CloseClick : ChangeEmailEvent()
}
