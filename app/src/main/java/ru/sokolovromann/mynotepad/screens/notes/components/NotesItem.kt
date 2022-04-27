package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.screens.notes.state.NotesItemState
import ru.sokolovromann.mynotepad.ui.components.ClickableSurface
import ru.sokolovromann.mynotepad.ui.components.ItemBodyText
import ru.sokolovromann.mynotepad.ui.components.ItemTitleText
import ru.sokolovromann.mynotepad.ui.components.TextItem

@ExperimentalFoundationApi
@Composable
fun NotesItem(
    itemState: NotesItemState,
    onClick: () -> Unit,
    onShowMenuChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    ClickableSurface(
        onClick = onClick,
        onLongClick = { onShowMenuChange(true) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 1.dp
    ) {
        TextItem(
            modifier = Modifier.padding(8.dp),
            title = if (itemState.note.title.isEmpty()) {
                null
            } else {
                { Title(text = itemState.note.title) }
            },
            body = {
                Body(
                    text = itemState.note.text,
                    maxLines = itemState.maxLines
                )
            },
            dropdownMenu = {
                NotesDropdownMenu(
                    expanded = itemState.showMenu,
                    onDismiss = { onShowMenuChange(false) },
                    onDeleteClick = onDelete
                )
            }
        )
    }
}

@Composable
private fun Title(text: String) {
    ItemTitleText(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    )
}

@Composable
private fun Body(
    text: String,
    maxLines: Int
) {
    ItemBodyText(
        text = text,
        maxLines = maxLines,
        modifier = Modifier.fillMaxWidth()
    )
}