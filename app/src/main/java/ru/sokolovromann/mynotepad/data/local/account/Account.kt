package ru.sokolovromann.mynotepad.data.local.account

data class Account(
    val uid: String,
    val email: String,
    val tokenId: String
) {
    fun isEmpty(): Boolean {
        return uid.isEmpty()
    }

    fun isNotEmpty() = !isEmpty()
}
