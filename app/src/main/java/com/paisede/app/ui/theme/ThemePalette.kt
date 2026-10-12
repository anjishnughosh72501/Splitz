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
    val positiveContainer: Color = Color(0xFF064E23).copy(alpha = 0.35f),
    val positiveStrong: Color = Color(0xFF6EE7B7),
    val onPositive: Color = Color(0xFF052E16),

    val negative: Color = Color(0xFFF87171),
    val negativeContainer: Color = Color(0xFF7F1D1D).copy(alpha = 0.35f),
    val negativeStrong: Color = Color(0xFFFECACA),
    val onNegative: Color = Color(0xFF450A0A),

    val warning: Color = Color(0xFFFBBF24),
    val warningContainer: Color = Color(0xFF78350F).copy(alpha = 0.35f),
    val warningStrong: Color = Color(0xFFFDE047),
    val onWarning: Color = Color(0xFF451A03),

    val info: Color = Color(0xFF60A5FA),
    val infoContainer: Color = Color(0xFF1E3A8A).copy(alpha = 0.35f),

    // Texture asset reference
    val textureResId: Int
)

object ThemePalettes {

    // ========================================================
    // 0. KISSATEN THEME (Kohi Split / Stitch Source of Truth)
    // Warm tactile washi paper, deep espresso, and matcha accents
    // ========================================================
    val Kissaten = SplitzPalette(
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
        onBrandGradient = Color(0xFFFAF6F0),
        gradientStart = Color(0xFF4A3525),
        gradientEnd = Color(0xFFC87D43),

        textPrimary = Color(0xFF1C1C18),
        textSecondary = Color(0xFF4F453E),
        textMuted = Color(0xFF81756D),
        textInverse = Color(0xFFFDF9F3),
        textDisabled = Color(0xFFA89F97),

        positive = Color(0xFF353D11),
        positiveContainer = Color(0xFFDDE8AB),
        positiveStrong = Color(0xFF1F2700),
        onPositive = Color(0xFFFFFFFF),

        negative = Color(0xFFBA1A1A),
        negativeContainer = Color(0xFFFFDAD6),
        negativeStrong = Color(0xFF93000A),
        onNegative = Color(0xFFFFFFFF),

        warning = Color(0xFFC87D43),
        warningContainer = Color(0xFFFFDCC5),
        warningStrong = Color(0xFF703700),
        onWarning = Color(0xFF301400),

        info = Color(0xFF4A3525),
        infoContainer = Color(0xFFFEDCC5),

        textureResId = R.drawable.bg_graphite_texture
    )

    // ========================================================
    // 1. GRAPHITE THEME
    // Minimal, cinematic, refined, calm, sophisticated monochrome
    // ========================================================
    val Graphite = SplitzPalette(
        background = Color(0xFF0A0B0F),
        backgroundElevated = Color(0xFF111319),
        backgroundSecondary = Color(0xFF151821),

        surface = Color(0xFF111319),
        surfaceElevated = Color(0xFF181B23),
        surfaceHover = Color(0xFF20242D),
        surfacePressed = Color(0xFF282D38),
        surfaceGlass = Color(0xFF111319).copy(alpha = 0.82f),
        surfaceGlassStrong = Color(0xFF181B23).copy(alpha = 0.92f),
        surfaceScrim = Color(0xFF050608).copy(alpha = 0.65f),

        claySurface = Color(0xFF1B1E28),
        clayShadow = Color(0xFF000000).copy(alpha = 0.55f),
        clayHighlight = Color(0xFFFFFFFF).copy(alpha = 0.18f),

        borderSubtle = Color(0xFF282C3A),
        border = Color(0xFF3B4154),
        borderStrong = Color(0xFF555D75),
        borderGlass = Color(0xFFE6E8EE).copy(alpha = 0.22f),
        borderFocus = Color(0xFFE6E8EE),

        brandPrimary = Color(0xFFE6E8EE),
        brandAccent = Color(0xFFA9B2C3),
        brandSoft = Color(0xFF20242D),
        onBrandPrimary = Color(0xFF0A0B0F),
        onBrandGradient = Color(0xFF0A0B0F),
        gradientStart = Color(0xFFE6E8EE),
        gradientEnd = Color(0xFFA9B2C3),

        textPrimary = Color(0xFFF5F6F8),
        textSecondary = Color(0xFFB0B4BF),
        textMuted = Color(0xFF858A97),
        textInverse = Color(0xFF0A0B0F),
        textDisabled = Color(0xFF565B66),

        textureResId = R.drawable.bg_graphite_texture
    )

