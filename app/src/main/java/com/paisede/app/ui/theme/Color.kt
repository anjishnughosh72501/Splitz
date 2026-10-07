package com.paisede.app.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// PRIMITIVE COLOR TOKENS
// ============================================================

// Neutral / Slate
val Slate950 = Color(0xFF020617)
val Slate900 = Color(0xFF0F172A)
val Slate850 = Color(0xFF16213A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate600 = Color(0xFF475569)
val Slate500 = Color(0xFF64748B)
val Slate400 = Color(0xFF94A3B8)
val Slate300 = Color(0xFFCBD5E1)
val Slate200 = Color(0xFFE2E8F0)
val Slate100 = Color(0xFFF1F5F9)
val Slate50 = Color(0xFFF8FAFC)
val Slate25 = Color(0xFFFCFDFE)

// Brand / Teal
val Teal900 = Color(0xFF042F2E)
val Teal800 = Color(0xFF0F766E)
val Teal700 = Color(0xFF0D9488)
val Teal600 = Color(0xFF0F9B8F)
val Teal500 = Color(0xFF14B8A6)
val Teal400 = Color(0xFF2DD4BF)
val Teal300 = Color(0xFF5EEAD4)
val Teal200 = Color(0xFF99F6E4)
val Teal100 = Color(0xFFCCFBF1)
val Teal50 = Color(0xFFF0FDFA)

// Emerald / Positive
val Emerald900 = Color(0xFF052E16)
val Emerald800 = Color(0xFF064E23)
val Emerald700 = Color(0xFF047857)
val Emerald600 = Color(0xFF059669)
val Emerald500 = Color(0xFF10B981)
val Emerald400 = Color(0xFF34D399)
val Emerald300 = Color(0xFF6EE7B7)
val Emerald200 = Color(0xFFA7F3D0)
val Emerald100 = Color(0xFFD1FAE5)
val Emerald50 = Color(0xFFECFDF5)

// Coral / Negative
val Red900 = Color(0xFF450A0A)
val Red800 = Color(0xFF7F1D1D)
val Red700 = Color(0xFFB91C1C)
val Red600 = Color(0xFFDC2626)
val Red500 = Color(0xFFEF4444)
val Red400 = Color(0xFFF87171)
val Red300 = Color(0xFFFDBA74)
val Red200 = Color(0xFFFECACA)
val Red100 = Color(0xFFFEE2E2)
val Red50 = Color(0xFFFEF2F2)

// Amber / Warning
val Amber900 = Color(0xFF451A03)
val Amber800 = Color(0xFF78350F)
val Amber700 = Color(0xFFB45309)
val Amber600 = Color(0xFFD97706)
val Amber500 = Color(0xFFF59E0B)
val Amber400 = Color(0xFFFBBF24)
val Amber300 = Color(0xFFFDE047)
val Amber200 = Color(0xFFFEF3C7)
val Amber100 = Color(0xFFFFF8E1)
val Amber50 = Color(0xFFFFFBEB)

// Indigo / Info
val Indigo900 = Color(0xFF1E1B4B)
val Indigo800 = Color(0xFF28255B)
val Indigo700 = Color(0xFF3730A3)
val Indigo600 = Color(0xFF4338CA)
val Indigo500 = Color(0xFF6366F1)
val Indigo400 = Color(0xFF818CF8)
val Indigo300 = Color(0xFFA5B4FC)
val Indigo200 = Color(0xFFE0E7FF)
val Indigo100 = Color(0xFFEEF2FF)
val Indigo50 = Color(0xFFEEF2FF)

// Violet / Accent
val Violet900 = Color(0xFF2E1065)
val Violet800 = Color(0xFF4C1D95)
val Violet700 = Color(0xFF6D28D9)
val Violet600 = Color(0xFF8B5CF6)
val Violet500 = Color(0xFFA855F7)
val Violet400 = Color(0xFFC084FC)
val Violet300 = Color(0xFFD8B4FE)
val Violet200 = Color(0xFFEDE9FE)
val Violet100 = Color(0xFFF5F3FF)
val Violet50 = Color(0xFFFAF5FF)

// White / Black
val PureWhite = Color.White
val PureBlack = Color.Black

// ============================================================
// LIGHT THEME SEMANTIC COLORS
// ============================================================

object LightColors {
    // Backgrounds
    val Background = Slate50
    val BackgroundElevated = PureWhite
    val BackgroundSecondary = Slate100

    // Surfaces
    val Surface = PureWhite
    val SurfaceElevated = PureWhite
    val SurfaceHover = Slate50
    val SurfacePressed = Slate100
    val SurfaceGlass = PureWhite.copy(alpha = 0.85f)
    val SurfaceGlassStrong = PureWhite.copy(alpha = 0.95f)
    val SurfaceScrim = PureBlack.copy(alpha = 0.3f)

    // Borders
    val BorderSubtle = Slate200
    val Border = Slate200
    val BorderStrong = Slate300
    val BorderGlass = PureWhite.copy(alpha = 0.6f)
    val BorderFocus = Teal500

    // Primary
    val Primary = Teal700
    val PrimaryBright = Teal500
    val PrimaryDark = Teal800
    val PrimaryContainer = Teal100
    val PrimaryContainerHover = Teal200
    val OnPrimary = PureWhite
    val OnPrimaryContainer = Teal900

    // Secondary
    val Secondary = Slate700
    val SecondaryContainer = Slate100
    val OnSecondary = PureWhite
    val OnSecondaryContainer = Slate900

    // Tertiary
    val Tertiary = Indigo600
    val TertiaryContainer = Indigo100
    val OnTertiary = PureWhite
    val OnTertiaryContainer = Indigo900

    // Semantic Status
    val Positive = Emerald600
    val PositiveContainer = Emerald50
    val PositiveStrong = Emerald700
    val OnPositive = PureWhite
    val OnPositiveContainer = Emerald900

    val Negative = Red600
    val NegativeContainer = Red50
    val NegativeStrong = Red700
    val OnNegative = PureWhite
    val OnNegativeContainer = Red900

    val Warning = Amber600
    val WarningContainer = Amber50
    val WarningStrong = Amber700
    val OnWarning = Slate900
    val OnWarningContainer = Amber900

    val Info = Indigo600
    val InfoContainer = Indigo50
    val InfoStrong = Indigo700
    val OnInfo = PureWhite
    val OnInfoContainer = Indigo900

    // Text
    val TextPrimary = Slate900
    val TextSecondary = Slate600
    val TextMuted = Slate400
    val TextInverse = PureWhite
    val TextOnPrimary = PureWhite
    val TextOnColor = PureWhite
    val TextDisabled = Slate400

    // Icon
    val IconPrimary = Slate700
    val IconSecondary = Slate500
    val IconMuted = Slate400
    val IconInverse = PureWhite

    // Input
    val InputBackground = PureWhite
    val InputBorder = Slate300
    val InputBorderFocus = Teal500
    val InputBorderError = Red500
    val InputPlaceholder = Slate400
    val InputText = Slate900

    // Divider
    val Divider = Slate200
    val DividerStrong = Slate300

    // Shadow
    val Shadow = PureBlack.copy(alpha = 0.08f)
    val ShadowStrong = PureBlack.copy(alpha = 0.15f)
    val ShadowColored = Teal700.copy(alpha = 0.15f)

    // Overlay
    val OverlayLight = PureBlack.copy(alpha = 0.32f)
    val OverlayMedium = PureBlack.copy(alpha = 0.5f)
    val OverlayDark = PureBlack.copy(alpha = 0.65f)
}

// ============================================================
// DARK THEME SEMANTIC COLORS
// ============================================================

object DarkColors {
    // Backgrounds
    val Background = Slate950
    val BackgroundElevated = Slate900
    val BackgroundSecondary = Slate850

    // Surfaces
    val Surface = Slate900
    val SurfaceElevated = Slate850
    val SurfaceHover = Slate800
    val SurfacePressed = Slate700
    val SurfaceGlass = Slate900.copy(alpha = 0.85f)
    val SurfaceGlassStrong = Slate900.copy(alpha = 0.95f)
    val SurfaceScrim = PureBlack.copy(alpha = 0.5f)

    // Borders
    val BorderSubtle = Slate800
    val Border = Slate700
    val BorderStrong = Slate600
    val BorderGlass = PureWhite.copy(alpha = 0.1f)
    val BorderFocus = Teal400

    // Primary
    val Primary = Teal400
    val PrimaryBright = Teal300
    val PrimaryDark = Teal500
    val PrimaryContainer = Teal900
    val PrimaryContainerHover = Teal800
    val OnPrimary = Slate950
    val OnPrimaryContainer = Teal50

    // Secondary
    val Secondary = Slate300
    val SecondaryContainer = Slate800
    val OnSecondary = Slate950
    val OnSecondaryContainer = Slate100

    // Tertiary
    val Tertiary = Indigo400
    val TertiaryContainer = Indigo900
    val OnTertiary = Slate950
    val OnTertiaryContainer = Indigo50

    // Semantic Status
    val Positive = Emerald400
    val PositiveContainer = Emerald900.copy(alpha = 0.3f)
    val PositiveStrong = Emerald300
    val OnPositive = Slate950
    val OnPositiveContainer = Emerald50

    val Negative = Red400
    val NegativeContainer = Red900.copy(alpha = 0.3f)
    val NegativeStrong = Red300
    val OnNegative = Slate950
    val OnNegativeContainer = Red50

    val Warning = Amber400
    val WarningContainer = Amber900.copy(alpha = 0.3f)
    val WarningStrong = Amber300
    val OnWarning = Slate950
    val OnWarningContainer = Amber50

    val Info = Indigo400
    val InfoContainer = Indigo900.copy(alpha = 0.3f)
    val InfoStrong = Indigo300
    val OnInfo = Slate950
    val OnInfoContainer = Indigo50

    // Text
    val TextPrimary = Slate50
    val TextSecondary = Slate300
    val TextMuted = Slate500
    val TextInverse = Slate950
    val TextOnPrimary = Slate950
    val TextOnColor = Slate950
    val TextDisabled = Slate600

    // Icon
    val IconPrimary = Slate200
    val IconSecondary = Slate400
    val IconMuted = Slate600
    val IconInverse = Slate950

    // Input
    val InputBackground = Slate850
    val InputBorder = Slate700
    val InputBorderFocus = Teal400
    val InputBorderError = Red400
    val InputPlaceholder = Slate500
    val InputText = Slate50

    // Divider
    val Divider = Slate800
    val DividerStrong = Slate700

    // Shadow
    val Shadow = PureBlack.copy(alpha = 0.3f)
    val ShadowStrong = PureBlack.copy(alpha = 0.5f)
    val ShadowColored = Teal400.copy(alpha = 0.15f)

    // Overlay
    val OverlayLight = PureBlack.copy(alpha = 0.4f)
    val OverlayMedium = PureBlack.copy(alpha = 0.6f)
    val OverlayDark = PureBlack.copy(alpha = 0.8f)
}

// ============================================================
// LEGACY ALIASES (for backward compatibility during migration)
// ============================================================

val BrandTeal = Teal700
val BrandTealLight = Teal500
val BrandTealDark = Teal800
val BrandTealContainer = Teal100

val CreditGreen = Emerald600
val CreditGreenDark = Emerald700
val CreditGreenContainer = Emerald100

val DebtRed = Red600
val DebtRedDark = Red700
val DebtRedContainer = Red100

val AccentAmber = Amber600
val AccentAmberContainer = Amber100

val IndigoPurple = Indigo600
val IndigoContainer = Indigo100