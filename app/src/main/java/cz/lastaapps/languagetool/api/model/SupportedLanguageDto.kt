package cz.lastaapps.languagetool.api.model

import cz.lastaapps.languagetool.domain.model.Language
import kotlinx.serialization.Serializable

@Serializable
internal data class SupportedLanguageDto(
    val name: String,
    val code: String,
    val longCode: String,
)

internal fun SupportedLanguageDto.toDomain() =
    Language(
        name = name,
        code = code,
        longCode = longCode,
    )
