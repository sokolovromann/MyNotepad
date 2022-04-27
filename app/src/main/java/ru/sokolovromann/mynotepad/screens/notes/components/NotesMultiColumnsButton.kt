package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultIconButton

@Composable
fun NotesMultiColumnsButton(
    multiColumns: Boolean,
    onClick: () -> Unit
) {
    DefaultIconButton(
        onClick = onClick,
        icon = if (multiColumns) {
            painterResource(id = R.drawable.ic_notes_list_multi_columns)
        } else {
            painterResource(id = R.drawable.ic_notes_grid_multi_columns)
        },
        contentDescription = if (multiColumns) {
            stringResource(id = R.string.notes_multi_column_content_description)
        } else {
            stringResource(id = R.string.notes_single_column_content_description)
        }
    )
}