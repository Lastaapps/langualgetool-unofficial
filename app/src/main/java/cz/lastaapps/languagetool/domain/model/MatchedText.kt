package cz.lastaapps.languagetool.domain.model

import arrow.core.None
import arrow.core.Option
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class MatchedText(
    val text: String,
    val errors: ImmutableList<MatchedError>,
    val isComplete: Option<Boolean>, // the text may not have been checked yet at all
    val isTouched: Boolean, // if the user has edited the text after the request
) {
    companion object {
        @Suppress("SpellCheckingInspection")
        val empty
            get() = MatchedText(
//                text = "",
//                text = "This sentenci seplling wrong is.",
                text = "So how are you doing thoday?",
                //text = "Errors will be underlined in different colours: we will mark seplling errors with red underilnes. Furthermore grammar error's are highlighted in yellow.",
                errors = persistentListOf(),
                isComplete = None,
                isTouched = true,
            )
    }
}

internal data class MatchedError(
    val index: Int,
    val message: String,
    val shortMessage: String?,
    val range: IntRange,
    val original: String,
    val replacements: ImmutableList<String>,
    val ruleDescription: String,
    val errorType: ErrorType,
    val explanationUrl: String?,
    val isPremium: Boolean,
) {
    companion object {
        fun example(range: IntRange) =
            MatchedError(
                index = 0,
                message = "Use “a” instead of ‘an’ if the following word doesn’t start with a vowel sound, e.g. ‘a sentence’, ‘a university’.",
                shortMessage = "Wrong article",
                range = range,
                original = "hitler",
                replacements = persistentListOf("hello", "hi"),
                ruleDescription = "Use of 'a' vs. 'an'",
                errorType = ErrorType.PUNCTUATION,
                explanationUrl = "",
                isPremium = true,
            )
    }
}

enum class ErrorType {
    GRAMMAR, SPELLING, STYLE, PUNCTUATION, OTHER
}
