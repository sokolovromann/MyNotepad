package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.ItemIcon
import ru.sokolovromann.mynotepad.ui.components.SwitchItem

@Composable
fun SettingsGeneralContent(
    appNightTheme: Boolean,
    notesSaveAndClose: Boolean,
    onAppNightThemeChange: (appNightTheme: Boolean) -> Unit,
    onNotesSaveAndClose: (notesSaveAndClose: Boolean) -> Unit
) {
    SettingsHeader(
        text = stringResource(id = R.string.settings_general_header)
    )
    SwitchItem(
        title = stringResource(id = R.string.settings_app_night_theme_title),
        icon = {
            ItemIcon(painter = painterResource(id = R.drawable.ic_settings_night_theme))
        },
        checked = appNightTheme,
        onCheckedChange = onAppNightThemeChange
    )
    SwitchItem(
        title = stringResource(id = R.string.settings_save_note_and_close_title),
        icon = {
               ItemIcon(painter = painterResource(id = R.drawable.ic_settings_notes_save_and_close))
        },
        checked = notesSaveAndClose,
        onCheckedChange = onNotesSaveAndClose
    )
}