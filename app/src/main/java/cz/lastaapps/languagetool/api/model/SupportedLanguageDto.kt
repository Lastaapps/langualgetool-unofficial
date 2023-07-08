package cz.lastaapps.languagetool.api.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SupportedLanguageDto(
    val name: String,
    val code: String,
    val longCode: String,
)
