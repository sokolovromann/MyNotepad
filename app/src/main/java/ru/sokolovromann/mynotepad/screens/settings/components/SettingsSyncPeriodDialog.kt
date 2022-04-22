package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.data.local.settings.NotesSyncPeriod
import ru.sokolovromann.mynotepad.screens.settings.state.SettingsSyncPeriodDialogState
import ru.sokolovromann.mynotepad.ui.components.RadioButtonItem

@Composable
fun SettingsSyncPeriodDialog(
    dialogState: SettingsSyncPeriodDialogState,
    onDismiss: () -> Unit,
    onSelectedChange: (notesSyncPeriod: NotesSyncPeriod) -> Unit
) {
    if (!dialogState.showDialog) {
        return
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.background(color = MaterialTheme.colors.surface)) {
            Text(
                text = stringResource(id = R.string.settings_sync_period_title),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(all = 24.dp)
            )
            RadioButtonItem(
                title = stringResource(id = R.string.settings_sync_period_description_one_hour),
                selected = dialogState.selected == NotesSyncPeriod.ONE_HOUR,
                onSelectedChange = { onSelectedChange(NotesSyncPeriod.ONE_HOUR) }
            )
            RadioButtonItem(
                title = stringResource(id = R.string.settings_sync_period_description_three_hour),
                selected = dialogState.selected == NotesSyncPeriod.THREE_HOURS,
                onSelectedChange = { onSelectedChange(NotesSyncPeriod.THREE_HOURS) }
            )
            RadioButtonItem(
                title = stringResource(id = R.string.settings_sync_period_description_five_hour),
                selected = dialogState.selected == NotesSyncPeriod.FIVE_HOURS,
                onSelectedChange = { onSelectedChange(NotesSyncPeriod.FIVE_HOURS) }
            )
        }
    }
}