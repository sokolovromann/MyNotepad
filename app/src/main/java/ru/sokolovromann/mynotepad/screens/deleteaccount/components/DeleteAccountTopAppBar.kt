package ru.sokolovromann.mynotepad.screens.deleteaccount.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.*

@Composable
fun DeleteAccountTopAppBar(
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    deleting: Boolean
) {
    DefaultTopAppBar(
        navigationIcon = {
            AppBarIconButton(
                onClick = onCloseClick,
                icon = Icons.Filled.Close
            )
        },
        actions = {
            if (deleting) {
                AppBarLoadingIndicator()
            } else {
                AppBarTextButton(
                    onClick = onDeleteClick,
                    text = stringResource(id = R.string.delete_account_delete)
                )
            }
        },
    )
}