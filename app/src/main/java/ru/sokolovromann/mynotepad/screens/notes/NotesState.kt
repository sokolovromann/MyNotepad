package ru.sokolovromann.mynotepad.screens.notes

import ru.sokolovromann.mynotepad.data.local.note.Note

sealed class NotesState {
    object Loading : NotesState()
    data class Notes(val notes: List<Note>) : NotesState()
    object NotFound : NotesState()
}
