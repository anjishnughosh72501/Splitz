package com.paisede.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import com.paisede.app.data.preferences.AppCurrency
import com.paisede.app.data.preferences.SplitzVisualTheme

val LocalAppCurrency = staticCompositionLocalOf { AppCurrency.DEFAULT }
val LocalSplitzVisualTheme = staticCompositionLocalOf { SplitzVisualTheme.GRAPHITE }
val LocalSplitzPalette = staticCompositionLocalOf { ThemePalettes.Graphite }

fun createColorSchemeForTheme(
    palette: SplitzPalette,
    darkTheme: Boolean
): ColorScheme {
    return if (darkTheme) {
        darkColorScheme(
            primary = palette.brandPrimary,
            onPrimary = palette.onBrandPrimary,
            primaryContainer = palette.brandSoft,
            onPrimaryContainer = palette.textPrimary,
            secondary = palette.brandAccent,
            onSecondary = palette.onBrandPrimary,
            secondaryContainer = palette.surfaceElevated,
            onSecondaryContainer = palette.textPrimary,
            tertiary = palette.brandAccent,
            onTertiary = palette.onBrandPrimary,
            tertiaryContainer = palette.brandSoft,
            onTertiaryContainer = palette.textPrimary,
            error = palette.negative,
            onError = palette.onNegative,
            errorContainer = palette.negativeContainer,
            onErrorContainer = palette.negativeStrong,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.surface,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.surfaceElevated,
            onSurfaceVariant = palette.textSecondary,
            outline = palette.border,
            outlineVariant = palette.borderSubtle,
            scrim = palette.surfaceScrim,
            surfaceTint = palette.brandPrimary
        )
    } else {
        // Coordinated light appearance with identical semantic structure
        lightColorScheme(
            primary = if (palette == ThemePalettes.Graphite) Color(0xFF1E212B) else palette.brandPrimary,
            onPrimary = Color.White,
            primaryContainer = if (palette == ThemePalettes.Graphite) Color(0xFFE2E4E9) else palette.brandSoft,
            onPrimaryContainer = Color(0xFF0F172A),
            secondary = palette.brandAccent,
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFF1F5F9),
            onSecondaryContainer = Color(0xFF0F172A),
            tertiary = palette.brandAccent,
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFEEF2FF),
            onTertiaryContainer = Color(0xFF1E1B4B),
            error = palette.negative,
            onError = Color.White,
            errorContainer = Color(0xFFFEE2E2),
            onErrorContainer = Color(0xFF7F1D1D),
            background = Color(0xFFF8FAFC),
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFF1F5F9),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFCBD5E1),
            outlineVariant = Color(0xFFE2E8F0),
            scrim = Color(0xFF000000).copy(alpha = 0.32f),
            surfaceTint = palette.brandPrimary
        )
    }
}

