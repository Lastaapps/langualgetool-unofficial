package cz.lastaapps.languagetool.di

import arrow.core.None
import arrow.core.Option
import cz.lastaapps.languagetool.api.LanguageToolApi
import cz.lastaapps.languagetool.api.LanguageToolApiImpl
import cz.lastaapps.languagetool.api.createHttpClient
import cz.lastaapps.languagetool.data.ApiCredentialsProvider
import cz.lastaapps.languagetool.data.CorrectionConfigProvider
import cz.lastaapps.languagetool.data.LangToolRepository
import cz.lastaapps.languagetool.data.LangToolRepositoryImpl
import cz.lastaapps.languagetool.data.UrlProvider
import cz.lastaapps.languagetool.data.model.ApiCredentials
import cz.lastaapps.languagetool.data.model.CorrectionConfig
import cz.lastaapps.languagetool.ui.features.home.HomeViewModel
import cz.lastaapps.languagetool.ui.features.language.LanguageDialogViewModel
import kotlinx.datetime.Clock
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val module = module {
    single { createHttpClient() }
    single { Clock.System } bind Clock::class

    viewModelOf(::HomeViewModel)
    viewModelOf(::LanguageDialogViewModel)

    singleOf(::LangToolRepositoryImpl) bind LangToolRepository::class
    factoryOf(::LanguageToolApiImpl) bind LanguageToolApi::class

    factory {
        object : UrlProvider {
            override suspend fun provideUrl(): String {
                return "https://api.languagetool.org/v2"
            }
        }
    } bind UrlProvider::class
    factory {
        object : CorrectionConfigProvider {
            override suspend fun getCorrectionConfig(): CorrectionConfig {
                return CorrectionConfig(null, false, null)
            }
        }
    } bind CorrectionConfigProvider::class
    factory {
        object : ApiCredentialsProvider {
            override suspend fun provideCredentials(): Option<ApiCredentials> {
                return None
            }
        }
    } bind ApiCredentialsProvider::class
}
