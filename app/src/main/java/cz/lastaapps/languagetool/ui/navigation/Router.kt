package cz.lastaapps.languagetool.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.lastaapps.languagetool.ui.components.saneDialog
import cz.lastaapps.languagetool.ui.features.dialogs.HelpDialogContent
import cz.lastaapps.languagetool.ui.features.dialogs.SpellcheckDialog
import cz.lastaapps.languagetool.ui.features.home.HomeDest
import cz.lastaapps.languagetool.ui.features.language.LanguageDialogDest
import cz.lastaapps.languagetool.ui.features.settings.SettingsDest
import org.koin.androidx.compose.koinViewModel

@Composable
fun Router(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navUp = { navController.navigateUp().let { } }

    NavHost(
        navController = navController,
        startDestination = Dests.starting,
        modifier = modifier,
    ) {
        composable(Dests.HOME) {
            HomeDest(
                viewModel = koinViewModel(),
                toAbout = { navController.navigate(Dests.ABOUT) },
                toHelp = { navController.navigate(Dests.HELP) },
                toLanguage = { navController.navigate(Dests.LANGUAGE) },
                toSettings = { navController.navigate(Dests.SETTINGS) },
                toSpellCheck = { navController.navigate(Dests.SPELLCHECK) },
            )
        }
        composable(Dests.SETTINGS) {
            SettingsDest(
                viewModel = koinViewModel(),
            )
        }
        saneDialog(Dests.LANGUAGE, navUp) {
            LanguageDialogDest(
                onNavigateUp = {
                    navController.navigateUp()
                },
            )
        }
        saneDialog(Dests.SPELLCHECK, navUp) { SpellcheckDialog() }
        saneDialog(Dests.HELP, navUp) { HelpDialogContent() }
        saneDialog(Dests.ABOUT, navUp) { Text("About") }
    }
}
