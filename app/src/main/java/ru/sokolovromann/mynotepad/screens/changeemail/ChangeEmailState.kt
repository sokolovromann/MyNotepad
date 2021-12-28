package ru.sokolovromann.mynotepad.screens.changeemail

data class ChangeEmailState(
    val email: String = "",
    val incorrectEmail: Boolean = false,
    val changing: Boolean = false
)
