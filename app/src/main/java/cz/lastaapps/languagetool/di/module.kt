package cz.lastaapps.languagetool.di

import cz.lastaapps.languagetool.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val module = module {
    viewModelOf(::HomeViewModel)
}