@Composable
fun SplitzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    visualTheme: SplitzVisualTheme = SplitzVisualTheme.GRAPHITE,
    fontScale: Float = 1.0f,
    currency: AppCurrency = AppCurrency.DEFAULT,
    content: @Composable () -> Unit
) {
    val palette = ThemePalettes.forTheme(visualTheme)
    val colorScheme = createColorSchemeForTheme(palette, darkTheme)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                // Ensure status bar seamlessly matches app background
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    val currentDensity = LocalDensity.current
    val scaledDensity = Density(
        density = currentDensity.density,
        fontScale = currentDensity.fontScale * fontScale
    )

    CompositionLocalProvider(
        LocalDensity provides scaledDensity,
        LocalAppCurrency provides currency,
        LocalSplitzVisualTheme provides visualTheme,
        LocalSplitzPalette provides palette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun PaiseDeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    visualTheme: SplitzVisualTheme = SplitzVisualTheme.GRAPHITE,
    content: @Composable () -> Unit
) {
    SplitzTheme(darkTheme = darkTheme, visualTheme = visualTheme, content = content)
}

val ColorScheme.isDark: Boolean
    get() = this.background == DarkColors.Background || this.background == ThemePalettes.Graphite.background || this.background == ThemePalettes.ElectricBlue.background || this.background == ThemePalettes.Ultraviolet.background

// Semantic color accessors for use across UI components
object SplitzColors {
    @Composable
    fun isDark(): Boolean = MaterialTheme.colorScheme.isDark

    @Composable
    fun background(): Color = LocalSplitzPalette.current.background

    @Composable
    fun backgroundElevated(): Color = LocalSplitzPalette.current.backgroundElevated

    @Composable
    fun backgroundSecondary(): Color = LocalSplitzPalette.current.backgroundSecondary

    @Composable
    fun surface(): Color = LocalSplitzPalette.current.surface

    @Composable
    fun surfaceVariant(): Color = MaterialTheme.colorScheme.surfaceVariant

    @Composable
    fun surfaceElevated(): Color = LocalSplitzPalette.current.surfaceElevated

    @Composable
    fun surfaceHover(): Color = LocalSplitzPalette.current.surfaceHover

    @Composable
    fun surfacePressed(): Color = LocalSplitzPalette.current.surfacePressed

    @Composable
    fun surfaceGlass(): Color = LocalSplitzPalette.current.surfaceGlass

    @Composable
    fun surfaceGlassStrong(): Color = LocalSplitzPalette.current.surfaceGlassStrong

    @Composable
    fun border(): Color = LocalSplitzPalette.current.border

    @Composable
    fun borderSubtle(): Color = LocalSplitzPalette.current.borderSubtle

    @Composable
    fun borderStrong(): Color = LocalSplitzPalette.current.borderStrong

    @Composable
    fun borderGlass(): Color = LocalSplitzPalette.current.borderGlass

    @Composable
    fun borderFocus(): Color = LocalSplitzPalette.current.borderFocus

    @Composable
    fun primary(): Color = LocalSplitzPalette.current.brandPrimary

    @Composable
    fun primaryBright(): Color = LocalSplitzPalette.current.brandAccent

    @Composable
    fun primaryDark(): Color = LocalSplitzPalette.current.brandSoft

    @Composable
    fun positive(): Color = LocalSplitzPalette.current.positive

    @Composable
    fun positiveContainer(): Color = LocalSplitzPalette.current.positiveContainer

    @Composable
    fun positiveStrong(): Color = LocalSplitzPalette.current.positiveStrong

    @Composable
    fun negative(): Color = LocalSplitzPalette.current.negative

    @Composable
    fun negativeContainer(): Color = LocalSplitzPalette.current.negativeContainer

    @Composable
    fun negativeStrong(): Color = LocalSplitzPalette.current.negativeStrong

    @Composable
    fun warning(): Color = LocalSplitzPalette.current.warning

    @Composable
    fun warningContainer(): Color = LocalSplitzPalette.current.warningContainer

    @Composable
    fun warningStrong(): Color = LocalSplitzPalette.current.warningStrong

    @Composable
    fun info(): Color = LocalSplitzPalette.current.info

    @Composable
    fun infoContainer(): Color = LocalSplitzPalette.current.infoContainer

    @Composable
    fun textPrimary(): Color = LocalSplitzPalette.current.textPrimary

    @Composable
    fun textSecondary(): Color = LocalSplitzPalette.current.textSecondary

    @Composable
    fun textMuted(): Color = LocalSplitzPalette.current.textMuted

    @Composable
    fun textInverse(): Color = LocalSplitzPalette.current.textInverse

    @Composable
    fun textDisabled(): Color = LocalSplitzPalette.current.textDisabled

    @Composable
    fun onPrimary(): Color = LocalSplitzPalette.current.onBrandPrimary

    @Composable
    fun onPositive(): Color = LocalSplitzPalette.current.onPositive

    @Composable
    fun onNegative(): Color = LocalSplitzPalette.current.onNegative

    @Composable
    fun onWarning(): Color = LocalSplitzPalette.current.onWarning
}