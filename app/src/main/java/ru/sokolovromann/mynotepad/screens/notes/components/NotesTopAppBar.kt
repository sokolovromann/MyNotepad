package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultIconButton
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun NotesTopAppBar(
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.notes_name))
        },
        navigationIcon = {
            DefaultIconButton(
                onClick = onNavigationIconClick,
                imageVector = Icons.Filled.Menu)
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )
}

@Preview(showBackground = true)
@Composable
private fun NotesTopAppBarPreview() {
    MyNotepadTheme {
        NotesTopAppBar(
            onNavigationIconClick = {}
        )
    }
}