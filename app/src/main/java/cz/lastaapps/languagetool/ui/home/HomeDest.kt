package cz.lastaapps.languagetool.ui.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cz.lastaapps.languagetool.ui.home.components.ActionChips
import cz.lastaapps.languagetool.ui.home.components.HomeBottomAppBar
import cz.lastaapps.languagetool.ui.home.components.TextCorrectionField
import cz.lastaapps.languagetool.ui.home.model.CheckProgress

@Composable
internal fun HomeDest(
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
    val state by viewModel.getState().collectAsState()

    val textBlock: @Composable (Modifier) -> Unit = {
        TextCorrectionField(
            text = state.matched,
            onText = viewModel::onTextChanged,
            onCursor = {},
            onCheckRequest = viewModel::onCheckRequest,
            charLimit = 20_000,
            modifier = it,
        )
    }
    val chipsBlock: @Composable () -> Unit = {
        ActionChips(
            isPicky = false,
            onPickyClick = { /*TODO*/ },
            selectedLanguage = null,
            onLanguageClick = { /*TODO*/ },
            hasPremium = false,
            onPremiumClick = { /*TODO*/ },
            onHelpClick = { /*TODO*/ },
        )
    }
    val appBarBlock: @Composable () -> Unit = {
        HomeBottomAppBar(
            progress = CheckProgress.Ready,
            onCheck = { viewModel.onCheckRequest() },
            onSystemSpellCheck = { /*TODO*/ },
            onLogin = { /*TODO*/ },
            onSettings = { /*TODO*/ },
            onAbout = { /*TODO*/ },
        )
    }

    HomeScreenScaffold(
        snackbarHostState = snackbarHostState,
        text = textBlock,
        actionChips = chipsBlock,
        appBar = appBarBlock,
        modifier = modifier,
    )
}
