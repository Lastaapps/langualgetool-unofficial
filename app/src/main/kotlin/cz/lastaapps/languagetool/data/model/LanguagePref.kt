package cz.lastaapps.languagetool.data.model

import cz.lastaapps.languagetool.domain.model.Language
import kotlinx.serialization.Serializable

@Serializable
internal data class LanguageStoreModel(
    val name: String,
    val code: String,
    val longCode: String,
)

internal fun LanguageStoreModel.toDomain() = Language(
    name = name,
    code = code,
    longCode = longCode,
)

internal fun Language.toPref() = LanguageStoreModel(
    name = name,
    code = code,
    longCode = longCode,
)
