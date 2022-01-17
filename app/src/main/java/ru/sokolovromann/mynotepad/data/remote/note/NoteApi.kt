package ru.sokolovromann.mynotepad.data.remote.note

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.sokolovromann.mynotepad.data.remote.HttpRoute
import javax.inject.Inject

class NoteApi @Inject constructor(
    private val client: HttpClient
) {

    suspend fun getNotes(owner: String, tokenId: String): Result<NotesResponse> {
        return try {
            val response: Map<String, NoteResponse> = client.get{
                url(HttpRoute.notes(owner, tokenId))
            }
            val notesResponse = NotesResponse(response.values.toList())
            Result.success(notesResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun putNote(noteRequest: NoteRequest, tokenId: String): Result<NoteResponse> {
        return try {
            Result.success(
                client.put {
                    url(HttpRoute.note(noteRequest.uid, noteRequest.owner, tokenId))
                    contentType(ContentType.Application.Json)
                    body = noteRequest
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNote(noteRequest: NoteRequest, tokenId: String): Result<Unit> {
        return try {
            Result.success(
                client.delete(
                    urlString = HttpRoute.note(noteRequest.uid, noteRequest.owner, tokenId)
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}