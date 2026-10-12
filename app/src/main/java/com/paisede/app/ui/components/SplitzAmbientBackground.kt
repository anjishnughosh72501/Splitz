package com.paisede.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import com.paisede.app.data.preferences.SplitzVisualTheme
import com.paisede.app.ui.theme.LocalSplitzPalette
import com.paisede.app.ui.theme.LocalSplitzVisualTheme

/**
 * Modern Programmatic Ambient Mesh Gradient Background
 * Replaces heavy bitmap decoding with an ultra-smooth, hardware-accelerated,
 * theme-adaptive canvas gradient with zero noise/artifacting.
 */
@Composable
fun SplitzAmbientBackground(
    modifier: Modifier = Modifier,
    visualTheme: SplitzVisualTheme = LocalSplitzVisualTheme.current,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val palette = LocalSplitzPalette.current

    val (glowTopRight, glowBottomLeft, baseBg) = remember(palette) {
        when (visualTheme) {
            SplitzVisualTheme.KISSATEN -> Triple(
                Color(0xFFFEDCC5).copy(alpha = 0.45f), // Warm caramel/roasted top-right glow
                Color(0xFFDDE8AB).copy(alpha = 0.30f), // Soft matcha bottom-left glow
                palette.background
            )
            SplitzVisualTheme.GRAPHITE -> Triple(
                Color(0xFF222633),
                Color(0xFF13151D),
                palette.background
            )
            SplitzVisualTheme.ELECTRIC_BLUE -> Triple(
                Color(0xFF132A54),
                Color(0xFF09172E),
                palette.background
            )
            SplitzVisualTheme.ULTRAVIOLET -> Triple(
                Color(0xFF2E134D),
                Color(0xFF1C0A33),
                palette.background
            )
            SplitzVisualTheme.EMERALD -> Triple(
                Color(0xFF0F382C),
                Color(0xFF082019),
                palette.background
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics(mergeDescendants = false) { }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Base ambient fill
            drawRect(color = baseBg)

            // Top-right dynamic ambient glow spot
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowTopRight.copy(alpha = 0.85f), Color.Transparent),
                    center = Offset(width * 0.90f, height * 0.12f),
                    radius = width * 0.95f
                ),
                radius = width * 0.95f,
                center = Offset(width * 0.90f, height * 0.12f)
            )

            // Bottom-left subtle counter-glow spot
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowBottomLeft.copy(alpha = 0.70f), Color.Transparent),
                    center = Offset(width * 0.10f, height * 0.85f),
                    radius = width * 0.85f
                ),
                radius = width * 0.85f,
                center = Offset(width * 0.10f, height * 0.85f)
            )
        }

        // Foreground app content
        content()
    }
}
