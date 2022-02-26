package ru.sokolovromann.mynotepad.screens.addeditnote.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.AppBarIconButton
import ru.sokolovromann.mynotepad.ui.components.DefaultTopAppBar
import ru.sokolovromann.mynotepad.ui.components.AppBarTextButton
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun AddEditNoteTopAppBar(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    showMenu: Boolean,
    onShowMenuChange: (isShow: Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    DefaultTopAppBar(
        navigationIcon = {
            AppBarIconButton(
                onClick = onBackClick,
                icon = Icons.Filled.ArrowBack
            )
        },
        actions = {
            AppBarTextButton(
                onClick = onSaveClick,
                text = stringResource(id = R.string.add_edit_note_save)
            )
            AppBarIconButton(
                onClick = { onShowMenuChange(true) },
                icon = Icons.Filled.MoreVert
            ) {
                AddEditNoteDropdownMenu(
                    expanded = showMenu,
                    onDismiss = { onShowMenuChange(false) },
                    onDeleteClick = onDeleteClick
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AddEditNoteTopAppBarPreview() {
    MyNotepadTheme {
        AddEditNoteTopAppBar(
            onBackClick = {},
            onSaveClick = {},
            showMenu = false,
            onShowMenuChange = {},
            onDeleteClick = {}
        )
    }
}