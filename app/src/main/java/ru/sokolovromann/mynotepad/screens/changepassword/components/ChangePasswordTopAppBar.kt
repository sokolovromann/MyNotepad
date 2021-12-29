package ru.sokolovromann.mynotepad.screens.changepassword.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultIconButton
import ru.sokolovromann.mynotepad.ui.components.DefaultLoadingIndicator
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun ChangePasswordTopAppBar(
    onCloseClick: () -> Unit,
    onChangeClick: () -> Unit,
    changing: Boolean
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            DefaultIconButton(
                onClick = onCloseClick,
                imageVector = Icons.Filled.Close
            )
        },
        actions = {
            if (changing) {
                DefaultLoadingIndicator(
                    indicatorColor = MaterialTheme.colors.onPrimary
                )
            } else {
                DefaultTextButton(
                    onClick = onChangeClick,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    stringResourceId = R.string.change_password_change,
                    allCaps = true
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
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