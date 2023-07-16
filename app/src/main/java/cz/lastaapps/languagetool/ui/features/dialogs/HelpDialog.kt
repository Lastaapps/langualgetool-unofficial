package cz.lastaapps.languagetool.ui.features.dialogs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.ui.theme.PaddingTokens
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HelpDialogContent(
    modifier: Modifier = Modifier,
) {
    val items = getStrings()
    LazyColumn(
        modifier = modifier,
    ) {
        items(items) { (title, description) ->
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(Modifier.height(PaddingTokens.MidSmall))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(PaddingTokens.MidLarge))
        }
    }
}

@Composable
private fun getStrings(): ImmutableList<Pair<String, String>> =
    remember {
        persistentListOf(
            R.string.help_disclaimer_title to R.string.help_disclaimer_description,
            R.string.help_premium_title to R.string.help_premium_description,
            R.string.help_rate_limit_title to R.string.help_rate_limit_description,
            R.string.help_picky_title to R.string.help_picky_description,
            R.string.help_language_title to R.string.help_language_description,
        )
    }.map { (title, description) ->
        stringResource(id = title) to stringResource(id = description)
    }.toImmutableList()
