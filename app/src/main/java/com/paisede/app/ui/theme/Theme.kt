package com.paisede.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = LightColors.Primary,
    onPrimary = LightColors.OnPrimary,
    primaryContainer = LightColors.PrimaryContainer,
    onPrimaryContainer = LightColors.OnPrimaryContainer,
    secondary = LightColors.Secondary,
    onSecondary = LightColors.OnSecondary,
    secondaryContainer = LightColors.SecondaryContainer,
    onSecondaryContainer = LightColors.OnSecondaryContainer,
    tertiary = LightColors.Tertiary,
    onTertiary = LightColors.OnTertiary,
    tertiaryContainer = LightColors.TertiaryContainer,
    onTertiaryContainer = LightColors.OnTertiaryContainer,
    error = LightColors.Negative,
    onError = LightColors.OnNegative,
    errorContainer = LightColors.NegativeContainer,
    onErrorContainer = LightColors.OnNegativeContainer,
    background = LightColors.Background,
    onBackground = LightColors.TextPrimary,
    surface = LightColors.Surface,
    onSurface = LightColors.TextPrimary,
    surfaceVariant = LightColors.SurfaceHover,
    onSurfaceVariant = LightColors.TextSecondary,
    outline = LightColors.Border,
    outlineVariant = LightColors.BorderSubtle,
    scrim = LightColors.OverlayMedium,
    surfaceTint = LightColors.Primary,
    inverseSurface = DarkColors.Surface,
    inverseOnSurface = DarkColors.TextPrimary,
    inversePrimary = DarkColors.Primary
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkColors.Primary,
    onPrimary = DarkColors.OnPrimary,
    primaryContainer = DarkColors.PrimaryContainer,
    onPrimaryContainer = DarkColors.OnPrimaryContainer,
    secondary = DarkColors.Secondary,
    onSecondary = DarkColors.OnSecondary,
    secondaryContainer = DarkColors.SecondaryContainer,
    onSecondaryContainer = DarkColors.OnSecondaryContainer,
    tertiary = DarkColors.Tertiary,
    onTertiary = DarkColors.OnTertiary,
    tertiaryContainer = DarkColors.TertiaryContainer,
    onTertiaryContainer = DarkColors.OnTertiaryContainer,
    error = DarkColors.Negative,
    onError = DarkColors.OnNegative,
    errorContainer = DarkColors.NegativeContainer,
    onErrorContainer = DarkColors.OnNegativeContainer,
    background = DarkColors.Background,
    onBackground = DarkColors.TextPrimary,
    surface = DarkColors.Surface,
    onSurface = DarkColors.TextPrimary,
    surfaceVariant = DarkColors.SurfaceHover,
    onSurfaceVariant = DarkColors.TextSecondary,
    outline = DarkColors.Border,
    outlineVariant = DarkColors.BorderSubtle,
    scrim = DarkColors.OverlayMedium,
    surfaceTint = DarkColors.Primary,
    inverseSurface = LightColors.Surface,
    inverseOnSurface = LightColors.TextPrimary,
    inversePrimary = LightColors.Primary
)

@Composable
fun SplitzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun PaiseDeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    SplitzTheme(darkTheme = darkTheme, content = content)
}

val ColorScheme.isDark: Boolean
    get() = this.surface == DarkColors.Surface

// Semantic color accessors for use in components
object SplitzColors {
    @Composable
    fun isDark(): Boolean = MaterialTheme.colorScheme.surface == DarkColors.Surface

    @Composable
    fun background(): Color = MaterialTheme.colorScheme.background

    @Composable
    fun backgroundElevated(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.BackgroundElevated else LightColors.BackgroundElevated

    @Composable
    fun surface(): Color = MaterialTheme.colorScheme.surface

    @Composable
    fun surfaceVariant(): Color = MaterialTheme.colorScheme.surfaceVariant

    @Composable
    fun surfaceElevated(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.SurfaceElevated else LightColors.SurfaceElevated

    @Composable
    fun surfaceGlass(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.SurfaceGlass else LightColors.SurfaceGlass

    @Composable
    fun surfaceGlassStrong(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.SurfaceGlassStrong else LightColors.SurfaceGlassStrong

    @Composable
    fun border(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.Border else LightColors.Border

    @Composable
    fun borderSubtle(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.BorderSubtle else LightColors.BorderSubtle

    @Composable
    fun borderGlass(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.BorderGlass else LightColors.BorderGlass

    @Composable
    fun primary(): Color = MaterialTheme.colorScheme.primary

    @Composable
    fun primaryBright(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.PrimaryBright else LightColors.PrimaryBright

    @Composable
    fun primaryDark(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.PrimaryDark else LightColors.PrimaryDark

    @Composable
    fun positive(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.Positive else LightColors.Positive

    @Composable
    fun positiveContainer(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.PositiveContainer else LightColors.PositiveContainer

    @Composable
    fun negative(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.Negative else LightColors.Negative

    @Composable
    fun negativeContainer(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.NegativeContainer else LightColors.NegativeContainer

    @Composable
    fun warning(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.Warning else LightColors.Warning

    @Composable
    fun warningContainer(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.WarningContainer else LightColors.WarningContainer

    @Composable
    fun info(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.Info else LightColors.Info

    @Composable
    fun textPrimary(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.TextPrimary else LightColors.TextPrimary

    @Composable
    fun textSecondary(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.TextSecondary else LightColors.TextSecondary

    @Composable
    fun onPrimary(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.OnPrimary else LightColors.OnPrimary

    @Composable
    fun onPositive(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.OnPositive else LightColors.OnPositive

    @Composable
    fun onNegative(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.OnNegative else LightColors.OnNegative

    @Composable
    fun onWarning(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.OnWarning else LightColors.OnWarning

    @Composable
    fun textMuted(): Color = if (MaterialTheme.colorScheme.isDark) DarkColors.TextMuted else LightColors.TextMuted
}