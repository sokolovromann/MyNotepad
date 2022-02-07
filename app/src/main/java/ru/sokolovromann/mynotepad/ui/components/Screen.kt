package ru.sokolovromann.mynotepad.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun CircularLoading(
    modifier: Modifier = Modifier,
    message: String = "",
    messageColor: Color = MaterialTheme.colors.onBackground,
    indicatorColor: Color = MaterialTheme.colors.secondary
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(all = 8.dp),
            color = indicatorColor
        )
        Text(
            style = MaterialTheme.typography.body1,
            color = messageColor,
            text = message
        )
    }
}

@Composable
fun NotFound(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    message: String,
    messageColor: Color = MaterialTheme.colors.onBackground
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon?.let { it() }
        Text(
            style = MaterialTheme.typography.body1,
            color = messageColor,
            text = message,
        )
    }
}

@Composable
fun NotFoundIcon(
    icon: ImageVector,
    iconColor: Color = tint(),
    contentDescription: String = ""
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = iconColor,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun NotFoundIcon(
    icon: Painter,
    iconColor: Color = tint(),
    contentDescription: String = ""
) {
    Icon(
        painter = icon,
        contentDescription = contentDescription,
        tint = iconColor,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
private fun tint(): Color = MaterialTheme.colors.onBackground.copy(0.8f)

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
private fun ScreenPreview() {
    MyNotepadTheme {
        Column {
            CircularLoading(message = "Loading... Please wait...")
            Spacer(modifier = Modifier.height(8.dp))
            NotFound(
                message = "Data is not found",
                icon = { NotFoundIcon(icon = Icons.Default.ShoppingCart) }
            )
        }
    }
}