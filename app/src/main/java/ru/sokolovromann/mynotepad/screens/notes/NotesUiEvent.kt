package ru.sokolovromann.mynotepad.screens.notes

import ru.sokolovromann.mynotepad.data.local.note.Note

sealed class NotesUiEvent {
    object AddNote : NotesUiEvent()
    data class EditNote(val note: Note) : NotesUiEvent()
    object ShowDeletedMessage : NotesUiEvent()
    object ShowSavedMessage : NotesUiEvent()
    object OpenNavigationMenu : NotesUiEvent()
    object CloseNavigationMenu : NotesUiEvent()
    object OpenSortNotesSheet : NotesUiEvent()
    object CloseSortNotesSheet : NotesUiEvent()
}
