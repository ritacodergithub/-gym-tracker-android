package com.example.e_commerce.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColors = darkColorScheme(
    primary = NeonIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2A1B5A),
    onPrimaryContainer = Color(0xFFE8E9F3),
    secondary = NeonCyan,
    onSecondary = Color(0xFF001C22),
    tertiary = NeonLime,
    onTertiary = Color(0xFF0C1600),
    background = BgDeep,
    onBackground = TextPrimary,
    surface = BgCard,
    onSurface = TextPrimary,
    surfaceVariant = BgElevated,
    onSurfaceVariant = TextSecondary,
    error = NeonRose,
    onError = Color.White,
    outline = StrokeEmphasis,
    outlineVariant = StrokeSubtle
)

private val LightColors = lightColorScheme(
    primary = NeonIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE4E0FF),
    secondary = NeonCyan,
    tertiary = NeonLime,
    background = LightBg,
    surface = LightSurface,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    error = NeonRose
)

@Composable
fun GymTheme(
    // Forced dark: the neon palette + glass cards + gradient backdrop are
    // designed to sit on a dark cosmos. The light scheme exists only as a
    // fallback — toggle this to respect the system setting if you ever want
    // a true light mode.
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    // Tint the system bars so the gradient page background reaches edge-to-edge.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}