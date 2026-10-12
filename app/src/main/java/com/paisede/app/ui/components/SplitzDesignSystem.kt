package com.paisede.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paisede.app.ui.theme.SplitzBorderWidth
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzElevation
import com.paisede.app.ui.theme.SplitzGradients
import com.paisede.app.ui.theme.SplitzMotion
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing

/**
 * Base Splitz surface primitive supporting tactile press animation, subtle border,
 * glass/elevated backgrounds, and custom elevation.
 */
@Composable
fun SplitzSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(SplitzShapes.cardMedium),
    backgroundColor: Color = SplitzColors.surfaceElevated().copy(alpha = 0.85f),
    borderColor: Color? = SplitzColors.borderGlass(),
    borderWidth: Dp = SplitzBorderWidth.thin,
    elevation: Dp = SplitzElevation.Level0,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null && enabled) 0.98f else 1.0f,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "SplitzSurfaceScale"
    )

    val borderModifier = if (borderColor != null) {
        Modifier.border(borderWidth, borderColor, shape)
    } else Modifier

    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
    } else Modifier

    Surface(
        modifier = modifier
            .scale(scale)
            .then(borderModifier)
            .then(clickModifier),
        shape = shape,
        color = backgroundColor,
        shadowElevation = elevation
    ) {
        Box(modifier = Modifier) {
            content()
        }
    }
}

/**
 * Tactical GlassCard: semi-transparent, subtle glass border, optional highlight shimmer/gradient.
 */
@Composable
fun SplitzGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = SplitzShapes.cardLarge,
    tint: Color = SplitzColors.surfaceGlass(),
    borderColor: Color = SplitzColors.borderGlass(),
    elevation: Dp = SplitzElevation.Level1,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.982f else 1.0f,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "GlassCardScale"
    )

    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(tint)
            .border(SplitzBorderWidth.thin, borderColor, shape)
            .then(clickModifier)
    ) {
        // Soft top highlight for realistic frosted glass refraction
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(SplitzGradients.GlassHighlight)
        )
        Box(modifier = Modifier.padding(SplitzSpacing.md)) {
            content()
        }
    }
}

/**
 * Claymorphic Card Primitive:
 * Delivers signature soft tactile inflation, top light refraction,
 * and deep perimeter shadow for hero elements.
 */
@Composable
fun SplitzClayCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = SplitzShapes.cardHero,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 12.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val palette = com.paisede.app.ui.theme.LocalSplitzPalette.current
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.982f else 1.0f,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "ClayCardScale"
    )

    val surfaceColor = backgroundColor ?: palette.claySurface
    val borderCol = borderColor ?: palette.borderGlass

    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else Modifier

    Surface(
        modifier = modifier
            .scale(scale)
            .then(clickModifier),
        shape = shape,
        color = surfaceColor,
        shadowElevation = elevation
    ) {
        Box(
            modifier = Modifier
                .border(SplitzBorderWidth.thin, borderCol, shape)
        ) {
            // Soft top refraction highlight
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(SplitzGradients.clayHighlight())
            )
            // Soft bottom clay ambient shadow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(SplitzGradients.clayInnerShadow())
            )

            Box(modifier = Modifier.padding(SplitzSpacing.lg)) {
                content()
            }
        }
    }
}

/**
 * ElevatedCard: Rich solid surface with refined subtle border and elevation.
 */
@Composable
fun SplitzElevatedCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = SplitzShapes.cardMedium,
    backgroundColor: Color = SplitzColors.surfaceElevated(),
    elevation: Dp = SplitzElevation.Level2,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    SplitzSurface(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        backgroundColor = backgroundColor,
        borderColor = SplitzColors.borderSubtle(),
        borderWidth = SplitzBorderWidth.thin,
        elevation = elevation,
        onClick = onClick,
        content = {
            Box(modifier = Modifier.padding(SplitzSpacing.md)) {
                content()
            }
        }
    )
}

/**
 * GradientCard: Hero card displaying a subtle gradient with high-contrast text.
 */
@Composable
fun SplitzGradientCard(
    modifier: Modifier = Modifier,
    brush: Brush = SplitzGradients.Primary,
    cornerRadius: Dp = SplitzShapes.cardHero,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.985f else 1.0f,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "GradientCardScale"
    )

    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(brush)
            .then(clickModifier)
    ) {
        // Soft top highlight
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(SplitzGradients.GlassHighlight)
        )
        Box(modifier = Modifier.padding(SplitzSpacing.lg)) {
            content()
        }
    }
}

/**
 * MetricCard: Fintech financial metric presentation card with title, value, helper, and optional trend icon.
 */
@Composable
fun SplitzMetricCard(
    label: String,
    valueContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    accentColor: Color = SplitzColors.primary(),
    onClick: (() -> Unit)? = null
) {
    SplitzGlassCard(
        modifier = modifier,
        cornerRadius = SplitzShapes.cardMedium,
        onClick = onClick
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(SplitzShapes.XS))
                            .background(accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(SplitzSpacing.xs))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = SplitzColors.textSecondary(),
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(SplitzSpacing.xs))
            valueContent()
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(SplitzSpacing.xxs))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = SplitzColors.textMuted()
                )
            }
        }
    }
}

/**
 * ActionCard: Modern tactile action button/card used for secondary and tertiary operations.
 */
@Composable
fun SplitzActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    iconTint: Color = SplitzColors.primary(),
    containerColor: Color = SplitzColors.surfaceElevated().copy(alpha = 0.85f),
    badgeText: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "ActionCardScale"
    )

    val shape = RoundedCornerShape(SplitzShapes.cardMedium)

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(containerColor)
            .border(SplitzBorderWidth.thin, SplitzColors.borderSubtle(), shape)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(SplitzSpacing.md)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(SplitzShapes.SM))
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (badgeText != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(SplitzShapes.Pill))
                            .background(iconTint.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = iconTint,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(SplitzSpacing.sm))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = SplitzColors.textPrimary(),
                fontWeight = FontWeight.SemiBold
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = SplitzColors.textMuted()
                )
            }
        }
    }
}
