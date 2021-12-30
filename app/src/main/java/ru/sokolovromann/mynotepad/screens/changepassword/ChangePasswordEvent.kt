package ru.sokolovromann.mynotepad.screens.changepassword

sealed class ChangePasswordEvent {
    data class OnNewPasswordChange(val newPassword: String) : ChangePasswordEvent()
    data class OnCurrentPasswordChange(val newPassword: String) : ChangePasswordEvent()
    object ChangeClick : ChangePasswordEvent()
    object CloseClick : ChangePasswordEvent()
}
