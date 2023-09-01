package cz.lastaapps.languagetool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cz.lastaapps.languagetool.ui.navigation.Router
import cz.lastaapps.languagetool.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            AppTheme(
                colorSystemBars = true,
            ) {
                Router()
            }
        }
    }
}
