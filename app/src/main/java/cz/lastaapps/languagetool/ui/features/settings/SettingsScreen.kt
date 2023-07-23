package cz.lastaapps.languagetool.ui.features.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.ui.features.settings.model.ApiUrlState
import cz.lastaapps.languagetool.ui.features.settings.model.CredentialsState
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
internal fun SettingsScreen(
    apiUrlState: ApiUrlState,
    onSaveApiUrl: (apiUrl: String) -> Unit,
    credentialsState: CredentialsState,
    onSaveCredentials: (username: String, apiKey: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var apiUrl by rememberSaveable(apiUrlState.url) {
        mutableStateOf(apiUrlState.url)
    }
    var username by rememberSaveable(credentialsState.username) {
        mutableStateOf(credentialsState.username)
    }
    var apiKey by rememberSaveable(credentialsState.apiKey) {
        mutableStateOf(credentialsState.apiKey)
    }
    val apiUrlSame = apiUrlState.url == apiUrl && !apiUrlState.isChecking
    val credentialsSame = credentialsState.username == username && credentialsState.apiKey == apiKey
        && !credentialsState.isChecking

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(
            PaddingTokens.Medium,
            Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.title_settings),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )

        val textFieldModifier = Modifier
            .fillMaxWidth()
            .sizeIn(maxWidth = 512.dp)

        OutlinedTextField(
            value = apiUrl,
            onValueChange = { apiUrl = it },
            label = { Text(stringResource(id = R.string.label_custom_api_url)) },
            placeholder = { Text(SettingsTokens.defaultApiUrl) },
            enabled = !apiUrlState.isChecking,
            maxLines = 1,
            modifier = textFieldModifier,
        )

        Checked(
            isValid = apiUrlState.isValid,
            areSame = apiUrlSame,
        )

        Button(
            onClick = { onSaveApiUrl(apiUrl) },
            enabled = !apiUrlState.isChecking,
        ) {
            Text(text = stringResource(id = R.string.button_save_and_check))
        }


        Spacer(Modifier.height(PaddingTokens.ExtraLarge))


        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.label_username_email)) },
            placeholder = { Text(SettingsTokens.exampleUsername) },
            enabled = !credentialsState.isChecking,
            maxLines = 1,
            modifier = textFieldModifier,
        )
        TextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text(stringResource(id = R.string.label_api_key)) },
            placeholder = { Text(SettingsTokens.exampleApiKey) },
            enabled = !credentialsState.isChecking,
            maxLines = 1,
            modifier = textFieldModifier,
        )

        Checked(
            isValid = credentialsState.isValid,
            areSame = credentialsSame,
        )

        Button(
            onClick = { onSaveCredentials(username, apiKey) },
            enabled = !credentialsState.isChecking,
        ) {
            Text(text = stringResource(id = R.string.button_save_and_check))
        }
    }
}

@Composable
private fun Checked(
    isValid: Boolean?,
    areSame: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(PaddingTokens.Medium),
    ) {
        if (areSame && isValid != null) {
            val color = if (isValid) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.error
            }
            val icon = if (isValid) {
                Icons.Default.Verified
            } else {
                Icons.Default.Error
            }
            val text = if (isValid) {
                R.string.text_checked
            } else {
                R.string.text_invalid
            }.let { stringResource(id = it) }

            Icon(icon, contentDescription = null, tint = color)
            Text(text = text, color = color)
        }
    }
}

private object SettingsTokens {
    const val defaultApiUrl = "https://api.languagetool.org/"
    const val exampleUsername = "sponge.bob@bikinibottom.com"

    @Suppress("SpellCheckingInspection")
    const val exampleApiKey = "XXXXXXXXXXXXXXXX"
}
