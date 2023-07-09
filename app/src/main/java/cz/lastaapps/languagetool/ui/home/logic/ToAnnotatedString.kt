package cz.lastaapps.languagetool.ui.home.logic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import cz.lastaapps.languagetool.data.model.ErrorType
import cz.lastaapps.languagetool.data.model.MatchedText

@Composable
internal fun MatchedText.toAnnotatedString() = remember(this) {
    buildAnnotatedString {
        var lastIndex = 0
        errors.forEach {
            append(text.substring(lastIndex, it.range.first))

            val color = it.errorType.toColor()
            pushStyle(
                SpanStyle(
                    color = color,
                    textDecoration = TextDecoration.Underline,
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
