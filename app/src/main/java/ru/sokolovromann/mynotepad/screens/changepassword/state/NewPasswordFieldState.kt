package ru.sokolovromann.mynotepad.screens.changepassword.state

data class NewPasswordFieldState(
    val newPassword: String,
    val showError: Boolean
) {
    companion object {
        val Default = NewPasswordFieldState(
            newPassword = "",
            showError = false
        )
    }
}
