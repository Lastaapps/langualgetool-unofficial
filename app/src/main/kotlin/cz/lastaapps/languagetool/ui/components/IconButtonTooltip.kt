package cz.lastaapps.languagetool.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonTooltip(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    PlainTooltipBox(
        tooltip = { Text(contentDescription) },
        modifier = modifier,
    ) {
        IconButton(onClick = onClick,
            modifier = Modifier.tooltipAnchor()) {
            Icon(icon, contentDescription = contentDescription)
        }
    }
}
