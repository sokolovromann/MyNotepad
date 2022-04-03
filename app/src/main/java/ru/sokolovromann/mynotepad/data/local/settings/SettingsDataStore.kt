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
    private val notesSyncPeriodKey = longPreferencesKey("notesSyncPeriod")
    private val notesMultiColumnsKey = booleanPreferencesKey("notesMultiColumns")
    private val notesSaveAndCloseKey = booleanPreferencesKey("notesSaveAndClose")

    fun getSettings(): Flow<Settings> {
        return dataStore.data.map { preferences ->
            Settings(
                appNightTheme = preferences[appNightThemeKey] ?: false,
                notesSort = enumValueOf(preferences[notesSortKey] ?: NotesSort.CREATED_ASC.name),
                notesLastSync = preferences[notesLastSyncKey] ?: 0L,
                notesSyncPeriod = NotesSyncPeriod.millisOf(preferences[notesSyncPeriodKey] ?: NotesSyncPeriod.THREE_HOURS.millis),
                notesMultiColumns = preferences[notesMultiColumnsKey] ?: false,
                notesSaveAndClose = preferences[notesSaveAndCloseKey] ?: false
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

    fun getNotesSyncPeriod(): Flow<NotesSyncPeriod> {
        return dataStore.data.map { preferences ->
            NotesSyncPeriod.millisOf(preferences[notesSyncPeriodKey] ?: NotesSyncPeriod.THREE_HOURS.millis)
        }
    }

    fun getNotesMultiColumns(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[notesMultiColumnsKey] ?: false
        }
    }

    fun getNotesSaveAndClose(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[notesSaveAndCloseKey] ?: false
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

    suspend fun saveNotesSyncPeriod(notesSyncPeriod: NotesSyncPeriod) {
        dataStore.edit { preferences ->
            preferences[notesSyncPeriodKey] = notesSyncPeriod.millis
        }
    }

    suspend fun saveNotesMultiColumns(notesMultiColumns: Boolean) {
        dataStore.edit { preferences ->
            preferences[notesMultiColumnsKey] = notesMultiColumns
        }
    }

    suspend fun saveNotesSaveAndClose(notesSaveAndClose: Boolean) {
        dataStore.edit { preferences ->
            preferences[notesSaveAndCloseKey] = notesSaveAndClose
        }
    }

    suspend fun clearSettings() {
        val defaultSettings = Settings()
        dataStore.edit { preferences ->
            preferences[appNightThemeKey] = defaultSettings.appNightTheme
            preferences[notesSortKey] = defaultSettings.notesSort.name
            preferences[notesLastSyncKey] = defaultSettings.notesLastSync
            preferences[notesSyncPeriodKey] = defaultSettings.notesSyncPeriod.millis
            preferences[notesMultiColumnsKey] = defaultSettings.notesMultiColumns
            preferences[notesSaveAndCloseKey] = defaultSettings.notesSaveAndClose
        }
    }
}