package ru.sokolovromann.mynotepad.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val appNightThemeKey = booleanPreferencesKey("appNightTheme")
    private val notesSortKey = stringPreferencesKey("notesSort")
    private val notesLastSyncKey = longPreferencesKey("notesLastSync")
    private val notesMultiColumnsKey = booleanPreferencesKey("notesMultiColumns")

    fun getSettings(): Flow<Settings> {
        return dataStore.data.map { preferences ->
            Settings(
                appNightTheme = preferences[appNightThemeKey] ?: false,
                notesSort = enumValueOf(preferences[notesSortKey] ?: NotesSort.CREATED_ASC.name),
                notesLastSync = preferences[notesLastSyncKey] ?: 0L,
                notesMultiColumns = preferences[notesMultiColumnsKey] ?: false
            )
        }
    }

    fun getAppNightTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[appNightThemeKey] ?: false
        }
    }

    fun getNotesSort(): Flow<NotesSort> {
        return dataStore.data.map { preferences ->
            enumValueOf(preferences[notesSortKey] ?: NotesSort.CREATED_ASC.name)
        }
    }

    fun getNotesLastSync(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[notesLastSyncKey] ?: 0L
        }
    }

    fun getNotesMultiColumns(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[notesMultiColumnsKey] ?: false
        }
    }

    suspend fun saveAppNightTheme(nightTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[appNightThemeKey] = nightTheme
        }
    }

    suspend fun saveNotesSort(notesSort: NotesSort) {
        dataStore.edit { preferences ->
            preferences[notesSortKey] = notesSort.name
        }
    }

    suspend fun saveNotesLastSync(notesLastSync: Long) {
        dataStore.edit { preferences ->
            preferences[notesLastSyncKey] = notesLastSync
        }
    }

    suspend fun saveNotesMultiColumns(notesMultiColumns: Boolean) {
        dataStore.edit { preferences ->
            preferences[notesMultiColumnsKey] = notesMultiColumns
        }
    }

    suspend fun clearSettings() {
        val defaultSettings = Settings()
        dataStore.edit { preferences ->
            preferences[appNightThemeKey] = defaultSettings.appNightTheme
            preferences[notesSortKey] = defaultSettings.notesSort.name
            preferences[notesLastSyncKey] = defaultSettings.notesLastSync
            preferences[notesMultiColumnsKey] = defaultSettings.notesMultiColumns
        }
    }
}