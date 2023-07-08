package cz.lastaapps.languagetool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cz.lastaapps.languagetool.ui.home.HomeScreen
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

                HomeScreen(
                    text = text,
                    onText = {text = it},
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
