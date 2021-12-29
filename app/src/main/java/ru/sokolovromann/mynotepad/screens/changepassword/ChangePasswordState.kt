package ru.sokolovromann.mynotepad.screens.changepassword

data class ChangePasswordState(
    val password: String = "",
    val incorrectPassword: Boolean = false,
    val changing: Boolean = false
)
