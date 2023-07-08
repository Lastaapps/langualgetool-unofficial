package cz.lastaapps.languagetool.data

interface UrlProvider {
    suspend fun provideUrl(): String
}