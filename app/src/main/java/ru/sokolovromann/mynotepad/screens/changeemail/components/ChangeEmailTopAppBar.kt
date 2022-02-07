package ru.sokolovromann.mynotepad.screens.changeemail.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.*
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun ChangeEmailTopAppBar(
    onCloseClick: () -> Unit,
    onChangeClick: () -> Unit,
    changing: Boolean
) {
    DefaultTopAppBar(
        navigationIcon = {
            AppBarIconButton(
                onClick = onCloseClick,
                icon = Icons.Filled.Close
            )
        },
        actions = {
            if (changing) {
                AppBarLoadingIndicator()
            } else {
                AppBarTextButton(
                    onClick = onChangeClick,
                    text = stringResource(id = R.string.change_email_change)
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ChangeEmailTopAppBarPreview() {
    MyNotepadTheme {
        ChangeEmailTopAppBar(
            onCloseClick = {},
            onChangeClick = {},
            changing = true
        )
    }
}