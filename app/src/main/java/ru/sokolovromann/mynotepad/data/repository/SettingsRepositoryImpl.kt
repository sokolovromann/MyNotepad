package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort
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

    override suspend fun getNotesMultiColumns(): Flow<Boolean> {
        return settingsDataStore.getNotesMultiColumns()
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

    override suspend fun saveNotesMultiColumns(notesMultiColumns: Boolean) {
        settingsDataStore.saveNotesMultiColumns(notesMultiColumns)
    }

    override suspend fun clearSettings() {
        settingsDataStore.clearSettings()
    }
}