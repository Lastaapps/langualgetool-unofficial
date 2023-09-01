package cz.lastaapps.languagetool.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import cz.lastaapps.languagetool.ui.theme.PaddingTokens
import cz.lastaapps.languagetool.ui.util.WrapClick

@Composable
internal fun IconWithTitle(
    onClick: () -> Unit,
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
) {
    WrapClick(
        onClick = onClick,
        modifier = modifier,
        padding = PaddingTokens.Smaller,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PaddingTokens.Smaller),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, null)
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
