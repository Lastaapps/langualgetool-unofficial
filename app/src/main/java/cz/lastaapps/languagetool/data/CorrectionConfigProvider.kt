package cz.lastaapps.languagetool.data

import cz.lastaapps.languagetool.data.model.CorrectionConfig

internal interface CorrectionConfigProvider {
    suspend fun getCorrectionConfig(): CorrectionConfig
}
