package cz.lastaapps.languagetool.ui.features.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
internal fun SpellcheckDialog(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Spellcheck",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(PaddingTokens.MidSmall))

        Text(
            text = "TODO why is spellcheck not working",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
