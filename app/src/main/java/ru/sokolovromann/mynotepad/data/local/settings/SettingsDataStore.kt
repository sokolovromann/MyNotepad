package ru.sokolovromann.mynotepad.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(
    private val dataStore: DataStore<Preferences>
) {
    private val appNightThemeKey = booleanPreferencesKey("appNightTheme")
    private val notesSortKey = stringPreferencesKey("notesSort")

    fun getSettings(): Flow<Settings> {
        return dataStore.data.map { preferences ->
            Settings(
                appNightTheme = preferences[appNightThemeKey] ?: false,
                notesSort = enumValueOf(preferences[notesSortKey] ?: NotesSort.CREATED_ASC.name)
            )
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
}