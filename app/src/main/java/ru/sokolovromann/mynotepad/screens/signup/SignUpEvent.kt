package ru.sokolovromann.mynotepad.screens.signup

sealed class SignUpEvent {
    data class OnEmailChange(val newEmail: String) : SignUpEvent()
    data class OnPasswordChange(val newPassword: String) : SignUpEvent()
    object CreateAccountClick : SignUpEvent()
    object CloseClick : SignUpEvent()
}