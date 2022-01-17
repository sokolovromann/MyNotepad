package ru.sokolovromann.mynotepad.data.mapping

import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.remote.note.NoteRequest
import ru.sokolovromann.mynotepad.data.remote.note.NoteResponse
import ru.sokolovromann.mynotepad.data.remote.note.NotesResponse

interface NoteMapping {

    fun toNote(noteResponse: NoteResponse): Note

    fun toNoteRequest(note: Note): NoteRequest

    fun toNotesList(notesResponse: NotesResponse): List<Note>
}