package ru.sokolovromann.mynotepad.data.remote

object HttpRoute {

    private const val BASE_URL = "https://ru-sokolovromann-mynotepad-default-rtdb.europe-west1.firebasedatabase.app"

    fun notes(userUid: String, tokenId: String) =
        "$BASE_URL/users/$userUid/notes.json?auth=$tokenId"

    fun note(noteUid: String, userUid: String, tokenId: String) =
        "$BASE_URL/users/$userUid/notes/$noteUid.json?auth=$tokenId"
}