package com.paisede.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.theme.SplitzBorderWidth
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzElevation
import com.paisede.app.ui.theme.SplitzGradients
import com.paisede.app.ui.theme.SplitzMotion
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import kotlin.math.abs

@Composable
fun SplitzTopAppBar(
    title: String = "",
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onNavigateBack: (() -> Unit)? = null,
    backButtonText: String = "Back",
    centerTitle: Boolean = true,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    titleContent: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = SplitzColors.surfaceElevated().copy(alpha = 0.96f),
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, SplitzColors.borderSubtle().copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            if (centerTitle) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Leading navigation slot
                    Box(
                        modifier = Modifier.align(Alignment.CenterStart),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (navigationIcon != null) {
                            navigationIcon()
                        } else if (onNavigateBack != null) {
                            Surface(
                                shape = RoundedCornerShape(SplitzShapes.Pill),
                                color = SplitzColors.surface(),
                                border = BorderStroke(1.dp, SplitzColors.borderSubtle()),
                                modifier = Modifier.clickable(onClick = onNavigateBack)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = backButtonText,
                                        tint = SplitzColors.textPrimary(),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = backButtonText,
                                        style = SplitzTypography.LabelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textPrimary()
                                    )
                                }
                            }
                        }
                    }

                    // Center title & subtitle slot
                    if (titleContent != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            titleContent()
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (title.isNotBlank()) {
                                Text(
                                    text = title,
                                    style = SplitzTypography.TitleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SplitzColors.textPrimary(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                            if (subtitle != null) {
                                Text(
                                    text = subtitle,
                                    style = SplitzTypography.LabelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SplitzColors.brandAccent(),
                                    letterSpacing = 1.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Trailing actions slot
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        content = actions
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (navigationIcon != null) {
                        navigationIcon()
                        Spacer(modifier = Modifier.width(8.dp))
                    } else if (onNavigateBack != null) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = backButtonText,
                                tint = SplitzColors.textPrimary()
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (titleContent != null) {
                            titleContent()
                        } else {
                            Column {
                                if (title.isNotBlank()) {
                                    Text(
                                        text = title,
                                        style = SplitzTypography.TitleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textPrimary(),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (subtitle != null) {
                                    Text(
                                        text = subtitle,
                                        style = SplitzTypography.BodySmall,
                                        color = SplitzColors.textSecondary(),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        content = actions
                    )
                }
            }
        }
    }
}

@Composable
fun PaiseDeTopAppBar(
    title: String,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    SplitzTopAppBar(title = title, onNavigateBack = onNavigateBack, actions = actions)
}

@Composable
fun EmptyStateView(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    val colors = SplitzColors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SplitzSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceGlass())
                    .border(SplitzBorderWidth.thin, colors.borderGlass(), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Glow behind icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colors.primary().copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.primary(),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(SplitzSpacing.md))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(SplitzSpacing.xs))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary(),
            textAlign = TextAlign.Center
        )

        if (actionButtonText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(SplitzSpacing.xl))
            SplitzPrimaryButton(
                text = actionButtonText,
                onClick = onActionClick
            )
        }
    }
}

private val avatarColors = listOf(
    Color(0xFF0D9488), // Teal
    Color(0xFF6366F1), // Indigo
    Color(0xFF8B5CF6), // Violet
    Color(0xFFEC4899), // Pink
    Color(0xFFF59E0B), // Amber
    Color(0xFF10B981), // Emerald
    Color(0xFF3B82F6), // Blue
    Color(0xFFF97316)  // Orange
)

fun getDeterministicAvatarColor(name: String): Color {
    val hash = abs(name.hashCode())
    return avatarColors[hash % avatarColors.size]
}

@Composable
fun UserAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Int = 40,
    backgroundColor: Color? = null,
    textColor: Color? = null,
    isSelected: Boolean = false,
    showRing: Boolean = false
) {
    val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    val themeColors = SplitzColors
    val seedColor = getDeterministicAvatarColor(name)
    val bg = backgroundColor ?: seedColor.copy(alpha = 0.16f)
    val tc = textColor ?: (if (themeColors.isDark()) seedColor.copy(alpha = 0.9f) else seedColor)

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .then(
                if (isSelected || showRing) {
                    Modifier.border(2.dp, themeColors.primary(), CircleShape)
                } else {
                    Modifier.border(SplitzBorderWidth.thin, themeColors.borderGlass(), CircleShape)
                }
            )
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = if (size < 32) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelLarge,
            color = tc,
            fontWeight = FontWeight.Bold
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(themeColors.primary().copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = themeColors.onPrimary(),
                    modifier = Modifier.size((size * 0.45f).dp)
                )
            }
        }
    }
}

