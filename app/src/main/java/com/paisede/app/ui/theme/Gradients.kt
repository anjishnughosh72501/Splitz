package com.paisede.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object SplitzGradients {
    val Primary = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF0D9488),
            Color(0xFF0F766E),
            Color(0xFF0D9488)
        ),
        startX = 0.0f,
        endX = 1.0f,
        tileMode = androidx.compose.ui.graphics.TileMode.Clamp
    )

    val PrimaryBright = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF14B8A6),
            Color(0xFF0D9488),
            Color(0xFF14B8A6)
        ),
        startX = 0.0f,
        endX = 1.0f,
        tileMode = androidx.compose.ui.graphics.TileMode.Clamp
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
            Color(0xFF0F172A),
            Color(0xFF1E293B),
            Color(0xFF0F172A)
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

    val DarkAtmosphereTop = Brush.radialGradient(
        colors = listOf(
            Color(0xFF0D9488).copy(alpha = 0.15f),
            Color.Transparent
        )
    )

    val DarkAtmosphereBottom = Brush.radialGradient(
        colors = listOf(
            Color(0xFF6366F1).copy(alpha = 0.1f),
            Color.Transparent
        )
    )

    val LightAtmosphereTop = Brush.radialGradient(
        colors = listOf(
            Color(0xFF0D9488).copy(alpha = 0.08f),
            Color.Transparent
        )
    )

    val LightAtmosphereBottom = Brush.radialGradient(
        colors = listOf(
            Color(0xFF6366F1).copy(alpha = 0.05f),
            Color.Transparent
        )
    )
}