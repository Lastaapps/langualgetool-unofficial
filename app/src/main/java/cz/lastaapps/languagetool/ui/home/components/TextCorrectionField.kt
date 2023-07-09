package cz.lastaapps.languagetool.ui.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextCorrectionField(
    text: TextFieldValue,
    onText: (TextFieldValue) -> Unit,
    onCheckRequest: () -> Boolean,
    charLimit: Int,
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) {
    val chars = text.text.length

    TextField(
        value = text,
        onValueChange = {

        },
        placeholder = {
            Text(text = "Enter the text to spell-check...")
        },
        supportingText = {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "20 errors\nResults incomplete!",
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
