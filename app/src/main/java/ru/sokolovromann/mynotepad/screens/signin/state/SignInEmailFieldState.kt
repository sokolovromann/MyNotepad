package ru.sokolovromann.mynotepad.screens.signin.state

data class SignInEmailFieldState(
    val email: String,
    val showError: Boolean
) {

    companion object {
        val Default = SignInEmailFieldState(
            email = "",
            showError = false
        )
    }
}
