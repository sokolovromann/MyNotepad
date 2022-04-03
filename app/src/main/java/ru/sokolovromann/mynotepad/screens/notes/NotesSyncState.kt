package ru.sokolovromann.mynotepad.screens.notes

import ru.sokolovromann.mynotepad.data.local.settings.NotesSyncPeriod

data class NotesSyncState(
    val lastSync: Long = 0L,
    val period: NotesSyncPeriod = NotesSyncPeriod.THREE_HOURS,
    val syncing: Boolean = false
)
