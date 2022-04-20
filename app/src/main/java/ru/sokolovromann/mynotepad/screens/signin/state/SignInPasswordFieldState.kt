package ru.sokolovromann.mynotepad.screens.signin.state

data class SignInPasswordFieldState(
    val password: String,
    val showError: Boolean
) {

    companion object {
        val Default = SignInPasswordFieldState(
            password = "",
            showError = false
        )
    }
}