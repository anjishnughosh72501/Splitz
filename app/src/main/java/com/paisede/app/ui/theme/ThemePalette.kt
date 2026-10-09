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
    // 1. GRAPHITE THEME
    // Minimal, cinematic, refined, calm, sophisticated monochrome
    // ========================================================
    val Graphite = SplitzPalette(
        background = Color(0xFF0A0B0F),
        backgroundElevated = Color(0xFF111319),
        backgroundSecondary = Color(0xFF151821),

        surface = Color(0xFF111319),
        surfaceElevated = Color(0xFF1A1D25),
        surfaceHover = Color(0xFF20242D),
        surfacePressed = Color(0xFF282D38),
        surfaceGlass = Color(0xFF111319).copy(alpha = 0.82f),
        surfaceGlassStrong = Color(0xFF1A1D25).copy(alpha = 0.92f),
        surfaceScrim = Color(0xFF050608).copy(alpha = 0.65f),

        borderSubtle = Color(0xFF1E212B),
        border = Color(0xFF2B2F3A),
        borderStrong = Color(0xFF3E4452),
        borderGlass = Color(0xFFE6E8EE).copy(alpha = 0.12f),
        borderFocus = Color(0xFFE6E8EE),

        brandPrimary = Color(0xFFE6E8EE),
        brandAccent = Color(0xFFA9B2C3),
        brandSoft = Color(0xFF20242D),
        onBrandPrimary = Color(0xFF0A0B0F),

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
        surfaceElevated = Color(0xFF14223A),
        surfaceHover = Color(0xFF192B48),
        surfacePressed = Color(0xFF203558),
        surfaceGlass = Color(0xFF0D1629).copy(alpha = 0.82f),
        surfaceGlassStrong = Color(0xFF14223A).copy(alpha = 0.92f),
        surfaceScrim = Color(0xFF03070E).copy(alpha = 0.65f),

        borderSubtle = Color(0xFF18263E),
        border = Color(0xFF263A59),
        borderStrong = Color(0xFF3B557E),
        borderGlass = Color(0xFF5C9CFF).copy(alpha = 0.18f),
        borderFocus = Color(0xFF5C9CFF),

        brandPrimary = Color(0xFF5C9CFF),
        brandAccent = Color(0xFF86B8FF),
        brandSoft = Color(0xFF162E54),
        onBrandPrimary = Color(0xFF070D1A),

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
        surfaceElevated = Color(0xFF25123D),
        surfaceHover = Color(0xFF2F174D),
        surfacePressed = Color(0xFF3A1D5E),
        surfaceGlass = Color(0xFF190D2B).copy(alpha = 0.82f),
        surfaceGlassStrong = Color(0xFF25123D).copy(alpha = 0.92f),
        surfaceScrim = Color(0xFF080312).copy(alpha = 0.65f),

        borderSubtle = Color(0xFF2D1847),
        border = Color(0xFF46305E),
        borderStrong = Color(0xFF624583),
        borderGlass = Color(0xFFA78BFA).copy(alpha = 0.18f),
        borderFocus = Color(0xFFA78BFA),

        brandPrimary = Color(0xFFA78BFA),
        brandAccent = Color(0xFFC4A7FF),
        brandSoft = Color(0xFF39205A),
        onBrandPrimary = Color(0xFF10081F),

        textPrimary = Color(0xFFFAF6FF),
        textSecondary = Color(0xFFCDBDE6),
        textMuted = Color(0xFF9B88B9),
        textInverse = Color(0xFF10081F),
        textDisabled = Color(0xFF5C4B74),

        textureResId = R.drawable.bg_ultraviolet_texture
    )

    fun forTheme(theme: SplitzVisualTheme): SplitzPalette {
        return when (theme) {
            SplitzVisualTheme.GRAPHITE -> Graphite
            SplitzVisualTheme.ELECTRIC_BLUE -> ElectricBlue
            SplitzVisualTheme.ULTRAVIOLET -> Ultraviolet
        }
    }
}
