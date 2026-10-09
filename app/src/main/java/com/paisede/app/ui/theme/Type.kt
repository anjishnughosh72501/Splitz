package com.paisede.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

object SplitzFontFamily {
    val Display = FontFamily.SansSerif
    val Sans = FontFamily.SansSerif
    // Uses Monospace / tabular numeric font representation for rock-solid stability in currency and amounts
    val Mono = FontFamily.Monospace
}

object SplitzTypography {
    val DisplayLarge = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp,
        lineHeight = 60.sp,
        letterSpacing = (-1.2).sp
    )

    val DisplayMedium = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.8).sp
    )

    val DisplaySmall = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    )

    val HeadlineLarge = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.4).sp
    )

    val HeadlineMedium = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp
    )

    val HeadlineSmall = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )

    val TitleLarge = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    )

    val TitleMedium = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    )

    val TitleSmall = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    val BodyLarge = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    )

    val BodyMedium = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )

    val BodySmall = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )

    val LabelLarge = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    val LabelMedium = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

    val LabelSmall = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.5.sp
    )

    val Caption = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.4.sp
    )

    // Financial & numeric hierarchy styles with tabular digits stability
    val MoneyDisplay = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.6).sp
    )

    val MoneyLarge = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.3).sp
    )

    val MoneyMedium = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.2).sp
    )

    val MoneySmall = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )
}

// Material3 Typography mapping
val Typography = Typography(
    displayLarge = SplitzTypography.DisplayLarge,
    displayMedium = SplitzTypography.DisplayMedium,
    displaySmall = SplitzTypography.DisplaySmall,
    headlineLarge = SplitzTypography.HeadlineLarge,
    headlineMedium = SplitzTypography.HeadlineMedium,
    headlineSmall = SplitzTypography.HeadlineSmall,
    titleLarge = SplitzTypography.TitleLarge,
    titleMedium = SplitzTypography.TitleMedium,
    titleSmall = SplitzTypography.TitleSmall,
    bodyLarge = SplitzTypography.BodyLarge,
    bodyMedium = SplitzTypography.BodyMedium,
    bodySmall = SplitzTypography.BodySmall,
    labelLarge = SplitzTypography.LabelLarge,
    labelMedium = SplitzTypography.LabelMedium,
    labelSmall = SplitzTypography.LabelSmall
)