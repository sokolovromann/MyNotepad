package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun NotesTopAppBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun NotesTopAppBarPreview() {
    MyNotepadTheme {
        NotesTopAppBar()
    }
}