    // ========================================================
    // 2. ELECTRIC BLUE THEME
    // Technical, energetic, intelligent, premium, modern, clear
    // ========================================================
    val ElectricBlue = SplitzPalette(
        background = Color(0xFF070D1A),
        backgroundElevated = Color(0xFF0D1629),
        backgroundSecondary = Color(0xFF101B33),

        surface = Color(0xFF0D1629),
        surfaceElevated = Color(0xFF152646),
        surfaceHover = Color(0xFF1B3058),
        surfacePressed = Color(0xFF223A68),
        surfaceGlass = Color(0xFF0D1629).copy(alpha = 0.82f),
        surfaceGlassStrong = Color(0xFF152646).copy(alpha = 0.94f),
        surfaceScrim = Color(0xFF03070E).copy(alpha = 0.65f),

        claySurface = Color(0xFF162747),
        clayShadow = Color(0xFF02050B).copy(alpha = 0.60f),
        clayHighlight = Color(0xFF5C9CFF).copy(alpha = 0.22f),

        borderSubtle = Color(0xFF22375C),
        border = Color(0xFF345284),
        borderStrong = Color(0xFF4C73B3),
        borderGlass = Color(0xFF5C9CFF).copy(alpha = 0.25f),
        borderFocus = Color(0xFF5C9CFF),

        brandPrimary = Color(0xFF5C9CFF),
        brandAccent = Color(0xFF86B8FF),
        brandSoft = Color(0xFF162E54),
        onBrandPrimary = Color(0xFF070D1A),
        onBrandGradient = Color(0xFFFFFFFF),
        gradientStart = Color(0xFF3B82F6),
        gradientEnd = Color(0xFF60A5FA),

        textPrimary = Color(0xFFF3F7FF),
        textSecondary = Color(0xFFB8C7E3),
        textMuted = Color(0xFF8294B4),
        textInverse = Color(0xFF070D1A),
        textDisabled = Color(0xFF4C5D7A),

        textureResId = R.drawable.bg_electric_blue_texture
    )

    // ========================================================
    // 3. ULTRAVIOLET THEME
    // Expressive, atmospheric, creative, bold, premium, slightly futuristic
    // ========================================================
    val Ultraviolet = SplitzPalette(
        background = Color(0xFF10081F),
        backgroundElevated = Color(0xFF190D2B),
        backgroundSecondary = Color(0xFF211138),

        surface = Color(0xFF190D2B),
        surfaceElevated = Color(0xFF281442),
        surfaceHover = Color(0xFF321A52),
        surfacePressed = Color(0xFF3D2063),
        surfaceGlass = Color(0xFF190D2B).copy(alpha = 0.82f),
        surfaceGlassStrong = Color(0xFF281442).copy(alpha = 0.94f),
        surfaceScrim = Color(0xFF080312).copy(alpha = 0.65f),

        claySurface = Color(0xFF2C1648),
        clayShadow = Color(0xFF05010B).copy(alpha = 0.60f),
        clayHighlight = Color(0xFFA78BFA).copy(alpha = 0.24f),

        borderSubtle = Color(0xFF381F57),
        border = Color(0xFF52317E),
        borderStrong = Color(0xFF7247AC),
        borderGlass = Color(0xFFA78BFA).copy(alpha = 0.25f),
        borderFocus = Color(0xFFA78BFA),

        brandPrimary = Color(0xFFA78BFA),
        brandAccent = Color(0xFFC4A7FF),
        brandSoft = Color(0xFF39205A),
        onBrandPrimary = Color(0xFF10081F),
        onBrandGradient = Color(0xFFFFFFFF),
        gradientStart = Color(0xFF8B5CF6),
        gradientEnd = Color(0xFFA78BFA),

        textPrimary = Color(0xFFFAF6FF),
        textSecondary = Color(0xFFCDBDE6),
        textMuted = Color(0xFF9B88B9),
        textInverse = Color(0xFF10081F),
        textDisabled = Color(0xFF5C4B74),

        textureResId = R.drawable.bg_ultraviolet_texture
    )

    // ========================================================
    // 4. EMERALD THEME
    // Balanced, modern, fresh botanical jade & forest contours
    // ========================================================
    val Emerald = SplitzPalette(
        background = Color(0xFF08140F),
        backgroundElevated = Color(0xFF0E2019),
        backgroundSecondary = Color(0xFF132A22),

        surface = Color(0xFF0E2019),
        surfaceElevated = Color(0xFF163227),
        surfaceHover = Color(0xFF1D3E31),
        surfacePressed = Color(0xFF244C3D),
        surfaceGlass = Color(0xFF0E2019).copy(alpha = 0.82f),
        surfaceGlassStrong = Color(0xFF163227).copy(alpha = 0.94f),
        surfaceScrim = Color(0xFF030A07).copy(alpha = 0.65f),

        claySurface = Color(0xFF16382D),
        clayShadow = Color(0xFF020705).copy(alpha = 0.60f),
        clayHighlight = Color(0xFF34D399).copy(alpha = 0.22f),

        borderSubtle = Color(0xFF1E4637),
        border = Color(0xFF2E634F),
        borderStrong = Color(0xFF438A70),
        borderGlass = Color(0xFF34D399).copy(alpha = 0.25f),
        borderFocus = Color(0xFF10B981),

        brandPrimary = Color(0xFF34D399),
        brandAccent = Color(0xFF10B981),
        brandSoft = Color(0xFF164435),
        onBrandPrimary = Color(0xFF08140F),
        onBrandGradient = Color(0xFFFFFFFF),
        gradientStart = Color(0xFF059669),
        gradientEnd = Color(0xFF34D399),

        textPrimary = Color(0xFFF0FDF4),
        textSecondary = Color(0xFFB7E4C7),
        textMuted = Color(0xFF74A892),
        textInverse = Color(0xFF08140F),
        textDisabled = Color(0xFF43695B),

        textureResId = R.drawable.bg_electric_blue_texture
    )

    fun forTheme(theme: SplitzVisualTheme): SplitzPalette {
        return when (theme) {
            SplitzVisualTheme.KISSATEN -> Kissaten
            SplitzVisualTheme.GRAPHITE -> Graphite
            SplitzVisualTheme.ELECTRIC_BLUE -> ElectricBlue
            SplitzVisualTheme.ULTRAVIOLET -> Ultraviolet
            SplitzVisualTheme.EMERALD -> Emerald
        }
    }
}
