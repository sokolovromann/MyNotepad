package ru.sokolovromann.mynotepad.data.mapping

import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.local.note.NoteSyncState
import ru.sokolovromann.mynotepad.data.remote.note.NoteRequest
import ru.sokolovromann.mynotepad.data.remote.note.NoteResponse
import ru.sokolovromann.mynotepad.data.remote.note.NotesResponse

class NoteMappingImpl : NoteMapping {

    override fun toNote(noteResponse: NoteResponse) = Note(
        uid = noteResponse.uid,
        owner = noteResponse.owner,
        title = noteResponse.title,
        text = noteResponse.text,
        created = noteResponse.created,
        lastModified = noteResponse.lastModified,
        syncState = NoteSyncState.NOTHING.name
    )

    override fun toNoteRequest(note: Note) = NoteRequest(
        uid = note.uid,
        owner = note.owner,
        title = note.title,
        text = note.text,
        created = note.created,
        lastModified = note.lastModified
    )

    override fun toNotesList(notesResponse: NotesResponse) =
        notesResponse.notes.map { noteResponse -> toNote(noteResponse) }
}