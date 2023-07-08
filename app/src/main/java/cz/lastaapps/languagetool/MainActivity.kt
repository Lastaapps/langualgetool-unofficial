package cz.lastaapps.languagetool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cz.lastaapps.languagetool.ui.home.HomeScreenScaffold
import cz.lastaapps.languagetool.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            AppTheme(
                colorSystemBars = true,
            ) {
                var text by remember {
                    mutableStateOf(TextFieldValue(""))
                }

                HomeScreenScaffold(
                    text = text,
                    onText = { text = it },
                    onCheck = {},
                    onLogin = {},
                    onAbout = {},
                    onSettings = {},
                    onChooseLanguage = {},
                    onSystemSpellCheck = {},
                )
            }
        }
    }
}
