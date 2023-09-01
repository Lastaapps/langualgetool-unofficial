package cz.lastaapps.languagetool.ui.features.dialogs

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
internal fun SpellcheckDialog(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.spellcheck_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(PaddingTokens.MidSmall))

        Text(
            text = stringResource(id = R.string.spellcheck_description),
            style = MaterialTheme.typography.bodyMedium,
        )

        Button(
            onClick = {
                context.startActivity(Intent(Settings.ACTION_SETTINGS))
            },
        ) {
            Text(stringResource(id = R.string.button_open_settings))
        }
    }
}
