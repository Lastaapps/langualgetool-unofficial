package cz.lastaapps.languagetool.api

import cz.lastaapps.languagetool.api.model.CorrectionDto
import cz.lastaapps.languagetool.api.model.SupportedLanguageDto
import cz.lastaapps.languagetool.core.Outcome
import cz.lastaapps.languagetool.core.util.catchingNetwork
import cz.lastaapps.languagetool.data.ApiCredentialsProvider
import cz.lastaapps.languagetool.data.UrlProvider
import cz.lastaapps.languagetool.data.model.CorrectionConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.HttpMethod
import io.ktor.util.AttributeKey

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
        client.submitForm(urlProvider.provideUrl() + "/client") {
            method = HttpMethod.Post
            setAttributes {
                put(AttributeKey("text"), text)

                // language
                put(AttributeKey("language"), config.language ?: "auto")

                // motherTongue
                config.motherTongue?.let {
                    put(AttributeKey("motherTongue"), it)
                }

                // level
                put(
                    AttributeKey("level"), if (config.isPicky) {
                        "picky"
                    } else {
                        "default"
                    }
                )

                credentials.onSome {
                    this.put(AttributeKey("username"), it.userName)
                    this.put(AttributeKey("apiKey"), it.apiKey)
                }
            }
        }.body()
    }

    override suspend fun getLanguages(): Outcome<List<SupportedLanguageDto>> = catchingNetwork {
        client.get(urlProvider.provideUrl() + "/languages").body()
    }
}
