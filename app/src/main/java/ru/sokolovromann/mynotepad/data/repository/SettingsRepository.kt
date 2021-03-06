package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort
import ru.sokolovromann.mynotepad.data.local.settings.NotesSyncPeriod
import ru.sokolovromann.mynotepad.data.local.settings.Settings

interface SettingsRepository {

    suspend fun getSettings(): Flow<Settings>

    suspend fun getAppNightTheme(): Flow<Boolean>

    suspend fun getNotesSort(): Flow<NotesSort>

    suspend fun getNotesLastSync(): Flow<Long>

    suspend fun getNotesSyncPeriod(): Flow<NotesSyncPeriod>

    suspend fun getNotesMultiColumns(): Flow<Boolean>

    suspend fun getNotesSaveAndClose(): Flow<Boolean>

    suspend fun saveAppNightTheme(nightTheme: Boolean)

    suspend fun saveNotesSort(notesSort: NotesSort)

    suspend fun saveNotesLastSync(notesLastSync: Long)

    suspend fun saveNotesSyncPeriod(notesSyncPeriod: NotesSyncPeriod)

    suspend fun saveNotesMultiColumns(notesMultiColumns: Boolean)

    suspend fun saveNotesSaveAndClose(notesSaveAndClose: Boolean)

    suspend fun clearSettings()
}