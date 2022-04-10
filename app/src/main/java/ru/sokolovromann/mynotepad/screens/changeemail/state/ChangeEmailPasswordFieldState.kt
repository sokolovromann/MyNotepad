package ru.sokolovromann.mynotepad.screens.changeemail.state

data class ChangeEmailPasswordFieldState(
    val password: String,
    val showError: Boolean
) {
    companion object {
        val Default = ChangeEmailPasswordFieldState(
            password = "",
            showError = false
        )
    }
}
