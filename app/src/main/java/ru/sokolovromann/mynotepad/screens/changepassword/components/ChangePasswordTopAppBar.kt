package ru.sokolovromann.mynotepad.screens.changepassword.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.*
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun ChangePasswordTopAppBar(
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
                    text = stringResource(id = R.string.change_password_change)
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ChangePasswordTopAppBarPreview() {
    MyNotepadTheme {
        ChangePasswordTopAppBar(
            onCloseClick = {},
            onChangeClick = {},
            changing = false
        )
    }
}