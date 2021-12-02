package ru.sokolovromann.mynotepad.screens.addeditnote.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddEditNoteSavedMessage(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar {
                Text(text = snackbarData.message)
            }
        }
    )
}