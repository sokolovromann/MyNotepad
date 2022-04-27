package ru.sokolovromann.mynotepad.screens.notes.state

import ru.sokolovromann.mynotepad.data.local.note.Note

data class NotesItemState(
    val note: Note,
    val showMenu: Boolean,
    val maxLines: Int
)
