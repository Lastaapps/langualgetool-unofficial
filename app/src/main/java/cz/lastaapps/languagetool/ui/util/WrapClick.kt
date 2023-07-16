package cz.lastaapps.languagetool.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
fun WrapClick(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    padding: Dp = PaddingTokens.MidSmall,
    content: @Composable () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = shape,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}
