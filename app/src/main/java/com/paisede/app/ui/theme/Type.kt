package com.paisede.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object SplitzFontFamily {
    val Display = FontFamily.Default
    val Sans = FontFamily.Default
    val Mono = FontFamily.Monospace
}

object SplitzTypography {
    val DisplayLarge = TextStyle(
        fontFamily = SplitzFontFamily.Display,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-1.5).sp
    )

    val DisplayMedium = TextStyle(
        fontFamily = SplitzFontFamily.Display,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = (-1.0).sp
    )

    val DisplaySmall = TextStyle(
        fontFamily = SplitzFontFamily.Display,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.5).sp
    )

    val HeadlineLarge = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    )

    val HeadlineMedium = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.25).sp
    )

    val HeadlineSmall = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )

    val TitleLarge = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )

    val TitleMedium = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
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
        fontSize = 16.sp,
        lineHeight = 24.sp,
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
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

    val Caption = TextStyle(
        fontFamily = SplitzFontFamily.Sans,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )

    // Money-specific styles
    val MoneyDisplay = TextStyle(
        fontFamily = SplitzFontFamily.Mono,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = (-1.0).sp
    )

    val MoneyLarge = TextStyle(
        fontFamily = SplitzFontFamily.Mono,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    )

    val MoneyMedium = TextStyle(
        fontFamily = SplitzFontFamily.Mono,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.25).sp
    )

    val MoneySmall = TextStyle(
        fontFamily = SplitzFontFamily.Mono,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    )
}

// Material3 Typography mapping for backward compatibility
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