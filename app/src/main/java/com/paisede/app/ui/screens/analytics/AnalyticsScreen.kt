package com.paisede.app.ui.screens.analytics

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.KissatenTokens
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzMotion
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.AnalyticsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val an = state.analytics
    var selectedLedgerTab by remember { mutableStateOf(0) } // 0 = Group Ledger, 1 = Personal Share

    val currentMonth = remember {
        SimpleDateFormat("MMMM yyyy", Locale.US).format(Date())
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            // Kissaten Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(SplitzShapes.Pill),
                    color = SplitzColors.surfaceElevated().copy(alpha = 0.9f),
                    border = BorderStroke(1.dp, SplitzColors.borderSubtle()),
                    modifier = Modifier.clickable { onNavigateBack() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = SplitzColors.textPrimary(),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Back",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Spending Analytics",
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Text(
                        text = "LEDGER & BUDGET",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SplitzColors.brandAccent(),
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(SplitzShapes.Pill),
                    color = SplitzColors.surfaceElevated(),
                    border = BorderStroke(1.dp, SplitzColors.borderSubtle())
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = SplitzColors.brandAccent(),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentMonth,
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (an.expenseCount == 0) {
                KissatenReceiptCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = null,
                            tint = SplitzColors.brandAccent(),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No expenses recorded yet",
                            style = SplitzTypography.TitleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Log expenses to view category breakdowns and member consumption analytics.",
                            style = SplitzTypography.BodySmall,
                            color = SplitzColors.textMuted(),
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
            } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Hero Ledger Overview Card (Washi Paper aesthetic)
                        item {
                            KissatenReceiptCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "GROUP TOTAL SPENT",
                                                style = SplitzTypography.LabelSmall,
                                                color = SplitzColors.textMuted(),
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.8.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            MoneyText(
                                                amountPaise = an.totalSpendingPaise,
                                                style = SplitzTypography.DisplayMedium,
                                                fontWeight = FontWeight.Black,
                                                colorMode = MoneyColorMode.NEUTRAL,
                                                animate = true
                                            )
                                        }

                                        KissatenHankoStamp(text = "ANALYTICS")
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                    KissatenPerforationDivider()
                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "AVERAGE EXPENSE",
                                                style = SplitzTypography.LabelSmall,
                                                color = SplitzColors.textMuted()
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
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
                                                color = SplitzColors.textMuted()
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "${an.expenseCount} entries",
                                                style = SplitzTypography.TitleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = SplitzColors.textPrimary()
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Category Breakdown Section
                        item {
                            Text(
                                text = "CATEGORY BREAKDOWN",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary(),
                                letterSpacing = 0.8.sp
                            )
                        }

                        item {
                            KissatenReceiptCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
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
                                                            style = SplitzTypography.BodyLarge,
                                                            fontWeight = FontWeight.Bold,
                                                            color = SplitzColors.textPrimary()
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Surface(
                                                            shape = RoundedCornerShape(SplitzShapes.Pill),
                                                            color = SplitzColors.brandSoft()
                                                        ) {
                                                            Text(
                                                                text = "$pct%",
                                                                style = SplitzTypography.LabelSmall,
                                                                fontWeight = FontWeight.Bold,
                                                                color = SplitzColors.brandPrimary(),
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    }
                                                    MoneyText(
                                                        amountPaise = amount,
                                                        style = SplitzTypography.BodyLarge,
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
                                                        .height(7.dp)
                                                        .clip(RoundedCornerShape(SplitzShapes.Pill)),
                                                    color = SplitzColors.brandAccent(),
                                                    trackColor = SplitzColors.borderSubtle()
                                                )
                                            }
                                        }
                                }
                            }
                        }

                        // Spending by Member (Consumed)
                        item {
                            Text(
                                text = "SPENDING BY PARTICIPANT",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary(),
                                letterSpacing = 0.8.sp
                            )
                        }

                        item {
                            KissatenReceiptCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
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
                                                        UserAvatar(name = name, size = 28)
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            text = name,
                                                            style = SplitzTypography.BodyLarge,
                                                            fontWeight = FontWeight.Bold,
                                                            color = SplitzColors.textPrimary()
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Surface(
                                                            shape = RoundedCornerShape(SplitzShapes.Pill),
                                                            color = SplitzColors.positiveContainer()
                                                        ) {
                                                            Text(
                                                                text = "$pct%",
                                                                style = SplitzTypography.LabelSmall,
                                                                fontWeight = FontWeight.Bold,
                                                                color = SplitzColors.positive(),
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    }
                                                    MoneyText(
                                                        amountPaise = amount,
                                                        style = SplitzTypography.BodyLarge,
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
                                                        .height(7.dp)
                                                        .clip(RoundedCornerShape(SplitzShapes.Pill)),
                                                    color = SplitzColors.positive(),
                                                    trackColor = SplitzColors.borderSubtle()
                                                )
                                            }
                                        }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
