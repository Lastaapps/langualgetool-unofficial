package cz.lastaapps.languagetool.ui.features.settings.model

internal data class ApiUrlState(
    val url: String,
    val isChecking: Boolean,
    val isValid: Boolean?,
)
