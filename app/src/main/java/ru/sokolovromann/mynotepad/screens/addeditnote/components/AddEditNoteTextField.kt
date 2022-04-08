package ru.sokolovromann.mynotepad.screens.addeditnote.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.addeditnote.state.NoteTextFieldState

@Composable
fun AddEditNoteTextField(
    fieldState: NoteTextFieldState,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = fieldState.text,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
        decorationBox = { innerTextField ->
            if (fieldState.showLabel) {
                Label(showError = fieldState.showError)
            }

            innerTextField()
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        keyboardActions = KeyboardActions.Default,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 0.dp)
            .focusRequester(focusRequester)
    )
}

@Composable
private fun Label(
    showError: Boolean
) {
    if (showError) {
        Text(
            text = stringResource(id = R.string.add_edit_note_empty_text_error_label),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.error
        )
    } else {
        Text(
            text = stringResource(id = R.string.add_edit_note_text_label),
            style = MaterialTheme.typography.body1
        )
    }
}