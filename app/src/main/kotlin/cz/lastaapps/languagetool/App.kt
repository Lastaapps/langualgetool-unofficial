package cz.lastaapps.languagetool

import android.app.Application
import cz.lastaapps.languagetool.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(
                module,
            )
        }
    }
}