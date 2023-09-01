package cz.lastaapps.languagetool.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
internal fun createHttpClient() = HttpClient {

    install(HttpTimeout) {
        requestTimeoutMillis = 60_000
    }

    install(ContentNegotiation) {
        this.json(
            Json {
                ignoreUnknownKeys = true
                explicitNulls = true
                prettyPrint = true
            },
        )
    }
    install(Logging) {
        level = LogLevel.ALL
    }

    followRedirects = true
    expectSuccess = true
}
