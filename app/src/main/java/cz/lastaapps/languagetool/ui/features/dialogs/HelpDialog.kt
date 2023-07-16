package cz.lastaapps.languagetool.ui.features.dialogs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cz.lastaapps.languagetool.ui.theme.PaddingTokens
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun HelpDialogContent(
    modifier: Modifier = Modifier,
) {
    val items = getStrings()
    LazyColumn(
        modifier = modifier,
    ) {
        items(items) { (title, description) ->
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(Modifier.height(PaddingTokens.MidSmall))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(PaddingTokens.MidLarge))
        }
    }
}

@Composable
private fun getStrings(): ImmutableList<Pair<String, String>> {
    return persistentListOf(
        "Disclaimer" to "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vivamus luctus egestas leo. Nulla pulvinar eleifend sem. Nullam at arcu a est sollicitudin euismod. Phasellus enim erat, vestibulum vel, aliquam a, posuere eu, velit. Aenean vel massa quis mauris vehicula lacinia. Integer in sapien. Maecenas sollicitudin. Fusce aliquam vestibulum ipsum. Aenean vel massa quis mauris vehicula lacinia.\n\nMauris dictum facilisis augue. Et harum quidem rerum facilis est et expedita distinctio. Duis sapien nunc, commodo et, interdum suscipit, sollicitudin et, dolor. Integer vulputate sem a nibh rutrum consequat. Morbi scelerisque luctus velit. Phasellus rhoncus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Ut tempus purus at lorem. Curabitur bibendum justo non orci. Integer tempor. Fusce wisi. Etiam ligula pede, sagittis quis, interdum ultricies, scelerisque eu. Maecenas aliquet accumsan leo. Aliquam erat volutpat. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Suspendisse sagittis ultrices augue. Maecenas fermentum, sem in pharetra pellentesque, velit turpis volutpat ante, in pharetra metus odio a lectus. Nulla turpis magna, cursus sit amet, suscipit a, interdum id, felis. Duis viverra diam non justo. Pellentesque sapien.",
        "Premium" to "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vivamus luctus egestas leo. Nulla pulvinar eleifend sem. Nullam at arcu a est sollicitudin euismod. Phasellus enim erat, vestibulum vel, aliquam a, posuere eu, velit. Aenean vel massa quis mauris vehicula lacinia. Integer in sapien. Maecenas sollicitudin. Fusce aliquam vestibulum ipsum. Aenean vel massa quis mauris vehicula lacinia.\n\nMauris dictum facilisis augue. Et harum quidem rerum facilis est et expedita distinctio. Duis sapien nunc, commodo et, interdum suscipit, sollicitudin et, dolor. Integer vulputate sem a nibh rutrum consequat. Morbi scelerisque luctus velit. Phasellus rhoncus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Ut tempus purus at lorem. Curabitur bibendum justo non orci. Integer tempor. Fusce wisi. Etiam ligula pede, sagittis quis, interdum ultricies, scelerisque eu. Maecenas aliquet accumsan leo. Aliquam erat volutpat. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Suspendisse sagittis ultrices augue. Maecenas fermentum, sem in pharetra pellentesque, velit turpis volutpat ante, in pharetra metus odio a lectus. Nulla turpis magna, cursus sit amet, suscipit a, interdum id, felis. Duis viverra diam non justo. Pellentesque sapien.",
        "Rate limit" to "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vivamus luctus egestas leo. Nulla pulvinar eleifend sem. Nullam at arcu a est sollicitudin euismod. Phasellus enim erat, vestibulum vel, aliquam a, posuere eu, velit. Aenean vel massa quis mauris vehicula lacinia. Integer in sapien. Maecenas sollicitudin. Fusce aliquam vestibulum ipsum. Aenean vel massa quis mauris vehicula lacinia.\n\nMauris dictum facilisis augue. Et harum quidem rerum facilis est et expedita distinctio. Duis sapien nunc, commodo et, interdum suscipit, sollicitudin et, dolor. Integer vulputate sem a nibh rutrum consequat. Morbi scelerisque luctus velit. Phasellus rhoncus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Ut tempus purus at lorem. Curabitur bibendum justo non orci. Integer tempor. Fusce wisi. Etiam ligula pede, sagittis quis, interdum ultricies, scelerisque eu. Maecenas aliquet accumsan leo. Aliquam erat volutpat. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Suspendisse sagittis ultrices augue. Maecenas fermentum, sem in pharetra pellentesque, velit turpis volutpat ante, in pharetra metus odio a lectus. Nulla turpis magna, cursus sit amet, suscipit a, interdum id, felis. Duis viverra diam non justo. Pellentesque sapien.",
        "Picky" to "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vivamus luctus egestas leo. Nulla pulvinar eleifend sem. Nullam at arcu a est sollicitudin euismod. Phasellus enim erat, vestibulum vel, aliquam a, posuere eu, velit. Aenean vel massa quis mauris vehicula lacinia. Integer in sapien. Maecenas sollicitudin. Fusce aliquam vestibulum ipsum. Aenean vel massa quis mauris vehicula lacinia.\n\nMauris dictum facilisis augue. Et harum quidem rerum facilis est et expedita distinctio. Duis sapien nunc, commodo et, interdum suscipit, sollicitudin et, dolor. Integer vulputate sem a nibh rutrum consequat. Morbi scelerisque luctus velit. Phasellus rhoncus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Ut tempus purus at lorem. Curabitur bibendum justo non orci. Integer tempor. Fusce wisi. Etiam ligula pede, sagittis quis, interdum ultricies, scelerisque eu. Maecenas aliquet accumsan leo. Aliquam erat volutpat. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Suspendisse sagittis ultrices augue. Maecenas fermentum, sem in pharetra pellentesque, velit turpis volutpat ante, in pharetra metus odio a lectus. Nulla turpis magna, cursus sit amet, suscipit a, interdum id, felis. Duis viverra diam non justo. Pellentesque sapien.",
        "Language" to "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vivamus luctus egestas leo. Nulla pulvinar eleifend sem. Nullam at arcu a est sollicitudin euismod. Phasellus enim erat, vestibulum vel, aliquam a, posuere eu, velit. Aenean vel massa quis mauris vehicula lacinia. Integer in sapien. Maecenas sollicitudin. Fusce aliquam vestibulum ipsum. Aenean vel massa quis mauris vehicula lacinia.\n\nMauris dictum facilisis augue. Et harum quidem rerum facilis est et expedita distinctio. Duis sapien nunc, commodo et, interdum suscipit, sollicitudin et, dolor. Integer vulputate sem a nibh rutrum consequat. Morbi scelerisque luctus velit. Phasellus rhoncus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Ut tempus purus at lorem. Curabitur bibendum justo non orci. Integer tempor. Fusce wisi. Etiam ligula pede, sagittis quis, interdum ultricies, scelerisque eu. Maecenas aliquet accumsan leo. Aliquam erat volutpat. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Suspendisse sagittis ultrices augue. Maecenas fermentum, sem in pharetra pellentesque, velit turpis volutpat ante, in pharetra metus odio a lectus. Nulla turpis magna, cursus sit amet, suscipit a, interdum id, felis. Duis viverra diam non justo. Pellentesque sapien.",
    )
}
