package ru.sokolovromann.mynotepad.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.sokolovromann.mynotepad.data.local.LocalDatabase
import ru.sokolovromann.mynotepad.data.local.settings.SettingsDataStore
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepositoryImpl
import ru.sokolovromann.mynotepad.data.repository.SettingsRepository
import ru.sokolovromann.mynotepad.data.repository.SettingsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "my_notepad_local_settings"
    )

    @Provides
    @Singleton
    fun providesNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository {
        return noteRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesNoteRepositoryImpl(localDatabase: LocalDatabase): NoteRepositoryImpl {
        return NoteRepositoryImpl(localDatabase)
    }

    @Provides
    @Singleton
    fun providesLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(context, LocalDatabase::class.java, "my_notepad_local_database")
            .build()
    }

    @Provides
    @Singleton
    fun providesSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository {
        return settingsRepositoryImpl
    }

    @Provides
    @Singleton
    fun providesSettingsRepositoryImpl(settingsDataStore: SettingsDataStore): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(settingsDataStore)
    }

    @Provides
    @Singleton
    fun providesSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context.dataStore)
    }
}