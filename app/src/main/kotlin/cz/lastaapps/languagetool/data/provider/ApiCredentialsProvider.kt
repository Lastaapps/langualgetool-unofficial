package cz.lastaapps.languagetool.data.provider

import arrow.core.Option
import arrow.core.toOption
import cz.lastaapps.languagetool.data.AppPreferences
import cz.lastaapps.languagetool.data.getApiCredentials
import cz.lastaapps.languagetool.domain.model.ApiCredentials
import kotlinx.coroutines.flow.first

internal interface ApiCredentialsProvider {
    suspend fun provideCredentials(): Option<ApiCredentials>
}

internal class ApiCredentialsProviderImpl(
    private val preferences: AppPreferences,
) : ApiCredentialsProvider {
    override suspend fun provideCredentials(): Option<ApiCredentials> =
        preferences.getApiCredentials().first().toOption()
}
