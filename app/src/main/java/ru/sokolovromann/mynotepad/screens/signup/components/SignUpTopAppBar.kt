package ru.sokolovromann.mynotepad.screens.signup.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultIconButton

@Composable
fun SignUpTopAppBar(
    onCloseClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.sign_up_name))
        },
        navigationIcon = {
            DefaultIconButton(
                onClick = onCloseClick,
                imageVector = Icons.Filled.Close
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )
}