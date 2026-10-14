package com.paisede.app.ui.theme

import androidx.compose.ui.graphics.Color
import com.paisede.app.R
import com.paisede.app.data.preferences.SplitzVisualTheme

/**
 * Complete semantic theme palette specification for Splitz visual themes.
 */
data class SplitzPalette(
    // App background
    val background: Color,
    val backgroundElevated: Color,
    val backgroundSecondary: Color,

    // Surfaces
    val surface: Color,
    val surfaceElevated: Color,
    val surfaceHover: Color,
    val surfacePressed: Color,
    val surfaceGlass: Color,
    val surfaceGlassStrong: Color,
    val surfaceScrim: Color,

    // Claymorphism tokens
    val claySurface: Color = surfaceElevated,
    val clayShadow: Color = Color.Black.copy(alpha = 0.45f),
    val clayHighlight: Color = Color.White.copy(alpha = 0.16f),

    // Borders
    val borderSubtle: Color,
    val border: Color,
    val borderStrong: Color,
    val borderGlass: Color,
    val borderFocus: Color,

    // Brand tokens
    val brandPrimary: Color,
    val brandAccent: Color,
    val brandSoft: Color,
    val onBrandPrimary: Color,
    val onBrandAccent: Color = onBrandPrimary,
    val onBrandGradient: Color = Color.White,

    // Gradient start and end stops
    val gradientStart: Color = brandPrimary,
    val gradientEnd: Color = brandAccent,

    // Text tokens
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textInverse: Color,
    val textDisabled: Color,

    // Financial semantics (stable across themes)
    val positive: Color = Color(0xFF34D399),
    val positiveContainer: Color = Color(0xFF0E2E1E),
    val positiveStrong: Color = Color(0xFF6EE7B7),
    val onPositive: Color = Color(0xFF041F11),
    val onPositiveContainer: Color = Color(0xFF6EE7B7),

    val negative: Color = Color(0xFFF87171),
    val negativeContainer: Color = Color(0xFF361418),
    val negativeStrong: Color = Color(0xFFFECACA),
    val onNegative: Color = Color(0xFF28070B),
    val onNegativeContainer: Color = Color(0xFFFECACA),

    val warning: Color = Color(0xFFFBBF24),
    val warningContainer: Color = Color(0xFF362309),
    val warningStrong: Color = Color(0xFFFDE047),
    val onWarning: Color = Color(0xFF261502),

    val info: Color = Color(0xFF60A5FA),
    val infoContainer: Color = Color(0xFF142244),

    // Texture asset reference
    val textureResId: Int
)

object ThemePalettes {

    // ========================================================
    // 1. LIGHT THEME (Basic Splitz Kissaten / Kohi Split)
    // Warm tactile washi paper, deep espresso, and caramel accents
    // ========================================================
    val Light = SplitzPalette(
        background = Color(0xFFFDF9F3),
        backgroundElevated = Color(0xFFF7F3ED),
        backgroundSecondary = Color(0xFFF1EDE7),

        surface = Color(0xFFFDF9F3),
        surfaceElevated = Color(0xFFFFFFFF),
        surfaceHover = Color(0xFFEBE8E2),
        surfacePressed = Color(0xFFE6E2DC),
        surfaceGlass = Color(0xFFFDF9F3).copy(alpha = 0.88f),
        surfaceGlassStrong = Color(0xFFFFFFFF).copy(alpha = 0.94f),
        surfaceScrim = Color(0xFF1C1C18).copy(alpha = 0.45f),

        claySurface = Color(0xFFFFFDF9),
        clayShadow = Color(0xFF3D2B1F).copy(alpha = 0.08f),
        clayHighlight = Color(0xFFFFFFFF).copy(alpha = 0.90f),

        borderSubtle = Color(0xFFEBE2D5),
        border = Color(0xFFD2C4BB),
        borderStrong = Color(0xFF81756D),
        borderGlass = Color(0xFFD2C4BB).copy(alpha = 0.55f),
        borderFocus = Color(0xFFC87D43),

        brandPrimary = Color(0xFF4A3525),
        brandAccent = Color(0xFFC87D43),
        brandSoft = Color(0xFFFEDCC5),
        onBrandPrimary = Color(0xFFFAF6F0),
        onBrandAccent = Color(0xFFFFFFFF),
        onBrandGradient = Color(0xFFFAF6F0),
        gradientStart = Color(0xFF4A3525),
        gradientEnd = Color(0xFFC87D43),

        textPrimary = Color(0xFF1C1C18),
        textSecondary = Color(0xFF4F453E),
        textMuted = Color(0xFF81756D),
        textInverse = Color(0xFFFDF9F3),
        textDisabled = Color(0xFFA89F97),

        positive = Color(0xFF166534),
        positiveContainer = Color(0xFFE8F5E9),
        positiveStrong = Color(0xFF14532D),
        onPositive = Color(0xFFFFFFFF),
        onPositiveContainer = Color(0xFF14532D),

        negative = Color(0xFFB91C1C),
        negativeContainer = Color(0xFFFEE2E2),
        negativeStrong = Color(0xFF7F1D1D),
        onNegative = Color(0xFFFFFFFF),
        onNegativeContainer = Color(0xFF7F1D1D),

        warning = Color(0xFFB45309),
        warningContainer = Color(0xFFFEF3C7),
        warningStrong = Color(0xFF78350F),
        onWarning = Color(0xFFFFFFFF),

        info = Color(0xFF1E40AF),
        infoContainer = Color(0xFFDBEAFE),

        textureResId = R.drawable.bg_graphite_texture
    )

