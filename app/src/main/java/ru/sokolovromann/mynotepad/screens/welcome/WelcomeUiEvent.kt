package ru.sokolovromann.mynotepad.screens.welcome

sealed class WelcomeUiEvent {
    object OpenSignUp : WelcomeUiEvent()
    object OpenSignIn : WelcomeUiEvent()
    object OpenNotes : WelcomeUiEvent()
}
