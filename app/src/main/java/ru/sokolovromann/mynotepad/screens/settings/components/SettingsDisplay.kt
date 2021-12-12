package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.BuildConfig
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultCard
import ru.sokolovromann.mynotepad.ui.components.DefaultSwitch
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@ExperimentalFoundationApi
@Composable
fun SettingsDisplay(
    appNightTheme: Boolean,
    appVersion: String,
    onAppNightThemeChange: (appNightTheme: Boolean) -> Unit,
    onGitHubClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
    ) {
        DefaultCard(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 8.dp)
        ) {
            Column {
                SettingsHeader(
                    text = stringResource(id = R.string.settings_general_header)
                )
                DefaultSwitch(
                    title = stringResource(id = R.string.settings_app_night_theme_title),
                    checked = appNightTheme,
                    onCheckedChange = onAppNightThemeChange
                )
            }
        }

        DefaultCard(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
        ) {
            Column {
                SettingsHeader(
                    text = stringResource(id = R.string.settings_about_header)
                )
                SettingsItem(
                    title = stringResource(id = R.string.settings_app_version_title),
                    description = appVersion,
                    onItemClick = {}
                )
                SettingsItem(
                    title = stringResource(id = R.string.settings_github_title),
                    description = stringResource(id = R.string.settings_github_description),
                    onItemClick = onGitHubClick
                )
            }
        }
    }
}

@Composable
private fun SettingsHeader(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun SettingsItem(
    title: String,
    description: String = "",
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (description.isEmpty()) 48.dp else 64.dp)
            .clickable { onItemClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 8.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1
        )
        if (description.isNotEmpty()) {
            Text(
                text = description,
                modifier = Modifier.padding(horizontal = 8.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.body2
            )
        }
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
            onGitHubClick = {}
        )
    }
}