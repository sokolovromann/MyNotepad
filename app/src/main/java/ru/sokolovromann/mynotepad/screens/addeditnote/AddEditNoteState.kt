package ru.sokolovromann.mynotepad.screens.addeditnote

data class AddEditNoteState(
    val title: String = "",
    val text: String = "",
    val emptyTextError: Boolean = false
)