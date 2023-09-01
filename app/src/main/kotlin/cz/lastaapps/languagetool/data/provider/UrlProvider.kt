package cz.lastaapps.languagetool.data.provider

import cz.lastaapps.languagetool.data.AppPreferences
import kotlinx.coroutines.flow.first

internal interface UrlProvider {
    suspend fun provideUrl(): String
}

internal class UrlProviderImpl(
    private val preferences: AppPreferences,
) : UrlProvider {
    override suspend fun provideUrl(): String =
        preferences.getApiUrl().first() ?: "https://api.languagetool.org"
}
