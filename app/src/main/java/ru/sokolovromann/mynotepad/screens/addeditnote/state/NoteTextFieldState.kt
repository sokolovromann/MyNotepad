package ru.sokolovromann.mynotepad.screens.addeditnote.state

data class NoteTextFieldState(
    val text: String,
    val showLabel: Boolean,
    val showError: Boolean
) {
    companion object {
        val Default = NoteTextFieldState(
            text = "",
            showLabel = true,
            showError = false
        )
    }
}
