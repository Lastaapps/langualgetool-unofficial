package cz.lastaapps.languagetool.ui.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cz.lastaapps.languagetool.data.model.MatchedText
import cz.lastaapps.languagetool.ui.home.logic.toAnnotatedString

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun TextCorrectionField(
    matched: MatchedText,
    onText: (String) -> Unit,
    onCursor: (Int) -> Unit,
    onCheckRequest: () -> Boolean,
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
        onCheckRequest = onCheckRequest,
        charLimit = charLimit,
        errors = matched.errors.size,
        isClear = !matched.isTouched,
        modifier = modifier,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun TextCorrectionField(
    text: TextFieldValue,
    onText: (TextFieldValue) -> Unit,
    onCheckRequest: () -> Boolean,
    charLimit: Int,
    errors: Int,
    isClear: Boolean,
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
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
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = buildString {
                        append(errors)
                        append(" errors, ")
                        if (isClear) {
                            append("Validated")
                        } else {
                            append("Dirty")
                        }
                    },
                )
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
        keyboardActions = KeyboardActions {
            if (onCheckRequest()) {
                keyboardController?.hide()
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.None,
        )
    )
}
