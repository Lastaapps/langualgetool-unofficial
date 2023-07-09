package cz.lastaapps.languagetool.data.model

import arrow.core.None
import arrow.core.Option
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class MatchedText(
    val text: String,
    val errors: ImmutableList<MatchedError>,
    val isComplete: Option<Boolean>, // the text may not have been checked yet at all
) {
    companion object {
        val empty
            get() = MatchedText(
                text = "This sentenci wrong is.",
                // text = "Errors will be underlined in different colours: we will mark seplling errors with red underilnes. Furthermore grammar error's are highlighted in yellow.",
                errors = persistentListOf(),
                isComplete = None,
            )
    }
}

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
