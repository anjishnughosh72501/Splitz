package com.paisede.app.ui.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
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
val LocalSplitzVisualTheme = staticCompositionLocalOf { SplitzVisualTheme.KISSATEN }
val LocalSplitzPalette = staticCompositionLocalOf { ThemePalettes.Light }

fun createColorSchemeForTheme(
    palette: SplitzPalette,
    darkTheme: Boolean
): ColorScheme {
    return if (darkTheme) {
        darkColorScheme(
            primary = palette.brandAccent,
            onPrimary = palette.onBrandPrimary,
            primaryContainer = palette.brandSoft,
            onPrimaryContainer = palette.textPrimary,
            secondary = palette.brandAccent,
            onSecondary = palette.onBrandPrimary,
            secondaryContainer = palette.surfaceElevated,
            onSecondaryContainer = palette.textPrimary,
            tertiary = palette.positive,
            onTertiary = palette.onPositive,
            tertiaryContainer = palette.positiveContainer,
            onTertiaryContainer = palette.positiveStrong,
            error = palette.negative,
            onError = palette.onNegative,
            errorContainer = palette.negativeContainer,
            onErrorContainer = palette.negativeStrong,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.surfaceElevated,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.surface,
            onSurfaceVariant = palette.textSecondary,
            outline = palette.border,
            outlineVariant = palette.borderSubtle,
            scrim = palette.surfaceScrim,
            surfaceTint = palette.brandAccent
        )
    } else {
        // Coordinated light appearance with identical semantic structure
        lightColorScheme(
            primary = palette.brandPrimary,
            onPrimary = palette.onBrandPrimary,
            primaryContainer = palette.brandSoft,
            onPrimaryContainer = palette.textPrimary,
            secondary = palette.brandAccent,
            onSecondary = Color.White,
            secondaryContainer = palette.backgroundElevated,
            onSecondaryContainer = palette.textPrimary,
            tertiary = palette.positive,
            onTertiary = Color.White,
            tertiaryContainer = palette.positiveContainer,
            onTertiaryContainer = palette.positiveStrong,
            error = palette.negative,
            onError = Color.White,
            errorContainer = palette.negativeContainer,
            onErrorContainer = palette.negativeStrong,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.surfaceElevated,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.backgroundElevated,
            onSurfaceVariant = palette.textSecondary,
            outline = palette.border,
            outlineVariant = palette.borderSubtle,
            scrim = palette.surfaceScrim,
            surfaceTint = palette.brandPrimary
        )
    }
}

@Composable
fun SplitzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontScale: Float = 1.0f,
    currency: AppCurrency = AppCurrency.DEFAULT,
    content: @Composable () -> Unit
) {
    val rawPalette = ThemePalettes.forMode(darkTheme)
    val animSpec = tween<Color>(250)
    val background by animateColorAsState(rawPalette.background, animSpec, label = "bg")
    val backgroundElevated by animateColorAsState(rawPalette.backgroundElevated, animSpec, label = "bgElev")
    val surface by animateColorAsState(rawPalette.surface, animSpec, label = "surface")
    val surfaceElevated by animateColorAsState(rawPalette.surfaceElevated, animSpec, label = "surfElev")
    val brandPrimary by animateColorAsState(rawPalette.brandPrimary, animSpec, label = "brandPri")
    val brandAccent by animateColorAsState(rawPalette.brandAccent, animSpec, label = "brandAcc")
    val brandSoft by animateColorAsState(rawPalette.brandSoft, animSpec, label = "brandSoft")
    val textPrimary by animateColorAsState(rawPalette.textPrimary, animSpec, label = "txtPri")
    val textSecondary by animateColorAsState(rawPalette.textSecondary, animSpec, label = "txtSec")
    val textMuted by animateColorAsState(rawPalette.textMuted, animSpec, label = "txtMut")
    val border by animateColorAsState(rawPalette.border, animSpec, label = "border")
    val borderSubtle by animateColorAsState(rawPalette.borderSubtle, animSpec, label = "borderSub")

    val palette = rawPalette.copy(
        background = background,
        backgroundElevated = backgroundElevated,
        surface = surface,
        surfaceElevated = surfaceElevated,
        brandPrimary = brandPrimary,
        brandAccent = brandAccent,
        brandSoft = brandSoft,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textMuted = textMuted,
        border = border,
        borderSubtle = borderSubtle
    )
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
        LocalSplitzPalette provides palette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Material3Shapes,
            content = content
        )
    }
}

@Composable
fun SplitzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    visualTheme: SplitzVisualTheme,
    fontScale: Float = 1.0f,
    currency: AppCurrency = AppCurrency.DEFAULT,
    content: @Composable () -> Unit
) {
    SplitzTheme(
        darkTheme = darkTheme,
        fontScale = fontScale,
        currency = currency,
        content = content
    )
}

@Composable
fun PaiseDeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    visualTheme: SplitzVisualTheme = SplitzVisualTheme.KISSATEN,
    content: @Composable () -> Unit
) {
    SplitzTheme(darkTheme = darkTheme, fontScale = 1.0f, content = content)
}

val ColorScheme.isDark: Boolean
    get() = this.background == ThemePalettes.Dark.background

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
    fun brandPrimary(): Color = LocalSplitzPalette.current.brandPrimary

    @Composable
    fun brandAccent(): Color = LocalSplitzPalette.current.brandAccent

    @Composable
    fun brandSoft(): Color = LocalSplitzPalette.current.brandSoft

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
    fun onBrandAccent(): Color = LocalSplitzPalette.current.onBrandAccent

    @Composable
    fun onPositive(): Color = LocalSplitzPalette.current.onPositive

    @Composable
    fun onPositiveContainer(): Color = LocalSplitzPalette.current.onPositiveContainer

    @Composable
    fun onNegative(): Color = LocalSplitzPalette.current.onNegative

    @Composable
    fun onNegativeContainer(): Color = LocalSplitzPalette.current.onNegativeContainer

    @Composable
    fun onWarning(): Color = LocalSplitzPalette.current.onWarning

    @Composable
    fun textTertiary(): Color = LocalSplitzPalette.current.textMuted

    @Composable
    fun error(): Color = LocalSplitzPalette.current.negative
}