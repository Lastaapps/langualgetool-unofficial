package cz.lastaapps.languagetool.ui.features.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.domain.model.CheckProgress
import cz.lastaapps.languagetool.ui.components.IconButtonTooltip
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
fun HomeBottomAppBar(
    progress: CheckProgress,
    onCheck: () -> Unit,
    onSystemSpellCheck: () -> Unit,
    onHelpClick: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButtonTooltip(
                onClick = onSystemSpellCheck,
                icon = Icons.Outlined.Keyboard,
                contentDescription = stringResource(id = R.string.button_spell_check),
            )
            IconButtonTooltip(
                onClick = onHelpClick,
                icon = Icons.Default.HelpOutline,
                contentDescription = stringResource(id = R.string.button_help),
            )
            IconButtonTooltip(
                onClick = onSettings,
                icon = Icons.Outlined.Settings,
                contentDescription = stringResource(id = R.string.button_settings),
            )
            IconButtonTooltip(
                onClick = onAbout,
                icon = Icons.Outlined.Info,
                contentDescription = stringResource(id = R.string.button_about),
            )
        }

        ExtendedFloatingActionButton(
            text = {
                Text(
                    when (progress) {
                        CheckProgress.Processing -> R.string.label_processing
                        is CheckProgress.RateLimit -> R.string.label_limited
                        CheckProgress.Ready -> R.string.label_ready
                    }.let { stringResource(id = it) },
                    modifier = Modifier.animateContentSize(),
                )
            },
            icon = {
                when (progress) {
                    CheckProgress.Processing ->
                        CircularProgressIndicator()

                    is CheckProgress.RateLimit -> {
                        var target by rememberSaveable { mutableStateOf(1f) }
                        val barProgress by animateFloatAsState(
                            targetValue = target,
                            label = "Rate Limit",
                            animationSpec = tween(
                                durationMillis = progress.duration.inWholeMilliseconds.toInt(),
                                easing = LinearEasing,
                            )
                        )
                        LaunchedEffect(Unit) { target = 0f }
                        CircularProgressIndicator(barProgress)
                    }

                    CheckProgress.Ready ->
                        Icon(
                            Icons.Default.Done,
                            contentDescription = null,
                        )
                }
            },
            onClick = onCheck,
            modifier = Modifier.padding(end = PaddingTokens.Small),
        )
    }
}