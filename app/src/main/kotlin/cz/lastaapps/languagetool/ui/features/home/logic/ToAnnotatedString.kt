package cz.lastaapps.languagetool.ui.features.home.logic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import cz.lastaapps.languagetool.domain.model.ErrorType
import cz.lastaapps.languagetool.domain.model.MatchedText

@Composable
internal fun MatchedText.toAnnotatedString() = remember(this) {
    buildAnnotatedString {
        var lastIndex = 0
        visibleErrors.forEach {
            append(text.substring(lastIndex, it.range.first))

            val color = it.errorType.toColor()
            // val color = MaterialTheme.colorScheme.error
            pushStyle(
                SpanStyle(
                    // color = Color.White,
                    // background = color,
                    color = color,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            append(text.substring(it.range))
            pop()

            lastIndex = it.range.last + 1
        }

        append(text.substring(lastIndex, text.length))
    }
}

fun ErrorType.toColor() = Color.Red
