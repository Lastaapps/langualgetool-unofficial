package cz.lastaapps.languagetool.ui.features.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.core.error.CommonErrors
import cz.lastaapps.languagetool.core.error.DomainError
import cz.lastaapps.languagetool.domain.model.MatchedText
import cz.lastaapps.languagetool.ui.components.IconWithTitle
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ActionChips(
    matched: MatchedText,
    onPasteText: (String) -> Unit,
    onClear: () -> Unit,
    onError: (DomainError) -> Unit,
    isPicky: Boolean,
    onPickyClick: () -> Unit,
    selectedLanguage: String?,
    onLanguageClick: () -> Unit,
    hasPremium: Boolean,
    onPremiumClick: () -> Unit,
    modifier: Modifier = Modifier,
    clipboard: ClipboardManager = LocalClipboardManager.current,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(PaddingTokens.Small, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
        ) {
//            if (!hasPremium) {
//                TextButton(
//                    onClick = onPremiumClick,
//                ) {
//                    Text(
//                        "Premium",
//                        softWrap = false,
//                        maxLines = 1,
//                        modifier = Modifier.basicMarquee(),
//                    )
//                }
//            }

            IconWithTitle(
                onClick = {
                    clipboard.setText(AnnotatedString(matched.text))
                },
                icon = Icons.Default.ContentCopy,
                title = stringResource(id = R.string.button_copy),
            )
            IconWithTitle(
                onClick = {
                    clipboard.getText()?.let {
                        onPasteText(it.text)
                    } ?: onError(CommonErrors.ClipboardEmpty)
                },
                icon = Icons.Default.ContentPaste,
                title = stringResource(id = R.string.button_paste),
            )
            IconWithTitle(
                onClick = onClear,
                icon = Icons.Outlined.Delete,
                title = stringResource(id = R.string.button_clear),
            )
        }

        FilterChip(
            selected = isPicky,
            onClick = onPickyClick,
            label = { Text(text = stringResource(id = R.string.label_picky)) },
        )

        FilterChip(
            selected = selectedLanguage != null,
            onClick = onLanguageClick,
            label = {
                Icon(Icons.Default.ArrowDropDown, null)
                Text(text = selectedLanguage ?: stringResource(id = R.string.label_language_auto))
            },
        )
    }
}
