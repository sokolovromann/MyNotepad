package ru.sokolovromann.mynotepad.screens.changeemail.state

data class ChangeEmailEmailFieldState(
    val email: String,
    val showError: Boolean
) {
    companion object {
        val Default = ChangeEmailEmailFieldState(
            email = "",
            showError = false
        )
    }
}