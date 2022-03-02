package ru.sokolovromann.mynotepad.screens.notes

import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort

sealed class NotesEvent {
    object AddNoteClick : NotesEvent()
    data class NoteClick(val note: Note) : NotesEvent()
    data class OpenNoteMenu(val index: Int) : NotesEvent()
    data class DeleteNoteClick(val note: Note) : NotesEvent()
    object NoteDeletedUndoClick : NotesEvent()
    data class OnNavigationMenuStateChange(val isOpen: Boolean) : NotesEvent()
    data class OnNotesSortSheetStateChange(val isOpen: Boolean) : NotesEvent()
    data class OnNotesSortChange(val notesSort: NotesSort) : NotesEvent()
    object NotesMultiColumnsClick : NotesEvent()
    object RefreshNotesClick : NotesEvent()
    data class NoteDeleted(val deletedNote: Note) : NotesEvent()
    data class NoteSaved(val noteSaved: Boolean) : NotesEvent()
}
