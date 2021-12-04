package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R

@Composable
fun NotesDropdownMenu(
    expanded: Boolean = false,
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(onClick = onDeleteClick) {
            Text(text = stringResource(id = R.string.notes_delete_note))
        }
    }
}