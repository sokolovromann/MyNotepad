package ru.sokolovromann.mynotepad.screens.changepassword

sealed class ChangePasswordEvent {
    data class OnPasswordChange(val newPassword: String) : ChangePasswordEvent()
    object ChangeClick : ChangePasswordEvent()
    object CloseClick : ChangePasswordEvent()
}
