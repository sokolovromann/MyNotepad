package ru.sokolovromann.mynotepad.screens.welcome

sealed class WelcomeState {
    object Empty : WelcomeState()
    object Loading : WelcomeState()
    object Welcome : WelcomeState()
}
