package ru.sokolovromann.mynotepad.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.local.note.NoteDao

@Database(entities = [Note::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}