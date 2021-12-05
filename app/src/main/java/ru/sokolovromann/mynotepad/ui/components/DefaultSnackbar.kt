package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultSnackbar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onActionClick: () -> Unit
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar(
                action = {
                    DefaultTextButton(
                        onClick = onActionClick,
                        text = snackbarData.actionLabel ?: ""
                    )
                }
            ) {
                Text(text = snackbarData.message)
            }
        }
    )
}

@Composable
fun DefaultSnackbar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
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