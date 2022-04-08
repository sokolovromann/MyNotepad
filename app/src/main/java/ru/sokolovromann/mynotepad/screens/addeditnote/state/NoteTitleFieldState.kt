package ru.sokolovromann.mynotepad.screens.addeditnote.state

data class NoteTitleFieldState(
    val title: String,
    val showLabel: Boolean
) {
    companion object {
        val Default = NoteTitleFieldState(
            title = "",
            showLabel = true
        )
    }
}
