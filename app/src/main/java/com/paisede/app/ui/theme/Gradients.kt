package com.paisede.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object SplitzGradients {
    @Composable
    fun primary(): Brush {
        val palette = LocalSplitzPalette.current
        return Brush.horizontalGradient(
            colors = listOf(
                palette.brandPrimary,
                palette.brandAccent,
                palette.brandPrimary
            )
        )
    }

    @Composable
    fun hero(): Brush {
        val palette = LocalSplitzPalette.current
        return Brush.verticalGradient(
            colors = listOf(
                palette.brandSoft.copy(alpha = 0.5f),
                palette.surfaceElevated.copy(alpha = 0.85f),
                palette.background.copy(alpha = 0.95f)
            )
        )
    }

    val Primary = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF5C9CFF),
            Color(0xFF86B8FF),
            Color(0xFF5C9CFF)
        )
    )

    val PrimaryBright = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF86B8FF),
            Color(0xFF5C9CFF),
            Color(0xFF86B8FF)
        )
    )

    val Success = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF10B981),
            Color(0xFF059669)
        )
    )

    val Debt = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFEF4444),
            Color(0xFFDC2626)
        )
    )

    val Warning = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFF59E0B),
            Color(0xFFD97706)
        )
    )

    val Info = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF3B82F6),
            Color(0xFF2563EB)
        )
    )

    val Analytics = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF6366F1),
            Color(0xFF8B5CF6),
            Color(0xFFEC4899)
        )
    )

    val Hero = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF14223A),
            Color(0xFF0D1629),
            Color(0xFF070D1A)
        )
    )

    val GlassHighlight = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.15f),
            Color.White.copy(alpha = 0.02f),
            Color.Transparent
        )
    )

    val GlassStrong = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.25f),
            Color.White.copy(alpha = 0.08f),
            Color.White.copy(alpha = 0.02f)
        )
    )
}