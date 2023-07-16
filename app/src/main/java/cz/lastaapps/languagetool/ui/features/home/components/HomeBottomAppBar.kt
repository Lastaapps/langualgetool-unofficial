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
                contentDescription = "Spell Check",
            )
            IconButtonTooltip(
                onClick = onHelpClick,
                icon = Icons.Default.HelpOutline,
                contentDescription = "Help",
            )
            IconButtonTooltip(
                onClick = onSettings,
                icon = Icons.Outlined.Settings,
                contentDescription = "Settings",
            )
            IconButtonTooltip(
                onClick = onAbout,
                icon = Icons.Outlined.Info,
                contentDescription = "About",
            )
        }

        ExtendedFloatingActionButton(
            text = {
                Text(
                    when (progress) {
                        CheckProgress.Processing -> "Working"
                        is CheckProgress.RateLimit -> "Limited"
                        CheckProgress.Ready -> "Ready"
                    },
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
                            contentDescription = "Validate input",
                        )
                }
            },
            onClick = onCheck,
            modifier = Modifier.padding(end = PaddingTokens.Small),
        )
    }
}