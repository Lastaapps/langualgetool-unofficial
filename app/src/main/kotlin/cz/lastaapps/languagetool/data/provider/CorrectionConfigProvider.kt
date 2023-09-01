package cz.lastaapps.languagetool.data.provider

import cz.lastaapps.languagetool.data.AppPreferences
import cz.lastaapps.languagetool.data.getCorrectionConfig
import cz.lastaapps.languagetool.domain.model.CorrectionConfig
import kotlinx.coroutines.flow.first

internal interface CorrectionConfigProvider {
    suspend fun getCorrectionConfig(): CorrectionConfig
}

internal class CorrectionConfigProviderImpl(
    private val preferences: AppPreferences,
) : CorrectionConfigProvider {
    override suspend fun getCorrectionConfig(): CorrectionConfig =
        preferences.getCorrectionConfig().first()
}
