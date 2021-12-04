package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton

@Composable
fun NotesDeletedMessage(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onUndoClick: () -> Unit
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar(
                action = {
                    DefaultTextButton(
                        onClick = onUndoClick,
                        text = snackbarData.actionLabel ?: ""
                    )
                }
            ) {
                Text(text = snackbarData.message)
            }
        }
    )
}