    // ========================================================
    // 2. DARK THEME (Basic Splitz Dark Espresso & Roasted Caramel)
    // Deep warm espresso charcoal, cream contrast, and glowing caramel accents
    // ========================================================
    val Dark = SplitzPalette(
        background = Color(0xFF141210),
        backgroundElevated = Color(0xFF1C1916),
        backgroundSecondary = Color(0xFF221E1A),

        surface = Color(0xFF1A1714),
        surfaceElevated = Color(0xFF25211D),
        surfaceHover = Color(0xFF2F2A25),
        surfacePressed = Color(0xFF3A342E),
        surfaceGlass = Color(0xFF1A1714).copy(alpha = 0.88f),
        surfaceGlassStrong = Color(0xFF25211D).copy(alpha = 0.94f),
        surfaceScrim = Color(0xFF080706).copy(alpha = 0.70f),

        claySurface = Color(0xFF25211D),
        clayShadow = Color(0xFF000000).copy(alpha = 0.55f),
        clayHighlight = Color(0xFFFFFFFF).copy(alpha = 0.12f),

        borderSubtle = Color(0xFF332C26),
        border = Color(0xFF4A4138),
        borderStrong = Color(0xFF706356),
        borderGlass = Color(0xFFDDA16B).copy(alpha = 0.22f),
        borderFocus = Color(0xFFDDA16B),

        brandPrimary = Color(0xFFF5EBE1),
        brandAccent = Color(0xFFDDA16B),
        brandSoft = Color(0xFF3A291E),
        onBrandPrimary = Color(0xFF141210),
        onBrandAccent = Color(0xFF141210),
        onBrandGradient = Color(0xFF141210),
        gradientStart = Color(0xFFDDA16B),
        gradientEnd = Color(0xFFC87D43),

        textPrimary = Color(0xFFFDF9F3),
        textSecondary = Color(0xFFD4C8BE),
        textMuted = Color(0xFF9E9186),
        textInverse = Color(0xFF141210),
        textDisabled = Color(0xFF6B6259),

        positive = Color(0xFF34D399),
        positiveContainer = Color(0xFF0E2E1E),
        positiveStrong = Color(0xFF6EE7B7),
        onPositive = Color(0xFF041F11),
        onPositiveContainer = Color(0xFF6EE7B7),

        negative = Color(0xFFF87171),
        negativeContainer = Color(0xFF361418),
        negativeStrong = Color(0xFFFECACA),
        onNegative = Color(0xFF28070B),
        onNegativeContainer = Color(0xFFFECACA),

        warning = Color(0xFFFBBF24),
        warningContainer = Color(0xFF362309),
        warningStrong = Color(0xFFFDE047),
        onWarning = Color(0xFF261502),

        info = Color(0xFF60A5FA),
        infoContainer = Color(0xFF142244),

        textureResId = R.drawable.bg_graphite_texture
    )

    // Backward-compatibility aliases
    val Kissaten = Light
    val Graphite = Dark
    val ElectricBlue = Dark
    val Ultraviolet = Dark
    val Emerald = Dark

    /**
     * Resolves the palette based directly on the theme mode (light vs dark).
     */
    fun forMode(darkTheme: Boolean): SplitzPalette {
        return if (darkTheme) Dark else Light
    }

    /**
     * Legacy adapter for backward compatibility.
     */
    fun forTheme(theme: SplitzVisualTheme): SplitzPalette {
        return Light
    }
}
