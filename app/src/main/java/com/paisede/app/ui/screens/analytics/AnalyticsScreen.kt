package com.paisede.app.ui.screens.analytics

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzSurface
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.IndigoPurple
import com.paisede.app.ui.theme.SplitzBorderWidth
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzGradients
import com.paisede.app.ui.theme.SplitzMotion
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.AnalyticsViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val an = state.analytics

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SplitzTopAppBar(
                title = "Spending Analytics",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (an.expenseCount == 0) {
                EmptyStateView(
                    title = "No Analytics Available",
                    subtitle = "Add expenses to see detailed category trends, breakdown, and insights.",
                    icon = Icons.Default.Analytics,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(SplitzSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(SplitzSpacing.md)
                ) {
                    // Hero Spending Overview Card
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(SplitzShapes.cardHero))
                                .background(SplitzColors.surfaceElevated().copy(alpha = 0.85f))
                                .border(
                                    width = SplitzBorderWidth.thin,
                                    color = SplitzColors.borderGlass(),
                                    shape = RoundedCornerShape(SplitzShapes.cardHero)
                                )
                                .padding(22.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "TOTAL SPENDING",
                                            style = SplitzTypography.LabelSmall,
                                            color = Color.White.copy(alpha = 0.6f),
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 1.2.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        MoneyText(
                                            amountPaise = an.totalSpendingPaise,
                                            style = SplitzTypography.MoneyDisplay,
                                            fontWeight = FontWeight.ExtraBold,
                                            colorMode = MoneyColorMode.NEUTRAL,
                                            animate = true
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(RoundedCornerShape(SplitzShapes.SM))
                                            .background(Color.White.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.TrendingUp,
                                            contentDescription = null,
                                            tint = SplitzColors.primaryBright(),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))
                                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "AVERAGE EXPENSE",
                                            style = SplitzTypography.LabelSmall,
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        MoneyText(
                                            amountPaise = an.averageExpensePaise,
                                            style = SplitzTypography.TitleMedium,
                                            fontWeight = FontWeight.Bold,
                                            colorMode = MoneyColorMode.NEUTRAL,
                                            animate = true
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "TOTAL EXPENSES",
                                            style = SplitzTypography.LabelSmall,
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${an.expenseCount} entries",
                                            style = SplitzTypography.TitleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Spending by Category
                    item {
                        Text(
                            text = "SPENDING BY CATEGORY",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textMuted()
                        )
                    }

                    item {
                        SplitzSurface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(SplitzShapes.cardMedium),
                            backgroundColor = SplitzColors.surface(),
                            borderColor = SplitzColors.borderSubtle()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(SplitzSpacing.md),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                an.spendingByCategoryPaise.entries.sortedByDescending { it.value }
                                    .forEach { (cat, amount) ->
                                        val targetProgress = if (an.totalSpendingPaise > 0L) {
                                            amount.toFloat() / an.totalSpendingPaise.toFloat()
                                        } else 0f

                                        val animatedProgress by animateFloatAsState(
                                            targetValue = targetProgress,
                                            animationSpec = tween(SplitzMotion.Duration.Slow, easing = SplitzMotion.Easing.Decelerated),
                                            label = "CategoryProgress"
                                        )

                                        val pct = (targetProgress * 100).toInt()

                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = cat.replaceFirstChar { it.uppercase() },
                                                        style = SplitzTypography.TitleMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = SplitzColors.textPrimary()
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "($pct%)",
                                                        style = SplitzTypography.BodySmall,
                                                        color = SplitzColors.textMuted()
                                                    )
                                                }
                                                MoneyText(
                                                    amountPaise = amount,
                                                    style = SplitzTypography.TitleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    colorMode = MoneyColorMode.NEUTRAL,
                                                    animate = true
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            LinearProgressIndicator(
                                                progress = { animatedProgress },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(8.dp)
                                                    .clip(RoundedCornerShape(SplitzShapes.Pill)),
                                                color = SplitzColors.primary(),
                                                trackColor = SplitzColors.surfaceElevated()
                                            )
                                        }
                                    }
                            }
                        }
                    }

                    // Spending by Person
                    item {
                        Text(
                            text = "SPENDING BY PERSON (CONSUMED)",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textMuted()
                        )
                    }

                    item {
                        SplitzSurface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(SplitzShapes.cardMedium),
                            backgroundColor = SplitzColors.surface(),
                            borderColor = SplitzColors.borderSubtle()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(SplitzSpacing.md),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                an.spendingByPersonPaise.entries.sortedByDescending { it.value }
                                    .forEach { (userId, amount) ->
                                        val name = state.members[userId]?.name ?: "Member"
                                        val targetProgress = if (an.totalSpendingPaise > 0L) {
                                            amount.toFloat() / an.totalSpendingPaise.toFloat()
                                        } else 0f

                                        val animatedProgress by animateFloatAsState(
                                            targetValue = targetProgress,
                                            animationSpec = tween(SplitzMotion.Duration.Slow, easing = SplitzMotion.Easing.Decelerated),
                                            label = "PersonProgress"
                                        )

                                        val pct = (targetProgress * 100).toInt()

                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    UserAvatar(name = name, size = 30)
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = name,
                                                        style = SplitzTypography.TitleMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = SplitzColors.textPrimary()
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "($pct%)",
                                                        style = SplitzTypography.BodySmall,
                                                        color = SplitzColors.textMuted()
                                                    )
                                                }
                                                MoneyText(
                                                    amountPaise = amount,
                                                    style = SplitzTypography.TitleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    colorMode = MoneyColorMode.NEUTRAL,
                                                    animate = true
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            LinearProgressIndicator(
                                                progress = { animatedProgress },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(8.dp)
                                                    .clip(RoundedCornerShape(SplitzShapes.Pill)),
                                                color = IndigoPurple,
                                                trackColor = SplitzColors.surfaceElevated()
                                            )
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}
