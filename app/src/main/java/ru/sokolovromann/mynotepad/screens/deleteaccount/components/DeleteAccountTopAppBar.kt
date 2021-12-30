package ru.sokolovromann.mynotepad.screens.deleteaccount.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultIconButton
import ru.sokolovromann.mynotepad.ui.components.DefaultLoadingIndicator
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton

@Composable
fun DeleteAccountTopAppBar(
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    deleting: Boolean
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
            if (deleting) {
                DefaultLoadingIndicator(
                    indicatorColor = MaterialTheme.colors.onPrimary
                )
            } else {
                DefaultTextButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    stringResourceId = R.string.delete_account_delete,
                    allCaps = true
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )
}