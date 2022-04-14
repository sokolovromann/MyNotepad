package ru.sokolovromann.mynotepad.screens.deleteaccount.state

data class DeleteAccountPasswordFieldState(
    val password: String,
    val showError: Boolean
) {
    companion object {
        val Default = DeleteAccountPasswordFieldState(
            password = "",
            showError = false
        )
    }
}
