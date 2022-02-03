package ru.sokolovromann.mynotepad.screens.notes

data class NotesSyncState(
    val lastSync: Long = 0L,
    val syncing: Boolean = false
)
