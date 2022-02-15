package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R

@Composable
fun SettingsAboutContent(
    appVersion: String,
    onGitHubClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit
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
    SettingsItem(
        title = stringResource(id = R.string.settings_feedback_title),
        icon = painterResource(id = R.drawable.ic_settings_feedback),
        description = stringResource(id = R.string.settings_feedback_description),
        onItemClick = onFeedbackClick
    )
    SettingsItem(
        title = stringResource(id = R.string.settings_terms_title),
        icon = painterResource(id = R.drawable.ic_settings_terms),
        description = stringResource(id = R.string.settings_terms_description),
        onItemClick = onTermsClick
    )
    SettingsItem(
        title = stringResource(id = R.string.settings_privacy_policy_title),
        icon = painterResource(id = R.drawable.ic_settings_privacy_policy),
        description = stringResource(id = R.string.settings_privacy_policy_description),
        onItemClick = onPrivacyPolicyClick
    )
}