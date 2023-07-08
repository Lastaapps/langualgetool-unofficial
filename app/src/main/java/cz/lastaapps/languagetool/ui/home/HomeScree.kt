package cz.lastaapps.languagetool.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PlainTooltipState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.lastaapps.languagetool.ui.components.IconButtonTooltip
import cz.lastaapps.languagetool.ui.theme.PaddingTokens
import cz.lastaapps.languagetool.ui.util.PreviewWrapper

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    text: TextFieldValue,
    onText: (TextFieldValue) -> Unit,
    onCheck: () -> Unit,
    onSystemSpellCheck: () -> Unit,
    onLogin: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
    onChooseLanguage: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        modifier = modifier,
        snackbarHost = {},
        bottomBar = {
            LangBottomAppBar(
                onCheck = onCheck,
                onLogin = onLogin,
                onSettings = onSettings,
                onAbout = onAbout,
                onSystemSpellCheck = onSystemSpellCheck,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(PaddingTokens.More.Screen)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingTokens.Medium),
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "LanguageTool",
                    style = MaterialTheme.typography.displaySmall,
                )

                Badge {
                    Text(
                        text = "Unofficial",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            TextField(
                value = text,
                onValueChange = onText,
                supportingText = {
                    Row(
//                        horizontalAlignment = Alignment.End,
//                        modifier = Modifier.fillMaxWidth(),
                    ) {

                        Text(
                            modifier = Modifier.weight(1f),
                            text = "20 errors\nResults incomplete!",
                        )
                        Text(
                            "1000/20000",
                        )
                    }
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardActions = KeyboardActions { onCheck() },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    TextButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Text(
                            "Premium",
                            softWrap = false,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                        )
                    }
                }

                IconButtonTooltip(
                    onClick = {},
                    icon = Icons.Default.HelpOutline,
                    contentDescription = "Help",
                )

                FilterChip(selected = true, onClick = {}, label = { Text(text = "Picky") })

                FilterChip(selected = true, onClick = {}, label = {
                    Icon(Icons.Default.ArrowDropDown, null)
                    Text(text = "Auto")
                })
            }
        }
    }
}

@Composable
fun LangBottomAppBar(
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
                contentDescription = "",
            )
            IconButtonTooltip(
                onClick = onLogin,
                icon = Icons.Outlined.Person,
                contentDescription = "",
            )
            IconButtonTooltip(
                onClick = onSettings,
                icon = Icons.Outlined.Settings,
                contentDescription = "",
            )
            IconButtonTooltip(
                onClick = onAbout,
                icon = Icons.Outlined.Info,
                contentDescription = "",
            )
        }

        ExtendedFloatingActionButton(
            text = { Text("Checking") },
            icon = {
                Icon(
                    Icons.Default.Done,
                    contentDescription = "Validate input",
                )
            },
            onClick = onCheck,
        )
//        SmallFloatingActionButton(onClick = onCheck) {
//            Row {
//                Icon(
//                    Icons.Default.Done,
//                    contentDescription = "Validate input",
//                )
//                Text("Checking")
//            }
//        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() = PreviewWrapper(screenPadding = 0.dp) {
    HomeScreen(
        text = """
       Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Integer lacinia. Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur? In sem justo, commodo ut, suscipit at, pharetra vitae, orci. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Maecenas sollicitudin. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus et lorem id felis nonummy placerat. Aliquam ante. Mauris dictum facilisis augue. Mauris metus. Etiam quis quam. Vivamus ac leo pretium faucibus. Etiam bibendum elit eget erat. Donec quis nibh at felis congue commodo. Mauris dolor felis, sagittis at, luctus sed, aliquam non, tellus.
    """.trimIndent().let(::TextFieldValue),
        onText = {},
        onCheck = {},
        onLogin = {},
        onAbout = {},
        onSettings = {},
        onChooseLanguage = {},
        onSystemSpellCheck = {},
    )
}
