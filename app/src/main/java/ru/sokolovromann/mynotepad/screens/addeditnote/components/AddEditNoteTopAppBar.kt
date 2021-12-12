package ru.sokolovromann.mynotepad.screens.addeditnote.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultIconButton
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun AddEditNoteTopAppBar(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = { 
            DefaultIconButton(
                onClick = onBackClick,
                imageVector = Icons.Filled.ArrowBack
            )
        },
        actions = {
            DefaultTextButton(
                onClick = onSaveClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colors.onPrimary
                ),
                stringResourceId = R.string.add_edit_note_save,
                allCaps = true
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )
}

@Preview(showBackground = true)
@Composable
private fun AddEditNoteTopAppBarPreview() {
    MyNotepadTheme {
        AddEditNoteTopAppBar(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}