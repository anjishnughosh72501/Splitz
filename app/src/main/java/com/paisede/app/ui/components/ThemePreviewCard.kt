package com.paisede.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.data.preferences.SplitzVisualTheme
import com.paisede.app.ui.theme.SplitzBorderWidth
import com.paisede.app.ui.theme.SplitzMotion
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.theme.ThemePalettes

/**
 * Interactive card displaying a live preview of one of the three textured visual themes.
 * Shows the actual bundled artwork, theme palette chips, title, description, and selected state.
 * Fully accessible with screen readers.
 */
@Composable
fun ThemePreviewCard(
    theme: SplitzVisualTheme,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = ThemePalettes.forTheme(theme)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1.0f,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "ThemePreviewScale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) palette.brandPrimary else palette.borderSubtle,
        animationSpec = tween(SplitzMotion.Duration.Fast),
        label = "ThemePreviewBorder"
    )

    val accessibilityDesc = "${theme.displayName} theme, ${if (isSelected) "selected" else "not selected"}. ${theme.description}"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(SplitzShapes.MD))
            .background(palette.surfaceElevated)
            .border(
                width = if (isSelected) SplitzBorderWidth.medium else SplitzBorderWidth.thin,
                color = borderColor,
                shape = RoundedCornerShape(SplitzShapes.MD)
            )
            .semantics {
                this.role = Role.RadioButton
                this.selected = isSelected
                this.contentDescription = accessibilityDesc
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onSelect
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Artwork banner preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(RoundedCornerShape(topStart = SplitzShapes.MD, topEnd = SplitzShapes.MD))
            ) {
                Image(
                    painter = painterResource(id = palette.textureResId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Readability overlay with theme glow
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    palette.surfaceElevated.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Selection badge at top right
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(palette.brandPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = palette.onBrandPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Mini financial metric preview on artwork
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹12,450",
                        style = SplitzTypography.MoneySmall,
                        fontWeight = FontWeight.Bold,
                        color = palette.textPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "+₹3,200",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.Bold,
                        color = palette.positive
                    )
                }
            }

            // Info & Color Swatches
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = theme.displayName,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = palette.textPrimary
                    )

                    // Representative color swatches
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ColorSwatch(color = palette.brandPrimary, tooltip = "Brand")
                        ColorSwatch(color = palette.surfaceElevated, tooltip = "Surface")
                        ColorSwatch(color = palette.positive, tooltip = "Positive")
                        ColorSwatch(color = palette.negative, tooltip = "Negative")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = theme.description,
                    style = SplitzTypography.BodySmall,
                    color = palette.textSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    color: Color,
    tooltip: String
) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(color)
            .border(0.5.dp, Color.White.copy(alpha = 0.25f), CircleShape)
    )
}
