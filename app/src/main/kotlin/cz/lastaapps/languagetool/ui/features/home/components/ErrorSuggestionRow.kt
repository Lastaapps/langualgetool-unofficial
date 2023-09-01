package cz.lastaapps.languagetool.ui.features.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.domain.logic.getErrorIndexForCursor
import cz.lastaapps.languagetool.domain.model.CheckProgress
import cz.lastaapps.languagetool.domain.model.ErrorType
import cz.lastaapps.languagetool.domain.model.MatchedError
import cz.lastaapps.languagetool.domain.model.MatchedText
import cz.lastaapps.languagetool.ui.theme.PaddingTokens
import cz.lastaapps.languagetool.ui.util.PreviewWrapper

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ErrorSuggestionRow(
    progress: CheckProgress,
    cursorPosition: Int,
    matched: MatchedText,
    onApplySuggestion: (MatchedError, String) -> Unit,
    onSkip: (MatchedError) -> Unit,
    onDetail: (MatchedError) -> Unit,
    errorSuggestionsRowState: LazyListState = rememberLazyListState(),
) {
    val errorIndex = remember(matched, cursorPosition) {
        matched.getErrorIndexForCursor(cursorPosition)
            .coerceAtLeast(0)
    }
    LaunchedEffect(errorIndex) {
        errorSuggestionsRowState.animateScrollToItem(errorIndex, 0)
    }

    LazyRow(
        state = errorSuggestionsRowState,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(PaddingTokens.MidSmall),
    ) {
        if (progress == CheckProgress.Processing) {
            return@LazyRow
        }

        items(matched.visibleErrors, key = { it.index }) { error ->

            ErrorSuggestionRowItem(
                onDetail = { onDetail(error) },
                onSuggestion = { onApplySuggestion(error, it) },
                onSkip = { onSkip(error) },
                error = error,
                modifier = Modifier.animateItemPlacement(),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ErrorSuggestionRowItem(
    onDetail: () -> Unit,
    onSuggestion: (String) -> Unit,
    onSkip: () -> Unit,
    error: MatchedError,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onDetail,
        modifier = modifier
            .sizeIn(
                minWidth = ErrorSuggestionTokens.minWidth,
                maxWidth = ErrorSuggestionTokens.maxWidth - 40.dp,
            )
            .height(ErrorSuggestionTokens.height)
            .padding(bottom = PaddingTokens.Small),
    ) {
        Column(
            modifier = modifier
                .padding(PaddingTokens.Small)
                .clip(MaterialTheme.shapes.medium)
                .width(IntrinsicSize.Max)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(PaddingTokens.Small),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(PaddingTokens.MidSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = error.shortMessage ?: error.message,
                    softWrap = false,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )

                if (error.isPremium) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = stringResource(id = R.string.label_premium_error),
                        tint = Color(0xFFFBC02D),
                    )
                }
            }

            Text(
                text = error.original,
                softWrap = false,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textDecoration = TextDecoration.LineThrough,
                style = MaterialTheme.typography.labelSmall,
            )

            FlowRow {
                val itemModifier = Modifier.padding(PaddingTokens.Small / 2)

                error.replacements.forEach { suggestion ->
                    SuggestionItem(
                        onClick = { onSuggestion(suggestion) },
                        suggestion = suggestion,
                        type = error.errorType,
                        modifier = itemModifier,
                    )
                }

                SkipItem(
                    onClick = onSkip,
                    modifier = itemModifier,
                )
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    onClick: () -> Unit,
    suggestion: String,
    type: ErrorType,
    modifier: Modifier = Modifier,
) {
    // val color = type.toColor()
    val color = MaterialTheme.colorScheme.error
    Surface(
        color = color,
        modifier = modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.small,
        shadowElevation = 4.dp,
    ) {
        Text(
            text = suggestion,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .sizeIn(minHeight = ErrorSuggestionTokens.minSuggestionHeight)
                .padding(
                    horizontal = PaddingTokens.MidSmall,
                    vertical = PaddingTokens.Smaller,
                ),
        )
    }
}

@Composable
private fun SkipItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.small,
        shadowElevation = 4.dp,
    ) {
        Text(
            text = stringResource(id = R.string.label_skip),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .sizeIn(minHeight = ErrorSuggestionTokens.minSuggestionHeight)
                .padding(
                    horizontal = PaddingTokens.MidSmall,
                    vertical = PaddingTokens.Smaller,
                ),
        )
    }
}

private object ErrorSuggestionTokens {
    val minWidth = 128.dp
    val maxWidth = 192.dp
    val height = 128.dp
    val minSuggestionHeight = 24.dp
}

@Preview
@Composable
private fun ErrorSuggestionRowItemPreview() = PreviewWrapper {
    ErrorSuggestionRowItem(
        onDetail = {},
        onSkip = {},
        onSuggestion = {},
        error = MatchedError.example(IntRange(0, 0)),
        modifier = Modifier,
    )
}
