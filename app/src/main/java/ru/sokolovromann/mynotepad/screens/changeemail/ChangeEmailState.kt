package ru.sokolovromann.mynotepad.screens.changeemail

data class ChangeEmailState(
    val email: String = "",
    val password: String = "",
    val incorrectEmail: Boolean = false,
    val incorrectPassword: Boolean = false,
    val changing: Boolean = false
)
