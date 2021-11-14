package ru.sokolovromann.mynotepad.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.sokolovromann.mynotepad.data.local.LocalDatabase
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
}