package ru.sokolovromann.mynotepad.screens.signup.state

data class SignUpPasswordFieldState(
    val password: String,
    val showError: Boolean
) {

    companion object {
        val Default = SignUpPasswordFieldState(
            password = "",
            showError = false
        )
    }
}
