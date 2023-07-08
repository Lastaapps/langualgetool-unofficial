package cz.lastaapps.languagetool.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
fun HomeScreenScaffold(
    snackbarHostState: SnackbarHostState,
    text: @Composable (Modifier) -> Unit,
    actionChips: @Composable () -> Unit,
    appBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
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
            Title(modifier = Modifier.align(Alignment.CenterHorizontally))

            text(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )

            actionChips()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Title(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = "LanguageTool",
            style = MaterialTheme.typography.displaySmall,
        )

        Badge {
            Text(
                text = "Unofficial",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
