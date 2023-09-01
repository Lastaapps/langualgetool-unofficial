package cz.lastaapps.languagetool.ui.features.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.domain.model.MatchedError
import cz.lastaapps.languagetool.ui.components.SaneDialog
import cz.lastaapps.languagetool.ui.theme.PaddingTokens
import cz.lastaapps.languagetool.ui.util.PreviewDialogWrapper

@Composable
internal fun MatchDetailDialog(
    error: MatchedError,
    onDismiss: () -> Unit,
    onApplySuggestion: (String) -> Unit,
    onSkip: () -> Unit,
) {
    SaneDialog(onDismiss = onDismiss) {
        MatchDetailDialogContent(
            error = error,
            onApplySuggestion = onApplySuggestion,
            onSkip = onSkip,
        )
    }
}

@Composable
private fun MatchDetailDialogContent(
    error: MatchedError,
    onApplySuggestion: (String) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
    urlHandler: UriHandler = LocalUriHandler.current,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        error.shortMessage?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        if (error.isPremium) {
            PremiumBadge()
        }
        Spacer(modifier = Modifier.height(PaddingTokens.Small))

        Text(
            error.message,
            fontStyle = FontStyle.Italic,
        )

        Spacer(modifier = Modifier.height(PaddingTokens.MidSmall))
        Sentence(
            text = error.contextSentence,
            range = error.contextRange,
        )
        Spacer(modifier = Modifier.height(PaddingTokens.MidSmall))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(PaddingTokens.Small),
        ) {
            error.replacements.forEach {
                ErrorBadge(it, { onApplySuggestion(it) })
            }
        }
        Spacer(modifier = Modifier.height(PaddingTokens.Small))

        SkipBadge(onSkip)
        Spacer(modifier = Modifier.height(PaddingTokens.MidLarge))

        Text(
            text = error.ruleCategoryName
                ?: stringResource(id = R.string.title_unknown_rule),
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = error.ruleDescription,
        )

        error.explanationUrl?.let { url ->
            TextButton(
                onClick = { urlHandler.openUri(url) },
                modifier = Modifier.align(Alignment.End),
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew, contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(PaddingTokens.Small))
                Text(text = stringResource(id = R.string.button_explain))
            }
        }
    }
}

@Composable
private fun PremiumBadge(modifier: Modifier = Modifier) {
    Surface(
        contentColor = Color.White,
        color = Color(0xFFFBC02D),
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Text(
            stringResource(id = R.string.text_premium),
            style = MaterialTheme.typography.labelSmall,
            modifier = modifier.padding(
                horizontal = PaddingTokens.Small,
                vertical = PaddingTokens.Tiny,
            ),
        )
    }
}

@Composable
fun Sentence(
    text: String,
    range: IntRange,
    modifier: Modifier = Modifier,
) {
    val sentence = buildAnnotatedString {
        val s1 = text.substring(0, range.first)
        val s2 = text.substring(range)
        val s3 = text.substring(range.last + 1)

        append(s1)
        pushStyle(
            SpanStyle(
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.error,
                textDecoration = TextDecoration.Underline,
            ),
        )
        append(s2)
        pop()
        append(s3)
    }
    Text(
        text = sentence,
        modifier = modifier,
    )
}

@Composable
fun ErrorBadge(
    suggestion: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.error,
        shape = MaterialTheme.shapes.extraSmall,
        shadowElevation = 6.dp,
        modifier = modifier,
    ) {
        Text(
            suggestion,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(
                horizontal = PaddingTokens.MidSmall,
                vertical = PaddingTokens.Smaller,
            ),
        )
    }
}

@Composable
fun SkipBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.extraSmall,
        shadowElevation = 6.dp,
        modifier = modifier,
    ) {
        Text(
            stringResource(id = R.string.label_skip),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(
                horizontal = PaddingTokens.MidSmall,
                vertical = PaddingTokens.Smaller,
            ),
        )
    }
}

@Preview
@Composable
private fun MatchDetailDialogContentPreview() = PreviewDialogWrapper {
    MatchDetailDialogContent(
        error = MatchedError.example(),
        onApplySuggestion = {},
        onSkip = {},
    )
}
