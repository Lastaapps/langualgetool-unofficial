package cz.lastaapps.languagetool.ui.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.lastaapps.languagetool.ui.components.IconButtonTooltip

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActionChips(
    isPicky: Boolean,
    onPickyClick: () -> Unit,
    selectedLanguage: String?,
    onLanguageClick: () -> Unit,
    hasPremium: Boolean,
    onPremiumClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (!hasPremium) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                TextButton(
                    onClick = onPremiumClick,
                ) {
                    Text(
                        "Premium",
                        softWrap = false,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                    )
                }
            }
        }

        IconButtonTooltip(
            onClick = onHelpClick,
            icon = Icons.Default.HelpOutline,
            contentDescription = "Help",
        )

        FilterChip(
            selected = isPicky,
            onClick = onPickyClick,
            label = { Text(text = "Picky") },
        )

        FilterChip(
            selected = selectedLanguage != null,
            onClick = onLanguageClick,
            label = {
                Icon(Icons.Default.ArrowDropDown, null)
                Text(text = selectedLanguage ?: "Auto")
            },
        )
    }
}
