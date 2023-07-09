package cz.lastaapps.languagetool.ui.home.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cz.lastaapps.languagetool.data.model.MatchedText
import cz.lastaapps.languagetool.ui.home.logic.toAnnotatedString
import cz.lastaapps.languagetool.ui.theme.PaddingTokens

@Composable
internal fun TextCorrectionField(
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
        charLimit = charLimit,
        errors = matched.errors.size,
        isClean = !matched.isTouched,
        modifier = modifier,
    )
}

@Composable
internal fun TextCorrectionField(
    text: TextFieldValue,
    onText: (TextFieldValue) -> Unit,
    charLimit: Int,
    errors: Int,
    isClean: Boolean,
    modifier: Modifier = Modifier,
) {
    val chars = text.text.length

    TextField(
        value = text,
        onValueChange = {
            onText(it)
        },
        placeholder = {
            Text(text = "Enter the text to spell-check...")
        },
        supportingText = {
            Row(
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
                    buildString {
                        append(errors)
                        append(" errors, ")
                        if (isClean) {
                            append("Validated")
                        } else {
                            append("Dirty")
                        }
                    }.let {
                        Crossfade(targetState = it, label = "validity_text") { text ->
                            Text(text = text)
                        }
                    }
                }

                Text(
                    "$chars/$charLimit",
                )
            }
        },
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier,
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
        )
    )
}
