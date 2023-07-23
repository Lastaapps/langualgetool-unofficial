package cz.lastaapps.languagetool.domain.logic

import cz.lastaapps.languagetool.domain.model.MatchedText
import cz.lastaapps.languagetool.ui.util.moveBy
import cz.lastaapps.languagetool.ui.util.sizeAsExclusive
import cz.lastaapps.languagetool.ui.util.withLength
import kotlinx.collections.immutable.toPersistentList

internal fun MatchedText.replace(
    range: IntRange,
    newText: String,
): MatchedText {

    // empty range
    if (range.last < range.first && newText == "") {
        return this
    }

//    val updatedText = buildString(this.text.length - range.sizeAsExclusive + newText.length) {
//        append(text.substring(0, range.first))
//        append(newText)
//        append(text.substring(range.last + 1, text.length))
//    }
    val updatedText = text.replaceRange(range, newText)

    val rangeWidth = newText.length - range.sizeAsExclusive
    val validMatches = errors.filter { error ->
        error.range.last.inc() < range.first // end of error
            || range.last.inc() < error.range.first // start of error
    }.map { error ->
        if (error.range.first < range.first) {
            error
        } else {
            error.copy(
                range = error.range.moveBy(rangeWidth),
            )
        }
    }.toPersistentList()

    return copy(
        text = updatedText,
        errors = validMatches,
        isTouched = true,
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
    for (i in 0 until kotlin.math.min(t1.length, t2.length) - firstDiffIndex) {
        if (t1[t1.lastIndex - i] != t2[t2.lastIndex - i]) {
            lastDiffIndex = i
            break
        }
    }

    if (lastDiffIndex == -1) {
        return when {
            t1.length > t2.length -> (0 withLength t1.length - t2.length).moveBy(firstDiffIndex) to ""
            t1.length < t2.length -> firstDiffIndex withLength 0 to t2.substring(
                (0 until t2.length - t1.length).moveBy(
                    firstDiffIndex
                )
            )

            else -> 0 withLength 0 to ""
        }
    }


    val diffLength = (t1.lastIndex - lastDiffIndex) - firstDiffIndex + 1

    return firstDiffIndex withLength diffLength to t2.substring(
        firstDiffIndex,
        t2.lastIndex - lastDiffIndex + 1
    )
}

internal fun MatchedText.getErrorIndexForCursor(cursorPosition: Int): Int =
    errors.indexOfLast {
        it.range.start <= cursorPosition
    }
