package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GlassPrimary,
    primaryContainer = GlassPrimaryContainer,
    secondary = GlassSecondary,
    secondaryContainer = GlassSecondaryContainer,
    background = GlassBg,
    surface = GlassSurface,
    surfaceVariant = GlassSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    error = GlassError
)

// Maintained for consistency, formatted to perfectly match the sleek cinematic design
private val LightColorScheme = darkColorScheme(
    primary = GlassPrimary,
    primaryContainer = GlassPrimaryContainer,
    secondary = GlassSecondary,
    secondaryContainer = GlassSecondaryContainer,
    background = GlassBg,
    surface = GlassSurface,
    surfaceVariant = GlassSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    error = GlassError
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Always prioritize custom hand-crafted high fidelity themes
    content: @Composable () -> Unit,
) {
    // We enforce our unified cosmic frosted theme for a majestic, secure visual experience
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
