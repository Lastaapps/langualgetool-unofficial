package cz.lastaapps.languagetool.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreenScaffold(
    snackbarHostState: SnackbarHostState,
    text: @Composable (Modifier) -> Unit,
    errorSuggestions: @Composable () -> Unit,
    actionChips: @Composable () -> Unit,
    appBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOpen: Boolean = WindowInsets.isImeVisible,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = appBar,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(PaddingTokens.More.Screen)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingTokens.Medium),
        ) {
            Title(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isSmall = keyboardOpen,
            )

            text(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )

            errorSuggestions()

            if (!keyboardOpen) {
                actionChips()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Title(
    isSmall: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isSmall) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = stringResource(id = R.string.title_app_name),
                style = MaterialTheme.typography.displaySmall,
            )

            Badge {
                Text(
                    text = stringResource(id = R.string.title_app_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        vertical = PaddingTokens.Tiny,
                        horizontal = PaddingTokens.Small,
                    )
                )
            }

        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(PaddingTokens.MidSmall),
        ) {
            Text(
                text = stringResource(id = R.string.title_app_name),
                style = MaterialTheme.typography.titleLarge,
            )

            Badge {
                Text(
                    text = stringResource(id = R.string.title_app_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        vertical = PaddingTokens.Tiny,
                        horizontal = PaddingTokens.Small,
                    )
                )
            }

        }
    }
}
