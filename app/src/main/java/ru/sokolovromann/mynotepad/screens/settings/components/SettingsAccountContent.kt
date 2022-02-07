package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R

@Composable
fun SettingsAccountContent(
    localAccount: Boolean,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    accountName: String,
    onChangeEmailClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    SettingsHeader(
        text = stringResource(id = R.string.settings_account_header)
    )
    if (localAccount) {
        LocalAccountCardContent(
            onSignUpClick = onSignUpClick,
            onSignInClick = onSignInClick
        )
    } else {
        AccountCardContent(
            onChangeEmailClick = onChangeEmailClick,
            onChangePasswordClick = onChangePasswordClick,
            onDeleteAccount = onDeleteAccountClick,
            onSignOutClick = onSignOutClick
        )
    }
}

@Composable
private fun LocalAccountCardContent(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    Column {
        SettingsItem(
            title = stringResource(id = R.string.settings_sign_up_title),
            icon = painterResource(id = R.drawable.ic_settings_sign_up),
            onItemClick = onSignUpClick
        )
        SettingsItem(
            title = stringResource(id = R.string.settings_sign_in_title),
            icon = painterResource(id = R.drawable.ic_settings_sign_in),
            onItemClick = onSignInClick
        )
    }
}

@Composable
private fun AccountCardContent(
    onChangeEmailClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Column {
        SettingsItem(
            title = stringResource(id = R.string.settings_change_email_title),
            icon = painterResource(id = R.drawable.ic_settings_change_email),
            onItemClick = onChangeEmailClick
        )
        SettingsItem(
            title = stringResource(id = R.string.settings_change_password_title),
            icon = painterResource(id = R.drawable.ic_settings_change_password),
            onItemClick = onChangePasswordClick
        )
        SettingsItem(
            title = stringResource(id = R.string.settings_sign_out_title),
            icon = painterResource(id = R.drawable.ic_settings_sign_out),
            onItemClick = onSignOutClick
        )
        SettingsItem(
            title = stringResource(id = R.string.settings_delete_account_title),
            icon = painterResource(id = R.drawable.ic_settings_delete_account),
            onItemClick = onDeleteAccount
        )
    }
}