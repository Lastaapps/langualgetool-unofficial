package cz.lastaapps.languagetool.ui.features.settings.model

internal data class CredentialsState(
    val username: String,
    val apiKey: String,
    val isChecking: Boolean,
    val isValid: Boolean?,
)
