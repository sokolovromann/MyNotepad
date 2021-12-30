package ru.sokolovromann.mynotepad.screens.deleteaccount

data class DeleteAccountState(
    val password: String = "",
    val incorrectPassword: Boolean = false,
    val deleting: Boolean = false
)
