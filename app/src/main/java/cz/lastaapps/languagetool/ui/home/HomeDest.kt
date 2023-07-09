package cz.lastaapps.languagetool.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cz.lastaapps.languagetool.ui.home.components.ActionChips
import cz.lastaapps.languagetool.ui.home.components.HomeBottomAppBar
import cz.lastaapps.languagetool.ui.home.components.TextCorrectionField
import cz.lastaapps.languagetool.ui.home.model.CheckProgress
import cz.lastaapps.languagetool.ui.home.components.ErrorSuggestionRow


@OptIn(ExperimentalFoundationApi::class)
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
    var cursorPosition by remember { mutableStateOf(0) }

    val textBlock: @Composable (Modifier) -> Unit = { localModifier ->
        TextCorrectionField(
            matched = state.matched,
            onText = viewModel::onTextChanged,
            onCursor = { cursorPosition = it },
            onCheckRequest = viewModel::onCheckRequest,
            charLimit = 20_000,
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
            onText = viewModel::onTextChanged,
            onError = { /*TODO*/ },
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
        errorSuggestions = errorSuggestions,
        appBar = appBarBlock,
        modifier = modifier,
    )
}

