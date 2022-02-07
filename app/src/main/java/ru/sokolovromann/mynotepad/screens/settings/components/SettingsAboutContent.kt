package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R

@Composable
fun SettingsAboutContent(
    appVersion: String,
    onGitHubClick: () -> Unit,
) {
    SettingsHeader(
        text = stringResource(id = R.string.settings_about_header)
    )
    SettingsItem(
        title = stringResource(id = R.string.settings_app_version_title),
        icon = painterResource(id = R.drawable.ic_settings_app_version),
        description = appVersion,
        onItemClick = {}
    )
    SettingsItem(
        title = stringResource(id = R.string.settings_github_title),
        icon = painterResource(id = R.drawable.ic_settings_github),
        description = stringResource(id = R.string.settings_github_description),
        onItemClick = onGitHubClick
    )
}