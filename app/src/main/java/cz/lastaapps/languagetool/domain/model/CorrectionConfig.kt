package cz.lastaapps.languagetool.domain.model

data class CorrectionConfig(
    val language: String?,
    val isPicky: Boolean,
    val motherTongue: String?,
)
