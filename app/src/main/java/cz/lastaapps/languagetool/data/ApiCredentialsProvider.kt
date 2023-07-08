package cz.lastaapps.languagetool.data

import arrow.core.Option
import cz.lastaapps.languagetool.data.model.ApiCredentials

internal interface ApiCredentialsProvider {
    suspend fun provideCredentials(): Option<ApiCredentials>
}
