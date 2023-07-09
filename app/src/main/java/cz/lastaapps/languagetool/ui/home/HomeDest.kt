package cz.lastaapps.languagetool.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun HomeDest(
    viewModel: HomeViewModel,
    toAbout: () -> Unit,
    toHelp: () -> Unit,
    toLanguage: () -> Unit,
    toLogin: () -> Unit,
    toSettings: () -> Unit,
    toSpellCheck: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState =
        remember { SnackbarHostState() },
) {
    val textBlock: @Composable (Modifier) -> Unit = {

    }
    val chipsBlock: @Composable () -> Unit = {
        Button(onClick = toSettings) {
            Text("Settings")
        }
    }
    val appBarBlock: @Composable () -> Unit = {
        Button(onClick = toAbout) {
            Text("About")
        }
    }

    HomeScreenScaffold(
        snackbarHostState = snackbarHostState,
        text = textBlock,
        actionChips = chipsBlock,
        appBar = appBarBlock,
        modifier = modifier,
    )
}