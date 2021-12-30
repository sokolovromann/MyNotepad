package ru.sokolovromann.mynotepad.screens.changepassword

data class ChangePasswordState(
    val newPassword: String = "",
    val currentPassword: String = "",
    val incorrectNewPassword: Boolean = false,
    val incorrectCurrentPassword: Boolean = false,
    val changing: Boolean = false
)
