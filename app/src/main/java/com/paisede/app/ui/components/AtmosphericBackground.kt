package com.paisede.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import com.paisede.app.data.preferences.SplitzVisualTheme
import com.paisede.app.ui.theme.LocalSplitzPalette
import com.paisede.app.ui.theme.LocalSplitzVisualTheme

/**
 * Reusable atmospheric background composable that renders the active or specified
 * textured artwork with configurable dark overlay, gradient readability treatments,
 * and reliable fallback background color.
 *
 * Designed to ensure foreground content remains 100% accessible, touches are not intercepted,
 * and bitmap decoding is managed cleanly via standard Android resource painters.
 */
@Composable
fun AtmosphericBackground(
    modifier: Modifier = Modifier,
    visualTheme: SplitzVisualTheme = LocalSplitzVisualTheme.current,
    overlayDarkness: Float = 0.55f,
    gradientOverlay: Brush? = null,
    shape: Shape? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val palette = LocalSplitzPalette.current
    val clipModifier = if (shape != null) Modifier.clip(shape) else Modifier

    Box(
        modifier = modifier
            .then(clipModifier)
            .background(palette.background)
            .semantics(mergeDescendants = false) { }
    ) {
        // Layer 1: Textured artwork
        Image(
            painter = painterResource(id = palette.textureResId),
            contentDescription = null, // Decorative layer, do not announce to screen readers
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Layer 2: Darkness overlay for optimal contrast and readability
        if (overlayDarkness > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(palette.background.copy(alpha = overlayDarkness.coerceIn(0f, 1f)))
            )
        }

        // Layer 3: Optional gradient overlay (e.g. hero surface depth / vertical fade)
        if (gradientOverlay != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientOverlay)
            )
        }

        // Layer 4: Foreground content
        content()
    }
}

/**
 * Atmospheric Hero Surface specifically designed for card headers, balances, and dashboards.
 * Pairs the textured artwork with a subtle top highlight and strong readability darkening.
 */
@Composable
fun AtmosphericHeroSurface(
    modifier: Modifier = Modifier,
    shape: Shape,
    borderColor: Color = Color.White.copy(alpha = 0.12f),
    content: @Composable BoxScope.() -> Unit
) {
    val palette = LocalSplitzPalette.current
    val heroGradient = Brush.verticalGradient(
        colors = listOf(
            palette.brandSoft.copy(alpha = 0.45f),
            palette.surface.copy(alpha = 0.85f),
            palette.background.copy(alpha = 0.95f)
        )
    )

    AtmosphericBackground(
        modifier = modifier,
        overlayDarkness = 0.45f,
        gradientOverlay = heroGradient,
        shape = shape,
        content = content
    )
}