@Composable
fun AvatarStack(
    names: List<String>,
    modifier: Modifier = Modifier,
    avatarSize: Int = 28,
    maxVisible: Int = 4
) {
    val visible = names.take(maxVisible)
    val remaining = names.size - maxVisible

    val overlap = (avatarSize * 0.28f).dp
    val totalCount = visible.size + if (remaining > 0) 1 else 0
    val totalWidth = if (totalCount == 0) 0.dp else (avatarSize.dp + overlap * (totalCount - 1))

    Box(
        modifier = modifier.size(width = totalWidth, height = avatarSize.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        visible.forEachIndexed { index, name ->
            Box(
                modifier = Modifier
                    .offset(x = overlap * index)
                    .border(1.5.dp, SplitzColors.background(), CircleShape)
            ) {
                UserAvatar(
                    name = name,
                    size = avatarSize
                )
            }
        }
        if (remaining > 0) {
            Box(
                modifier = Modifier
                    .offset(x = overlap * visible.size)
                    .size(avatarSize.dp)
                    .clip(CircleShape)
                    .background(SplitzColors.surfaceElevated())
                    .border(1.5.dp, SplitzColors.background(), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$remaining",
                    style = MaterialTheme.typography.labelSmall,
                    color = SplitzColors.textSecondary(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SplitzSpacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = SplitzColors.textSecondary(),
            fontWeight = FontWeight.SemiBold
        )

        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelMedium,
                color = SplitzColors.primary(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

/**
 * Modern Segmented Control with animated sliding pill indicator
 */
@Composable
fun SplitzSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SplitzColors
    val shape = RoundedCornerShape(SplitzShapes.SM)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surfaceGlass())
            .border(SplitzBorderWidth.thin, colors.borderSubtle(), shape)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, text ->
                val isSelected = index == selectedIndex
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(SplitzShapes.XS))
                        .then(
                            if (isSelected) {
                                Modifier
                                    .background(colors.primary())
                                    .border(SplitzBorderWidth.thin, colors.primaryBright().copy(alpha = 0.5f), RoundedCornerShape(SplitzShapes.XS))
                            } else Modifier
                        )
                        .clickable(interactionSource = interactionSource, indication = null) {
                            onItemSelected(index)
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) colors.onPrimary() else colors.textSecondary()
                    )
                }
            }
        }
    }
}

/**
 * Splitz Primary Gradient Button with tactile press animation
 */
@Composable
fun SplitzPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    brush: Brush = SplitzGradients.Primary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.97f else 1.0f,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "PrimaryButtonScale"
    )

    val shape = RoundedCornerShape(SplitzShapes.SM)

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .then(
                if (enabled) Modifier.background(brush)
                else Modifier.background(SplitzColors.surfaceVariant().copy(alpha = 0.5f))
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = SplitzSpacing.lg, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) SplitzColors.onPrimary() else SplitzColors.textMuted(),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(SplitzSpacing.xs))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (enabled) SplitzColors.onPrimary() else SplitzColors.textMuted()
            )
        }
    }
}

/**
 * Splitz Secondary Glass Button
 */
@Composable
fun SplitzSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.97f else 1.0f,
        animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
        label = "SecondaryButtonScale"
    )

    val shape = RoundedCornerShape(SplitzShapes.SM)

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(SplitzColors.surfaceGlass())
            .border(SplitzBorderWidth.thin, SplitzColors.borderGlass(), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = SplitzSpacing.md, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SplitzColors.textPrimary(),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(SplitzSpacing.xs))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = SplitzColors.textPrimary()
            )
        }
    }
}

/**
 * Splitz 2.0 Design System Surface Components
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = SplitzShapes.cardMedium,
    content: @Composable () -> Unit
) {
    SplitzGlassCard(
        modifier = modifier,
        cornerRadius = cornerRadius,
        content = { content() }
    )
}

@Composable
fun GlassCardStrong(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = SplitzShapes.cardLarge,
    content: @Composable () -> Unit
) {
    SplitzGlassCard(
        modifier = modifier,
        cornerRadius = cornerRadius,
        tint = SplitzColors.surfaceGlassStrong(),
        content = { content() }
    )
}

@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier,
    height: Dp = 100.dp,
    cornerRadius: Dp = SplitzShapes.cardMedium
) {
    val transition = androidx.compose.animation.core.rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(durationMillis = 1200, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "ShimmerTranslate"
    )

    val shimmerColors = listOf(
        SplitzColors.surfaceElevated().copy(alpha = 0.5f),
        SplitzColors.surfaceElevated().copy(alpha = 0.85f),
        SplitzColors.surfaceElevated().copy(alpha = 0.5f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset.Zero,
        end = androidx.compose.ui.geometry.Offset(x = translateAnim, y = translateAnim)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
            .border(SplitzBorderWidth.thin, SplitzColors.borderSubtle().copy(alpha = 0.4f), RoundedCornerShape(cornerRadius))
    )
}
