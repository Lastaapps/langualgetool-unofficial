package cz.lastaapps.languagetool.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.get
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

/**
 * Supports content resizing
 */
@Composable
fun SaneDialog(
    onDismiss: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties,
    ) {
        SaneDialogLogic(
            onDismiss = onDismiss,
            properties = properties,
            content = content,
        )
    }
}

@Composable
private fun SaneDialogLogic(
    onDismiss: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    val outerInteractionSrc = remember { MutableInteractionSource() }
    val innerInteractionSrc = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(outerInteractionSrc, null, onClick = {
                if (properties.dismissOnClickOutside) {
                    onDismiss()
                }
            }),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.clickable(innerInteractionSrc, null) { }) {
            SaneDialogDecoration(content = content)
        }
    }
}

@Composable
fun SaneDialogDecoration(
    content: @Composable () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(
            modifier = Modifier
                .padding(PaddingTokens.More.Dialog)
                .sizeIn(minWidth = 280.dp, maxWidth = 560.dp),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

fun NavGraphBuilder.saneDialog(
    route: String,
    onDismiss: () -> Unit,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    addDestination(
        DialogNavigator.Destination(
            provider[DialogNavigator::class],
            dialogProperties,
            content = { entry ->
                SaneDialogLogic(onDismiss, properties = dialogProperties) {
                    content(entry)
                }
            },
        ).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}
