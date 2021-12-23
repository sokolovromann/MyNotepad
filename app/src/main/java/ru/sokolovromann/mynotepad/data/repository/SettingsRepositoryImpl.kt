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

    override suspend fun saveAppNightTheme(nightTheme: Boolean) {
        settingsDataStore.saveAppNightTheme(nightTheme)
    }

    override suspend fun saveNotesSort(notesSort: NotesSort) {
        settingsDataStore.saveNotesSort(notesSort)
    }
}