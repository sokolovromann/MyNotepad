package ru.sokolovromann.mynotepad.screens.addeditnote.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.addeditnote.state.NoteTitleFieldState

@Composable
fun AddEditNoteTitleField(
    fieldState: NoteTitleFieldState,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = fieldState.title,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
        decorationBox = { innerTextField ->
            if (fieldState.showLabel) {
                Label()
            }
            innerTextField()
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        keyboardActions = KeyboardActions.Default,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 8.dp)
    )
}

@Composable
private fun Label() {
    Text(
        text = stringResource(id = R.string.add_edit_note_title_label),
        style = MaterialTheme.typography.h6
    )
}