package ru.sokolovromann.mynotepad.screens.notes

import androidx.compose.runtime.State
import ru.sokolovromann.mynotepad.data.local.note.Note

sealed class NotesState {
    object Loading : NotesState()

    data class Notes(
        val notes: List<Note>,
        val noteMenuPosition: State<Int>,
        val onNoteMenuPositionChange: (newPosition: Int) -> Unit,
        val onRestoreLastNote: () -> Unit
    ) : NotesState()

    object NotFound : NotesState()
}
