package ru.sokolovromann.mynotepad.screens.addeditnote

sealed class AddEditNoteUiEvent {
    object OpenNotes : AddEditNoteUiEvent()
    object OpenNotesAfterDeleted : AddEditNoteUiEvent()
    object OpenNotesAfterSaved : AddEditNoteUiEvent()
    object ShowSavedMessage : AddEditNoteUiEvent()
}