package ru.sokolovromann.mynotepad.screens.signup.state

data class SignUpEmailFieldState(
    val email: String,
    val showError: Boolean
) {

    companion object {
        val Default = SignUpEmailFieldState(
            email = "",
            showError = false
        )
    }
}
