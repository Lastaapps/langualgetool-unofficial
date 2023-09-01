package cz.lastaapps.languagetool.ui.features.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
internal fun SettingsDest(
    viewModel: SettingsViewModel,
) {
    val state by viewModel.flowState

    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    Scaffold { paddingValues ->
        SettingsScreen(
            apiUrlState = state.apiUrl,
            onSaveApiUrl = viewModel::saveApiUrl,
            credentialsState = state.credentials,
            onSaveCredentials = viewModel::saveCredentials,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(PaddingTokens.More.Screen),
        )
    }
}
