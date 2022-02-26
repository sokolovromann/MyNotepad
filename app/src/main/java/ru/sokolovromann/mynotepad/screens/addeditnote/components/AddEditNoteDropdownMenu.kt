package ru.sokolovromann.mynotepad.screens.addeditnote.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R

@Composable
fun AddEditNoteDropdownMenu(
    expanded: Boolean = false,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(onClick = onDeleteClick) {
            Text(text = stringResource(id = R.string.add_edit_note_delete_note))
        }
    }
}