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
                palette.gradientStart,
                palette.gradientEnd,
                palette.gradientStart
            )
        )
    }

    @Composable
    fun hero(): Brush {
        val palette = LocalSplitzPalette.current
        return Brush.verticalGradient(
            colors = listOf(
                palette.claySurface.copy(alpha = 0.90f),
                palette.surfaceElevated.copy(alpha = 0.85f),
                palette.background.copy(alpha = 0.95f)
            )
        )
    }

    @Composable
    fun success(): Brush {
        val palette = LocalSplitzPalette.current
        return Brush.horizontalGradient(
            colors = listOf(
                palette.positive,
                palette.positiveStrong
            )
        )
    }

    @Composable
    fun debt(): Brush {
        val palette = LocalSplitzPalette.current
        return Brush.horizontalGradient(
            colors = listOf(
                palette.negative,
                palette.negativeStrong
            )
        )
    }

    @Composable
    fun warning(): Brush {
        val palette = LocalSplitzPalette.current
        return Brush.horizontalGradient(
            colors = listOf(
                palette.warning,
                palette.warningStrong
            )
        )
    }

    @Composable
    fun clayHighlight(): Brush {
        val palette = LocalSplitzPalette.current
        return Brush.verticalGradient(
            0.0f to palette.clayHighlight,
            0.45f to palette.clayHighlight.copy(alpha = 0.04f),
            1.0f to Color.Transparent
        )
    }

    @Composable
    fun clayInnerShadow(): Brush {
        val palette = LocalSplitzPalette.current
        return Brush.verticalGradient(
            0.0f to Color.Transparent,
            0.7f to palette.clayShadow.copy(alpha = 0.04f),
            1.0f to palette.clayShadow.copy(alpha = 0.35f)
        )
    }

    // Static fallbacks for backward compatibility
    val Primary = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF4A3525),
            Color(0xFFC87D43),
            Color(0xFF4A3525)
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