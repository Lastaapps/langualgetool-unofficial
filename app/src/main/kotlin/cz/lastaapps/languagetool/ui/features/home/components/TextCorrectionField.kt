package cz.lastaapps.languagetool.ui.features.home.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cz.lastaapps.languagetool.R
import cz.lastaapps.languagetool.domain.model.CheckProgress
import cz.lastaapps.languagetool.domain.model.MatchedText
import cz.lastaapps.languagetool.ui.features.home.logic.toAnnotatedString
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
internal fun TextCorrectionField(
    progress: CheckProgress,
    matched: MatchedText,
    onText: (String) -> Unit,
    onCursor: (Int) -> Unit,
    charLimit: Int,
    modifier: Modifier = Modifier,
) {
    val value = matched.toAnnotatedString()

    // Stolen from the BasicTextField
    // Holds the latest internal TextFieldValue state. We need to keep it to have the correct value
    // of the composition.
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(value)) }
    // Holds the latest TextFieldValue that BasicTextField was recomposed with. We couldn't simply
    // pass `TextFieldValue(text = value)` to the CoreTextField because we need to preserve the
    // composition.
    val textFieldValue = textFieldValueState.copy(value)

    SideEffect {
        if (textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }
    // Last String value that either text field was recomposed with or updated in the onValueChange
    // callback. We keep track of it to prevent calling onValueChange(String) for same String when
    // CoreTextField's onValueChange is called multiple times without recomposition in between.
    var lastTextValue by remember(value) { mutableStateOf(value) }

    // support text was not working properly
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PaddingTokens.Smaller),
    ) {
        TextCorrectionField(
            text = textFieldValue,
            onText = { newTextFieldValueState ->
                textFieldValueState = newTextFieldValueState

                val stringChangedSinceLastInvocation =
                    lastTextValue != newTextFieldValueState.annotatedString
                lastTextValue = newTextFieldValueState.annotatedString

                if (stringChangedSinceLastInvocation) {
                    onText(newTextFieldValueState.annotatedString.text)
                }
                onCursor(newTextFieldValueState.selection.start)
            },
            enabled = progress != CheckProgress.Processing,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        SupportingText(
            text = textFieldValue,
            charLimit = charLimit,
            errors = matched.visibleErrors.size,
            isClean = !matched.isTouched,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
internal fun TextCorrectionField(
    text: TextFieldValue,
    onText: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val alpha by rememberInfiniteTransition(label = "TextField alpha")
        .animateFloat(
            initialValue = .6f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "TextField alpha",
        )

    TextField(
        value = text,
        onValueChange = {
            onText(it)
        },
        enabled = enabled,
        placeholder = {
            Text(text = stringResource(id = R.string.placeholder_enter_text))
        },
        supportingText = {},
        shape = MaterialTheme.shapes.extraLarge,
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.None,
        ),
        modifier = modifier
            .alpha(
                if (enabled) {
                    1f
                } else {
                    alpha
                },
            ),
    )
}

@Composable
private fun SupportingText(
    text: TextFieldValue,
    charLimit: Int,
    errors: Int,
    isClean: Boolean,
    modifier: Modifier = Modifier,
) {
    val chars = text.text.length

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PaddingTokens.Small),
            modifier = Modifier.weight(1f),
        ) {
            when {
                errors > 0 -> Icons.Default.Error to MaterialTheme.colorScheme.error
                !isClean -> Icons.Default.Warning to MaterialTheme.colorScheme.primary
                // else -> Icons.Default.Check to LocalContentColor.current
                else -> Icons.Default.Check to MaterialTheme.colorScheme.secondary
            }.let {
                Crossfade(targetState = it, label = "validity_icon") { (icon, tint) ->
                    Icon(icon, null, tint = tint)
                }
            }

            Crossfade(
                targetState = stringResource(
                    id = R.string.label_validation_summary,
                    errors,
                    if (isClean) {
                        R.string.label_validated
                    } else {
                        R.string.label_dirty
                    }.let { stringResource(id = it) },
                ),
                label = "validity_text",
            ) { text ->
                Text(text = text)
            }
        }

        Text(
            "$chars/$charLimit",
            color = if (chars > charLimit) {
                MaterialTheme.colorScheme.error
            } else {
                Color.Unspecified
            },
        )
    }
}
