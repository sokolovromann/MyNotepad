package ru.sokolovromann.mynotepad.screens.signin

sealed class SignInEvent {
    data class OnEmailChange(val newEmail: String) : SignInEvent()
    data class OnPasswordChange(val newPassword: String) : SignInEvent()
    object SignInClick : SignInEvent()
    object ResetPasswordClick : SignInEvent()
    object CloseClick : SignInEvent()
}
