package cz.lastaapps.languagetool.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import cz.lastaapps.languagetool.ui.home.HomeDest
import org.koin.androidx.compose.koinViewModel

@Composable
fun Router(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
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
                toLogin = { navController.navigate(Dests.LOGIN) },
                toSettings = { navController.navigate(Dests.SETTINGS) },
                toSpellCheck = { navController.navigate(Dests.SPELLCHECK) },
            )
        }
        composable(Dests.SETTINGS) { Text("Settings") }
        dialog(Dests.LANGUAGE) { Text("Language") }
        dialog(Dests.LOGIN) { Text("Login") }
        dialog(Dests.SPELLCHECK) { Text("Spellcheck") }
        dialog(Dests.HELP) { Text("Help") }
        dialog(Dests.ABOUT) { Text("About") }
    }
}
