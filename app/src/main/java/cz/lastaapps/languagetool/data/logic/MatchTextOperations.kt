package cz.lastaapps.languagetool.data.logic

import cz.lastaapps.languagetool.data.model.MatchedText
import cz.lastaapps.languagetool.ui.util.sizeAsExclusive
import cz.lastaapps.languagetool.ui.util.withLength
import kotlinx.collections.immutable.toImmutableList

internal fun MatchedText.replace(
    range: IntRange,
    newText: String,
): MatchedText {
//    val updatedText = buildString(this.text.length - range.sizeAsExclusive + newText.length) {
//        append(text.substring(0, range.first))
//        append(newText)
//        append(text.substring(range.last + 1, text.length))
//    }
    val updatedText = text.replaceRange(range, newText)

    val rangeWidth = newText.length - range.sizeAsExclusive
    val validMatches = errors.filter { error ->
        error.range.first < range.first
                || range.last <= error.range.last // both off by 1
    }.map { error ->
        if (error.range.first < range.first) {
            error
        } else {
            error.copy(
                range = error.range.let { errorRange ->
                    IntRange(
                        errorRange.first + rangeWidth,
                        errorRange.last + rangeWidth,
                    )
                }
            )
        }
    }.toImmutableList()

    return copy(
        text = updatedText,
        errors = validMatches,
    )
}

internal fun textDiff(
    t1: String, // old
    t2: String, // new
): Pair<IntRange, String> {
    var firstDiffIndex = -1

    for (i in 0 until kotlin.math.min(t1.length, t2.length)) {
        if (t1[i] != t2[i]) {
            firstDiffIndex = i
            break
        }
    }

    if (firstDiffIndex == -1) {
        return when {
            t1.length > t2.length -> t2.length withLength t1.length - t2.length to ""
            t1.length < t2.length -> t1.length withLength 0 to t2.substring(t1.length, t2.length)
            else -> 0 withLength 0 to ""
        }
    }

    var lastDiffIndex = -1
    for (i in 0 until kotlin.math.min(t1.length, t2.length)) {
        if (t1[t1.lastIndex - i] != t2[t2.lastIndex - i]) {
            lastDiffIndex = i
            break
        }
    }

    if (lastDiffIndex == -1) {
        return when {
            t1.length > t2.length -> 0 withLength t1.length - t2.length to ""
            t1.length < t2.length -> 0 withLength 0 to t2.substring(0, t2.length - t1.length)
            else -> 0 withLength 0 to ""
        }
    }


    val diffLength = (t1.lastIndex - lastDiffIndex) - firstDiffIndex + 1

    return firstDiffIndex withLength diffLength to t2.substring(
        firstDiffIndex,
        t2.lastIndex - lastDiffIndex + 1
    )
}

