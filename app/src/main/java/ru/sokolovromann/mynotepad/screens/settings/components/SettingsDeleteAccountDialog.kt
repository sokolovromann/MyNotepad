package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun SettingsDeleteAccountDialog(
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    AlertDialog(
        text = {
            Text(text = stringResource(id = R.string.settings_delete_account_warning))
        },
        onDismissRequest = onCancelClick,
        dismissButton = {
            DefaultTextButton(
                onClick = onCancelClick,
                text = stringResource(id = R.string.settings_delete_account_cancel)
            )
        },
        confirmButton = {
            OutlinedButton(onClick = onDeleteClick) {
                Text(text = stringResource(id = R.string.settings_delete_account_delete))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsDeleteAccountDialogPreview() {
    MyNotepadTheme {
        SettingsDeleteAccountDialog(
            onCancelClick = {},
            onDeleteClick = {}
        )
    }
}