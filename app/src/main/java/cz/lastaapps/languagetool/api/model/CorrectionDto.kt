package cz.lastaapps.languagetool.api.model

import arrow.core.some
import cz.lastaapps.languagetool.domain.model.MatchedText
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Serializable
internal data class CorrectionDto(
    val software: SoftwareDto,
    val warnings: WarningDto? = null,
    val language: LanguageDto,
    val matches: List<MatchDto>,
    val hiddenMatches: List<MatchDto> = emptyList(),
)

@Serializable
internal data class SoftwareDto(
    val name: String,
    val version: String,
    val buildDate: String,
    val apiVersion: Int,
    val status: String? = null,
    val premium: Boolean? = null,
)

@Serializable
internal data class WarningDto(
    val incompleteResults: Boolean? = null,
)

@Serializable
internal data class LanguageDto(
    val name: String,
    val code: String,
    val detectedLanguage: DetectedLanguageDto,
)

@Serializable
internal data class DetectedLanguageDto(
    val name: String,
    val code: String,
)

internal fun CorrectionDto.toDomain(
    text: String,
) = MatchedText(
    text = text,
    errors = (matches + hiddenMatches)
        .sortedBy { it.offset }
        .mapIndexed { index, it -> it.toDomain(index, text) }
        .toPersistentList(),
    isComplete = (this.warnings?.incompleteResults ?: true).some(),
    isTouched = false,
)
