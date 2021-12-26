package ru.sokolovromann.mynotepad.screens.signin

sealed class SignInUiEvent {
    object OpenNotes : SignInUiEvent()
    object OpenWelcome : SignInUiEvent()
    object ShowNetworkErrorMessage : SignInUiEvent()
    object ShowSignInErrorMessage: SignInUiEvent()
    object ShowUnknownErrorMessage : SignInUiEvent()
    object ShowResetPasswordMessage: SignInUiEvent()
}
