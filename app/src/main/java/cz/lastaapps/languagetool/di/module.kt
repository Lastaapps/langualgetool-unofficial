package cz.lastaapps.languagetool.di

import cz.lastaapps.languagetool.api.LanguageToolApi
import cz.lastaapps.languagetool.api.LanguageToolApiImpl
import cz.lastaapps.languagetool.api.createHttpClient
import cz.lastaapps.languagetool.data.AppPreferences
import cz.lastaapps.languagetool.data.AppPreferencesImpl
import cz.lastaapps.languagetool.data.LangToolRepository
import cz.lastaapps.languagetool.data.LangToolRepositoryImpl
import cz.lastaapps.languagetool.data.provider.ApiCredentialsProvider
import cz.lastaapps.languagetool.data.provider.ApiCredentialsProviderImpl
import cz.lastaapps.languagetool.data.provider.CorrectionConfigProvider
import cz.lastaapps.languagetool.data.provider.CorrectionConfigProviderImpl
import cz.lastaapps.languagetool.data.provider.UrlProvider
import cz.lastaapps.languagetool.data.provider.UrlProviderImpl
import cz.lastaapps.languagetool.ui.features.home.HomeViewModel
import cz.lastaapps.languagetool.ui.features.language.LanguageDialogViewModel
import cz.lastaapps.languagetool.ui.features.settings.SettingsViewModel
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
    viewModelOf(::SettingsViewModel)

    singleOf(::LangToolRepositoryImpl) bind LangToolRepository::class
    factoryOf(::LanguageToolApiImpl) bind LanguageToolApi::class
    singleOf(::AppPreferencesImpl) bind AppPreferences::class

    factoryOf(::UrlProviderImpl) bind UrlProvider::class
    factoryOf(::CorrectionConfigProviderImpl) bind CorrectionConfigProvider::class
    factoryOf(::ApiCredentialsProviderImpl) bind ApiCredentialsProvider::class
}
