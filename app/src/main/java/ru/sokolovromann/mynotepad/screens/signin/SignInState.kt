package ru.sokolovromann.mynotepad.screens.signin

data class SignInState(
    val email: String = "",
    val password: String = "",
    val incorrectEmail: Boolean = false,
    val incorrectPassword: Boolean = false,
    val signingIn: Boolean = false
)
