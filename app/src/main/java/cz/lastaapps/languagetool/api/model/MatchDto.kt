package cz.lastaapps.languagetool.api.model

import kotlinx.serialization.Serializable

@Serializable
internal data class MatchDto(
    val message: String,
    val shortMessage: String?,
    val offset: Int,
    val length: Int,
    val replacements: List<ReplacementDto>,
    val context: ContextDto,
    val sentence: String,
    // val rule: RuleDto,
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
