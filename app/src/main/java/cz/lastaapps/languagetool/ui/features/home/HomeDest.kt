package cz.lastaapps.languagetool.ui.features.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import cz.lastaapps.languagetool.ui.features.home.components.ActionChips
import cz.lastaapps.languagetool.ui.features.home.components.ErrorSuggestionRow
import cz.lastaapps.languagetool.ui.features.home.components.HomeBottomAppBar
import cz.lastaapps.languagetool.ui.features.home.components.TextCorrectionField
import cz.lastaapps.languagetool.ui.features.home.model.CheckProgress


@Composable
internal fun HomeDest(
    viewModel: HomeViewModel,
    toAbout: () -> Unit,
    toHelp: () -> Unit,
    toLanguage: () -> Unit,
    toSettings: () -> Unit,
    toSpellCheck: () -> Unit,
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState =
        remember { SnackbarHostState() },
) {
    val state by viewModel.getState().collectAsState()
    var cursorPosition by remember { mutableStateOf(0) }
    val uriHandler = LocalUriHandler.current

    val textBlock: @Composable (Modifier) -> Unit = { localModifier ->
        TextCorrectionField(
            matched = state.matched,
            onText = viewModel::onTextChanged,
            onCursor = { cursorPosition = it },
            charLimit = 20_000, // TODO
            modifier = localModifier,
        )
    }
    val errorSuggestions: @Composable () -> Unit = {
        ErrorSuggestionRow(
            cursorPosition = cursorPosition,
            matched = state.matched,
            onApplySuggestion = viewModel::applySuggestion,
        )
    }
    val chipsBlock: @Composable () -> Unit = {
        ActionChips(
            matched = state.matched,
            onPasteText = {
                viewModel.onTextChanged(it)
                viewModel.onCheckRequest()
            },
            onError = { /*TODO*/ },
            isPicky = false,
            onPickyClick = { /*TODO*/ },
            selectedLanguage = null,
            onLanguageClick = toLanguage,
            hasPremium = false,
            onPremiumClick = {
                uriHandler.openUri("https://languagetool.org/premium_new")
            },
            onHelpClick = toHelp,
        )
    }
    val appBarBlock: @Composable () -> Unit = {
        HomeBottomAppBar(
            progress = CheckProgress.Ready,
            onCheck = { viewModel.onCheckRequest() },
            onSystemSpellCheck = toSpellCheck,
            onHelpClick = toHelp,
            onSettings = toSettings,
            onAbout = toAbout,
        )
    }

    HomeScreenScaffold(
        snackbarHostState = hostState,
        text = textBlock,
        actionChips = chipsBlock,
        errorSuggestions = errorSuggestions,
        appBar = appBarBlock,
        modifier = modifier,
    )
}

