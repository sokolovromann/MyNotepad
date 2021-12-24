package ru.sokolovromann.mynotepad.screens.welcome

sealed class WelcomeEvent {
    object SignUpClick : WelcomeEvent()
    object SignInClick : WelcomeEvent()
    object ContinueWithoutSyncClick : WelcomeEvent()
    object CloseWelcome : WelcomeEvent()
}