package com.paisede.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.util.CurrencyUtils

/**
 * Hanko Ink Stamp Badge
 * Renders an authentic rotated physical stamp (e.g., "PAID", "SETTLED", "RECORD")
 */
@Composable
fun KissatenHankoStamp(
    text: String,
    modifier: Modifier = Modifier,
    subText: String? = null,
    rotation: Float = -6f,
    stampColor: Color = SplitzColors.negative(),
    isSettled: Boolean = true
) {
    Box(
        modifier = modifier
            .rotate(rotation)
            .border(
                width = 1.5.dp,
                color = stampColor.copy(alpha = 0.85f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(2.dp)
            .border(
                width = 1.dp,
                color = stampColor.copy(alpha = 0.50f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(stampColor.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = stampColor,
                modifier = Modifier.size(14.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = text,
                    style = SplitzTypography.LabelSmall,
                    fontWeight = FontWeight.Black,
                    color = stampColor,
                    letterSpacing = 1.sp
                )
                if (subText != null) {
                    Text(
                        text = subText,
                        style = SplitzTypography.Caption,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = stampColor
                    )
                }
            }
        }
    }
}

/**
 * Receipt Perforation Cutout Divider
 * Simulates a tear-away voucher slip with left & right semicircular punch-outs
 * and a tactile dashed cutting guide in between.
 */
@Composable
fun KissatenPerforationDivider(
    modifier: Modifier = Modifier,
    punchRadius: Dp = 8.dp,
    punchColor: Color = SplitzColors.background(),
    lineColor: Color = SplitzColors.borderSubtle()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        ) {
            drawLine(
                color = lineColor,
                start = Offset(0f, 0.5f),
                end = Offset(size.width, 0.5f),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
            )
        }
    }
}

/**
 * Jagged / Sawtooth Receipt Slip Border (Denpyo teeth pattern)
 */
@Composable
fun KissatenSawtoothTeeth(
    modifier: Modifier = Modifier,
    teethHeight: Dp = 8.dp,
    toothWidth: Dp = 10.dp,
    teethColor: Color = SplitzColors.surfaceElevated(),
    inverted: Boolean = false
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(teethHeight)
    ) {
        val width = size.width
        val height = size.height
        val toothW = toothWidth.toPx()
        val path = Path()

        if (!inverted) {
            // Pointing up: base is at the bottom, peaks at top
            path.moveTo(0f, height)
            var currentX = 0f
            while (currentX < width) {
                val midX = currentX + toothW / 2
                val endX = (currentX + toothW).coerceAtMost(width)
                path.lineTo(midX, 0f)
                path.lineTo(endX, height)
                currentX += toothW
            }
            path.lineTo(width, height)
            path.close()
        } else {
            // Inverted (bottom of receipt): base is at top, peaks at bottom
            path.moveTo(0f, 0f)
            var currentX = 0f
            while (currentX < width) {
                val midX = currentX + toothW / 2
                val endX = (currentX + toothW).coerceAtMost(width)
                path.lineTo(midX, height)
                path.lineTo(endX, 0f)
                currentX += toothW
            }
            path.lineTo(width, 0f)
            path.close()
        }

        drawPath(path = path, color = teethColor)
    }
}

/**
 * Washi Paper Receipt Card (Kissaten Tab Card)
 * Elevated paper surface with crisp borders, perforation guides, and coffee accents.
 */
@Composable
fun KissatenReceiptCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = SplitzColors.surfaceElevated(),
    borderColor: Color = SplitzColors.border(),
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, shape),
        shape = shape,
        color = backgroundColor,
        shadowElevation = 3.dp
    ) {
        Box(modifier = Modifier.padding(SplitzSpacing.md)) {
            content()
        }
    }
}

/**
 * Kissaten Active Trip / Group Capsule Selector
 */
@Composable
fun KissatenGroupSelectorCapsule(
    groupName: String,
    memberCount: Int,
    onSwitchClick: () -> Unit,
    modifier: Modifier = Modifier,
    japaneseSubtitle: String? = null
) {
    val subtitle = japaneseSubtitle ?: "Kohi Split • $memberCount members"
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SplitzShapes.Pill))
            .background(SplitzColors.backgroundElevated())
            .border(1.dp, SplitzColors.borderSubtle(), RoundedCornerShape(SplitzShapes.Pill))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(SplitzColors.brandSoft()),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalCafe,
                    contentDescription = null,
                    tint = SplitzColors.brandPrimary(),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = groupName,
                    style = SplitzTypography.TitleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.brandPrimary(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = SplitzTypography.Caption,
                    color = SplitzColors.brandAccent(),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Switch Button Pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(SplitzShapes.Pill))
                .background(SplitzColors.surfaceElevated())
                .border(1.dp, SplitzColors.borderSubtle(), RoundedCornerShape(SplitzShapes.Pill))
                .clickable { onSwitchClick() }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "SWITCH",
                style = SplitzTypography.LabelSmall,
                fontWeight = FontWeight.Bold,
                color = SplitzColors.textSecondary(),
                fontSize = 11.sp
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Switch group",
                tint = SplitzColors.textSecondary(),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Debt Optimization Pill Banner
 * "Smart ledger cleared X debts to just Y ☕ Plan >"
 */
