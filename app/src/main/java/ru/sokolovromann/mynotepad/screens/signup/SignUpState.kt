package ru.sokolovromann.mynotepad.screens.signup

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val incorrectEmail: Boolean = false,
    val incorrectMinLengthPassword: Boolean = false,
    val creatingAccount: Boolean = false
)
