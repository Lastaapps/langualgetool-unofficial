package cz.lastaapps.languagetool.ui.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.lastaapps.languagetool.ui.components.SaneDialogDecoration
import cz.lastaapps.languagetool.ui.theme.AppTheme
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
fun PreviewWrapper(
    screenPadding: Dp = PaddingTokens.More.Screen,
    content: @Composable () -> Unit,
) = AppTheme {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .padding(screenPadding)
                .sizeIn(
                    maxWidth = 400.dp,
                    maxHeight = 800.dp,
                )
        ) {
            content()
        }
    }
}

@Composable
fun PreviewDialogWrapper(
    content: @Composable () -> Unit,
) = AppTheme {
    SaneDialogDecoration(content = content)
}

@Composable
fun PreviewColumnWrapper(
    spacing: Dp = PaddingTokens.Small,
    screenPadding: Dp = PaddingTokens.More.Screen,
    content: @Composable ColumnScope.() -> Unit,
) = PreviewWrapper(
    screenPadding = screenPadding,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        content()
    }
}