@Composable
fun KissatenDebtBanner(
    beforeCount: Int,
    afterCount: Int,
    onPlanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SplitzColors.brandSoft().copy(alpha = 0.40f))
            .border(1.dp, SplitzColors.brandAccent().copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .clickable { onPlanClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = SplitzColors.brandAccent(),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (beforeCount > afterCount && afterCount > 0) {
                    "Smart ledger cleared $beforeCount debts to just $afterCount ☕"
                } else {
                    "Smart ledger optimization active • Kohi Split"
                },
                style = SplitzTypography.BodySmall,
                fontWeight = FontWeight.Medium,
                color = SplitzColors.brandPrimary(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Plan",
                style = SplitzTypography.LabelSmall,
                fontWeight = FontWeight.Bold,
                color = SplitzColors.brandAccent()
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = SplitzColors.brandAccent(),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

/**
 * Kissaten Member Balance Card for the horizontal carousel
 */
@Composable
fun KissatenMemberBalanceCard(
    name: String,
    netBalancePaise: Long,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val currentCurrency = com.paisede.app.ui.theme.LocalAppCurrency.current
    val isPositive = netBalancePaise > 0
    val isNegative = netBalancePaise < 0
    val isZero = netBalancePaise == 0L

    val amountColor = when {
        isPositive -> SplitzColors.positive()
        isNegative -> SplitzColors.negative()
        else -> SplitzColors.textMuted()
    }

    val statusText = when {
        isPositive -> "gets back"
        isNegative -> "owes tab"
        else -> "settled"
    }

    val statusColor = when {
        isPositive -> SplitzColors.positive()
        isNegative -> SplitzColors.negative()
        else -> SplitzColors.textMuted()
    }

    Surface(
        modifier = modifier
            .width(132.dp)
            .border(1.dp, SplitzColors.borderSubtle(), RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        color = SplitzColors.surfaceElevated(),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with "You" pill
            Box(contentAlignment = Alignment.BottomEnd) {
                UserAvatar(name = name, size = 44)
                if (isCurrentUser) {
                    Box(
                        modifier = Modifier
                            .offset(x = 4.dp, y = 4.dp)
                            .clip(RoundedCornerShape(SplitzShapes.Pill))
                            .background(Color(0xFFFEDCC5))
                            .border(1.dp, Color(0xFF4A3525).copy(alpha = 0.2f), RoundedCornerShape(SplitzShapes.Pill))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "You",
                            style = SplitzTypography.Caption,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF301400)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = name,
                style = SplitzTypography.TitleSmall,
                fontWeight = FontWeight.Bold,
                color = SplitzColors.brandPrimary(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = CurrencyUtils.formatPaise(
                    paise = netBalancePaise,
                    showSign = true,
                    currency = currentCurrency
                ),
                style = SplitzTypography.MoneySmall,
                fontWeight = FontWeight.Bold,
                color = amountColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = statusText,
                style = SplitzTypography.Caption,
                fontSize = 11.sp,
                color = statusColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * 52dp Tactile Kissaten Pill Button
 */
@Composable
fun KissatenPrimaryPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subText: String? = null,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.98f else 1.0f,
        animationSpec = tween(150),
        label = "PillButtonScale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .height(52.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        shape = RoundedCornerShape(SplitzShapes.Pill),
        color = if (enabled) SplitzColors.brandPrimary() else SplitzColors.borderSubtle(),
        shadowElevation = if (enabled) 3.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SplitzColors.onPrimary(),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = text,
                    style = SplitzTypography.TitleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.onPrimary()
                )
                if (subText != null) {
                    Text(
                        text = subText,
                        style = SplitzTypography.Caption,
                        fontSize = 10.sp,
                        color = Color(0xFFFEDCC5)
                    )
                }
            }
        }
    }
}

/**
 * Warm Floating Sticky Drawer for Adding Expense
 * Matches Stitch: "Add Expense • Split a new bill" + "Scan" badge
 */
@Composable
fun KissatenAddExpenseStickyDrawer(
    onAddExpenseClick: () -> Unit,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = tween(120),
        label = "DrawerScale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .height(54.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onAddExpenseClick
            ),
        shape = RoundedCornerShape(SplitzShapes.Pill),
        color = SplitzColors.brandPrimary(),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SplitzColors.brandSoft()),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = SplitzColors.brandPrimary(),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Add Expense",
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.onPrimary()
                    )
                    Text(
                        text = "Split a new bill",
                        style = SplitzTypography.Caption,
                        fontSize = 10.sp,
                        color = SplitzColors.onPrimary().copy(alpha = 0.75f)
                    )
                }
            }

            // Scan Receipt Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(SplitzShapes.Pill))
                    .background(SplitzColors.brandAccent().copy(alpha = 0.25f))
                    .clickable { onScanClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Scan",
                    tint = SplitzColors.onPrimary(),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Scan",
                    style = SplitzTypography.LabelSmall,
                    color = SplitzColors.onPrimary(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Kissaten Bottom Navigation Bar
 * 4 Tabs: Groups (coffee), Settle Up (balance), Activity (receipt_long), My Ledger (menu_book)
 */
enum class KissatenNavTab(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
) {
    GROUPS("Groups", "Groups", Icons.Default.Coffee),
    SETTLE_UP("Settle Up", "Settle", Icons.Default.Balance),
    ACTIVITY("Activity", "Activity", Icons.AutoMirrored.Filled.ReceiptLong),
    MY_LEDGER("My Ledger", "Ledger", Icons.Default.MenuBook)
}

@Composable
fun KissatenBottomBar(
    currentTab: KissatenNavTab,
    onTabSelected: (KissatenNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = SplitzColors.surfaceElevated(),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KissatenNavTab.entries.forEach { tab ->
                val isSelected = tab == currentTab

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(SplitzShapes.Pill))
                            .then(
                                if (isSelected) Modifier
                                    .background(SplitzColors.brandSoft())
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                else Modifier
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) SplitzColors.brandPrimary() else SplitzColors.textMuted(),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = tab.title,
                        style = SplitzTypography.Caption,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) SplitzColors.brandPrimary() else SplitzColors.textMuted()
                    )
                }
            }
        }
    }
}
