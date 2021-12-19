package ru.sokolovromann.mynotepad.data.remote.auth

data class UserResponse(
    val uid: String,
    val email: String,
    val tokenId: String = ""
)
