package ru.sokolovromann.mynotepad.screens.addeditnote

sealed class AddEditNoteUiEvent {
    object OpenNotes : AddEditNoteUiEvent()
    object ShowSavedMessage : AddEditNoteUiEvent()
}
