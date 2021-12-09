package ru.sokolovromann.mynotepad.screens.notes

import ru.sokolovromann.mynotepad.data.local.note.Note

sealed class NotesEvent {
    object AddNoteClick : NotesEvent()
    data class NoteClick(val note: Note) : NotesEvent()
    data class OpenNoteMenu(val index: Int) : NotesEvent()
    data class DeleteNoteClick(val note: Note) : NotesEvent()
    object NoteDeletedUndoClick : NotesEvent()
    data class NavigationMenuStateChange(val isOpen: Boolean) : NotesEvent()
}
