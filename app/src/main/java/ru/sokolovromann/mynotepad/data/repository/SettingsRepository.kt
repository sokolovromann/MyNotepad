package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort
import ru.sokolovromann.mynotepad.data.local.settings.Settings

interface SettingsRepository {

    suspend fun getSettings(): Flow<Settings>

    suspend fun saveAppNightTheme(nightTheme: Boolean)

    suspend fun saveNotesSort(notesSort: NotesSort)
}