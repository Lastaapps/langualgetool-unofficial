package cz.lastaapps.languagetool.data.model

import arrow.core.Option
import kotlinx.collections.immutable.ImmutableList

internal data class MatchedText(
    val text: String,
    val errors: ImmutableList<MatchedError>,
    val isComplete: Option<Boolean>, // the text may not have been checked yet at all
)

internal data class MatchedError(
    val message: String,
    val shortMessage: String?,
    val range: IntRange,
    val replacements: ImmutableList<String>,
    val ruleDescription: String,
    val errorType: ErrorType,
    val explanationUrl: String?,
)

enum class ErrorType {
    GRAMMAR, SPELLING, STYLE, PUNCTUATION, OTHER
}
