package ru.sokolovromann.mynotepad.screens.signin.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.AppBarIconButton
import ru.sokolovromann.mynotepad.ui.components.AppBarTitleText
import ru.sokolovromann.mynotepad.ui.components.DefaultTopAppBar

@Composable
fun SignInTopAppBar(
    onCloseClick: () -> Unit
) {
    DefaultTopAppBar(
        title = {
            AppBarTitleText(text = stringResource(id = R.string.sign_in_name))
        },
        navigationIcon = {
            AppBarIconButton(
                onClick = onCloseClick,
                icon = Icons.Filled.Close
            )
        },
    )
}