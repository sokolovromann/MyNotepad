package ru.sokolovromann.mynotepad.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    body: @Composable (() -> Unit)? = null,
    dropdownMenu: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        title?.let { it() }
        body?.let { it() }
        dropdownMenu?.let { it() }
    }
}

@Composable
fun ItemTitleText(
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Bold,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 2,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.subtitle1,
    text: String,
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = fontWeight,
        overflow = overflow,
        maxLines = maxLines,
        color = color,
        style = style
    )
}

@Composable
fun ItemBodyText(
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 5,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.body1,
    text: String
) {
    Text(
        text = text,
        modifier = modifier,
        overflow = overflow,
        maxLines = maxLines,
        color = color,
        style = style
    )
}

@Composable
fun RadioButtonItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    selected: Boolean,
    onSelectedChange: (isSelected: Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .height(if (description.isEmpty()) 56.dp else 64.dp)
            .clickable { onSelectedChange(!selected) },
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

@Composable
fun SwitchItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    icon: @Composable (() -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .height(if (description.isEmpty()) 56.dp else 64.dp)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        icon?.let { it() }
        Column(
            modifier = Modifier
                .padding(start = if (icon == null) 0.dp else 32.dp, end = 16.dp)
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
        Switch(
            checked = checked,
            onCheckedChange = null
        )
    }
}

@Composable
fun ItemIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String = "",
    tint: Color = tint()
) {
    Icon(
        modifier = modifier,
        painter = painter,
        contentDescription = contentDescription,
        tint = tint
    )
}

@Composable
fun ItemIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String = "",
    tint: Color = tint()
) {
    Icon(
        modifier = modifier,
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint
    )
}

@Composable
private fun tint(): Color = MaterialTheme.colors.onSurface.copy(0.8f)

@Preview(
    name = "Light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Night",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ItemPreview() {
    MyNotepadTheme {
        Column {
            TextItem(
                title = { ItemTitleText(text = "Item Title Text") },
                body = { ItemBodyText(text = "Item Body Text".repeat(10)) }
            )
            Divider()
            SwitchItem(
                title = "Item Switch",
                checked = true,
                onCheckedChange = {}
            )
            Divider()
            SwitchItem(
                title = "Item Switch With Icon",
                icon = { ItemIcon(imageVector = Icons.Default.Edit) },
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}