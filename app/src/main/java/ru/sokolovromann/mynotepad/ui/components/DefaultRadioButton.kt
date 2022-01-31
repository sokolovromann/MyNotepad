package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun DefaultRadioButton(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    selected: Boolean,
    onSelectedChange: (isSelected: Boolean) -> Unit
) {
    Row(
        modifier = modifier.then(
            Modifier
                .height(if (description.isEmpty()) 56.dp else 64.dp)
                .clickable { onSelectedChange(!selected) }
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = selected,
            onClick = {},
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1
            )
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.body2,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultRadioButtonPreview() {
    MyNotepadTheme {
        DefaultRadioButton(
            title = "Title",
            selected = true,
            onSelectedChange = {}
        )
    }
}