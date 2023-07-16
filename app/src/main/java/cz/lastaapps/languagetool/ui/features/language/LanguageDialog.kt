package cz.lastaapps.languagetool.ui.features.language

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.domain.model.Language
import cz.lastaapps.languagetool.ui.theme.PaddingTokens
import cz.lastaapps.languagetool.ui.util.WrapClick
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun LanguageDialogDest(
    onNavigateUp: () -> Unit,
    viewModel: LanguageDialogViewModel = koinViewModel(),
) {
    val state by viewModel.flowState
    LaunchedEffect(viewModel) {
        viewModel.onAppear()
    }

    // TODO handle error properly
    LaunchedEffect(state.error) {
        state.error?.let {
            onNavigateUp()
        }
    }

    LaunchedEffect(state.canLeave) {
        if (state.canLeave) {
            onNavigateUp()
        }
    }

    LanguageDialogContent(
        isLoading = state.isLoading,
        languages = state.languageList,
        onLangSelected = viewModel::onLanguageSelected,
    )
}

@Composable
internal fun LanguageDialogContent(
    isLoading: Boolean,
    languages: ImmutableList<Language>,
    onLangSelected: (Language?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            stringResource(id = R.string.title_select_language),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(PaddingTokens.Medium))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    WrapClick(onClick = { onLangSelected(null) }) {
                        Text(
                            text = stringResource(id = R.string.label_language_auto),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                items(languages) { lang ->
                    WrapClick(onClick = { onLangSelected(lang) }) {
                        Text(
                            text = lang.name + " (" + lang.longCode + ")",
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
