package ru.sokolovromann.mynotepad.screens.changepassword.state

data class CurrentPasswordFieldState(
    val currentPassword: String,
    val showError: Boolean
) {
    companion object {
        val Default = CurrentPasswordFieldState(
            currentPassword = "",
            showError = false
        )
    }
}
