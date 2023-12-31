package cz.lastaapps.languagetool.api

import cz.lastaapps.languagetool.api.model.CorrectionDto
import cz.lastaapps.languagetool.api.model.SupportedLanguageDto
import cz.lastaapps.languagetool.core.Outcome
import cz.lastaapps.languagetool.core.util.catchingNetwork
import cz.lastaapps.languagetool.data.provider.ApiCredentialsProvider
import cz.lastaapps.languagetool.data.provider.UrlProvider
import cz.lastaapps.languagetool.domain.model.CorrectionConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters

internal interface LanguageToolApi {
    suspend fun correct(
        text: String,
        config: CorrectionConfig,
    ): Outcome<CorrectionDto>

    suspend fun getLanguages(): Outcome<List<SupportedLanguageDto>>
}

internal class LanguageToolApiImpl(
    private val client: HttpClient,
    private val urlProvider: UrlProvider,
    private val apiCredentialsProvider: ApiCredentialsProvider,
) : LanguageToolApi {
    override suspend fun correct(
        text: String,
        config: CorrectionConfig,
    ): Outcome<CorrectionDto> = catchingNetwork {
        val credentials = apiCredentialsProvider.provideCredentials()
        client.submitForm(
            url = urlProvider.provideUrl() + "/v2/check",
            formParameters = parameters {
                append("text", text)

                // language
                append("language", config.language ?: "auto")

                // motherTongue
                config.motherTongue?.let {
                    append("motherTongue", it)
                }

                // level
                append(
                    "level",
                    if (config.isPicky) {
                        "picky"
                    } else {
                        "default"
                    },
                )

                // hidden rules results
                append("enableHiddenRules", config.hiddenRules.toString())

                credentials.onSome {
                    append("username", it.userName)
                    append("apiKey", it.apiKey)
                }
            },
        ).body()
    }

    override suspend fun getLanguages(): Outcome<List<SupportedLanguageDto>> = catchingNetwork {
        client.get(urlProvider.provideUrl() + "/v2/languages").body()
    }
}
