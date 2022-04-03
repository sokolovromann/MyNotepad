package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort
import ru.sokolovromann.mynotepad.data.local.settings.NotesSyncPeriod
import ru.sokolovromann.mynotepad.data.local.settings.Settings
import ru.sokolovromann.mynotepad.data.local.settings.SettingsDataStore
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {

    override suspend fun getSettings(): Flow<Settings> {
        return settingsDataStore.getSettings()
    }

    override suspend fun getAppNightTheme(): Flow<Boolean> {
        return settingsDataStore.getAppNightTheme()
    }

    override suspend fun getNotesSort(): Flow<NotesSort> {
        return settingsDataStore.getNotesSort()
    }

    override suspend fun getNotesLastSync(): Flow<Long> {
        return settingsDataStore.getNotesLastSync()
    }

    override suspend fun getNotesSyncPeriod(): Flow<NotesSyncPeriod> {
        return settingsDataStore.getNotesSyncPeriod()
    }

    override suspend fun getNotesMultiColumns(): Flow<Boolean> {
        return settingsDataStore.getNotesMultiColumns()
    }

    override suspend fun getNotesSaveAndClose(): Flow<Boolean> {
        return settingsDataStore.getNotesSaveAndClose()
    }

    override suspend fun saveAppNightTheme(nightTheme: Boolean) {
        settingsDataStore.saveAppNightTheme(nightTheme)
    }

    override suspend fun saveNotesSort(notesSort: NotesSort) {
        settingsDataStore.saveNotesSort(notesSort)
    }

    override suspend fun saveNotesLastSync(notesLastSync: Long) {
        settingsDataStore.saveNotesLastSync(notesLastSync)
    }

    override suspend fun saveNotesSyncPeriod(notesSyncPeriod: NotesSyncPeriod) {
        settingsDataStore.saveNotesSyncPeriod(notesSyncPeriod)
    }

    override suspend fun saveNotesMultiColumns(notesMultiColumns: Boolean) {
        settingsDataStore.saveNotesMultiColumns(notesMultiColumns)
    }

    override suspend fun saveNotesSaveAndClose(notesSaveAndClose: Boolean) {
        settingsDataStore.saveNotesSaveAndClose(notesSaveAndClose)
    }

    override suspend fun clearSettings() {
        settingsDataStore.clearSettings()
    }
}