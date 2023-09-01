package cz.lastaapps.languagetool.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import cz.lastaapps.languagetool.ui.theme.generated.DarkColors
import cz.lastaapps.languagetool.ui.theme.generated.LightColors

@Composable
fun AppTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    useDynamic: Boolean = false,
    colorSystemBars: Boolean = false,
    content: @Composable () -> Unit,
) {
    val isLightMode = !isDarkMode

    val colorScheme = if (useDynamic) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (isLightMode) {
                dynamicLightColorScheme(LocalContext.current)
            } else {
                dynamicDarkColorScheme(LocalContext.current)
            }
        } else {
            error("You fucked up")
        }
    } else {
        if (isLightMode) {
            LightColors
        } else {
            DarkColors
        }
    }.animated()

    if (colorSystemBars) {
        val systemUiController = rememberSystemUiController()

        SideEffect {
            systemUiController.setStatusBarColor(
                color = colorScheme.background,
                darkIcons = isLightMode,
            )
            systemUiController.setNavigationBarColor(
                color = colorScheme.surfaceVariant,
                darkIcons = isLightMode,
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
            content()
        }
    }
}

@Suppress("AnimateAsStateLabel")
@Composable
private fun ColorScheme.animated(): ColorScheme {
    return ColorScheme(
        background = animateColorAsState(background).value,
        error = animateColorAsState(error).value,
        errorContainer = animateColorAsState(errorContainer).value,
        inverseOnSurface = animateColorAsState(inverseOnSurface).value,
        inversePrimary = animateColorAsState(inversePrimary).value,
        inverseSurface = animateColorAsState(inverseSurface).value,
        onBackground = animateColorAsState(onBackground).value,
        onError = animateColorAsState(onError).value,
        onErrorContainer = animateColorAsState(onErrorContainer).value,
        onPrimary = animateColorAsState(onPrimary).value,
        onPrimaryContainer = animateColorAsState(onPrimaryContainer).value,
        onSecondary = animateColorAsState(onSecondary).value,
        onSecondaryContainer = animateColorAsState(onSecondaryContainer).value,
        onSurface = animateColorAsState(onSurface).value,
        onSurfaceVariant = animateColorAsState(onSurfaceVariant).value,
        onTertiary = animateColorAsState(onTertiary).value,
        onTertiaryContainer = animateColorAsState(onTertiaryContainer).value,
        outline = animateColorAsState(outline).value,
        primary = animateColorAsState(primary).value,
        primaryContainer = animateColorAsState(primaryContainer).value,
        secondary = animateColorAsState(secondary).value,
        secondaryContainer = animateColorAsState(secondaryContainer).value,
        surface = animateColorAsState(surface).value,
        surfaceVariant = animateColorAsState(surfaceVariant).value,
        surfaceTint = animateColorAsState(surfaceTint).value,
        tertiary = animateColorAsState(tertiary).value,
        tertiaryContainer = animateColorAsState(tertiaryContainer).value,
        outlineVariant = animateColorAsState(outlineVariant).value,
        scrim = animateColorAsState(scrim).value,
    )
}
