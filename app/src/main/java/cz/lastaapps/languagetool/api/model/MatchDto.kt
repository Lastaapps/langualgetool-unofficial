package cz.lastaapps.languagetool.api.model

import cz.lastaapps.languagetool.data.model.ErrorType
import cz.lastaapps.languagetool.data.model.MatchedError
import cz.lastaapps.languagetool.ui.util.withLength
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

// https://languagetool.org/http-api/

@Serializable
internal data class MatchDto(
    val message: String,
    val shortMessage: String?, // may be ""
    val offset: Int,
    val length: Int,
    val replacements: List<ReplacementDto>,
    val context: ContextDto,
    val sentence: String,
    val rule: RuleDto,
)

@Serializable
internal data class ReplacementDto(
    val value: String,
)

@Serializable
internal data class ContextDto(
    val text: String,
    val offset: Int,
    val length: Int,
)

@Serializable
internal data class RuleDto(
    val id: String,
    val subId: String,
    val description: String,
    val urls: List<UrlDto>,
    // https://www.w3.org/International/multilingualweb/lt/drafts/its20/its20.html#lqissue-typevalues
    val issueType: String?,
    val category: CategoryDto?,
)

@Serializable
internal data class UrlDto(
    val value: String,
)

@Serializable
internal data class CategoryDto(
    val id: String,
    val name: String,
)

internal fun MatchDto.toDomain() = MatchedError(
    message = this.message,
    shortMessage = this.shortMessage?.takeIf { it.isNotEmpty() },
    range = offset withLength length,
    replacements = this.replacements.map { it.value }.toImmutableList(),
    ruleDescription = this.rule.description,
    errorType = this.rule.toErrorType(),
    explanationUrl = this.rule.urls.firstOrNull()?.value,
)

internal fun RuleDto.toErrorType() =
    when (this.issueType) {
        "grammar",
        "omission",
        "addition",
        "duplication",
        "inconsistency",
        -> ErrorType.GRAMMAR

        "style",
        "terminology",
        -> ErrorType.STYLE

        "misspelling",
        -> ErrorType.SPELLING


        "typographical",
        -> ErrorType.PUNCTUATION

        else -> ErrorType.OTHER
    }
