package cz.lastaapps.languagetool.ui.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.lastaapps.languagetool.ui.components.IconButtonTooltip
import cz.lastaapps.languagetool.ui.home.model.CheckProgress

@Composable
fun HomeBottomAppBar(
    progress: CheckProgress,
    onCheck: () -> Unit,
    onSystemSpellCheck: () -> Unit,
    onLogin: () -> Unit,
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
                onClick = onLogin,
                icon = Icons.Outlined.Person,
                contentDescription = "Login",
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
                        is CheckProgress.RateLimit -> (progress.remaining.inWholeSeconds + 1).toString()
                        CheckProgress.Ready -> "Ready"
                    },
                    modifier = Modifier.animateContentSize(),
                )
            },
            icon = {
                when (progress) {
                    CheckProgress.Processing ->
                        CircularProgressIndicator()

                    is CheckProgress.RateLimit ->
                        CircularProgressIndicator(
                            1f * progress.remaining.inWholeSeconds / progress.total.inWholeSeconds
                        )

                    CheckProgress.Ready ->
                        Icon(
                            Icons.Default.Done,
                            contentDescription = "Validate input",
                        )
                }
            },
            onClick = onCheck,
        )
    }
}