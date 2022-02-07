package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.BuildConfig
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@ExperimentalFoundationApi
@Composable
fun SettingsDisplay(
    appNightTheme: Boolean,
    appVersion: String,
    onAppNightThemeChange: (appNightTheme: Boolean) -> Unit,
    onGitHubClick: () -> Unit,
    localAccount: Boolean,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    accountName: String,
    onChangeEmailClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.surface)
        .verticalScroll(scrollState)
    ) {
        SettingsGeneralContent(
            appNightTheme = appNightTheme,
            onAppNightThemeChange = onAppNightThemeChange
        )
        Divider()
        SettingsAccountContent(
            localAccount = localAccount,
            onSignUpClick = onSignUpClick,
            onSignInClick = onSignInClick,
            accountName = accountName,
            onChangeEmailClick = onChangeEmailClick,
            onChangePasswordClick = onChangePasswordClick,
            onSignOutClick = onSignOutClick,
            onDeleteAccountClick = onDeleteAccountClick
        )
        Divider()
        SettingsAboutContent(
            appVersion = appVersion,
            onGitHubClick = onGitHubClick
        )
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun SettingsDisplayPreview() {
    MyNotepadTheme {
        SettingsDisplay(
            appNightTheme = true,
            appVersion = BuildConfig.VERSION_NAME,
            onAppNightThemeChange = {},
            onGitHubClick = {},
            localAccount = false,
            onSignUpClick = {},
            onSignInClick = {},
            accountName = "email@domain.com",
            onChangeEmailClick = {},
            onChangePasswordClick = {},
            onSignOutClick = {},
            onDeleteAccountClick = {}
        )
    }
}