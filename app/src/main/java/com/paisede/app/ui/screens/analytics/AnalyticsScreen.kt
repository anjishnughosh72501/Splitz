package com.paisede.app.ui.screens.analytics

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.IndigoPurple
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
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
                .background(SplitzColors.background())
        ) {
            if (an.expenseCount == 0) {
                EmptyStateView(
                    title = "No Analytics Available",
                    subtitle = "Add expenses to see spending analytics and breakdown.",
                    icon = Icons.Default.Analytics,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Top Metrics Overview Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(SplitzShapes.cardHero),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle()),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF0F172A),
                                                Color(0xFF1E293B),
                                                Color(0xFF0D252E)
                                            )
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "TOTAL SPENT",
                                        style = SplitzTypography.LabelSmall,
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    MoneyText(
                                        amountPaise = an.totalSpendingPaise,
                                        style = SplitzTypography.MoneyDisplay,
                                        fontWeight = FontWeight.ExtraBold,
                                        colorMode = MoneyColorMode.NEUTRAL,
                                        animate = true
                                    )

                                    Spacer(modifier = Modifier.height(18.dp))
                                    androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
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
                                                text = "EXPENSES COUNT",
                                                style = SplitzTypography.LabelSmall,
                                                color = Color.White.copy(alpha = 0.6f)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "${an.expenseCount}",
                                                style = SplitzTypography.TitleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
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
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(SplitzShapes.MD),
                            colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                an.spendingByCategoryPaise.entries.sortedByDescending { it.value }
                                    .forEach { (cat, amount) ->
                                        val progress = if (an.totalSpendingPaise > 0L) {
                                            amount.toFloat() / an.totalSpendingPaise.toFloat()
                                        } else 0f

                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = cat.replaceFirstChar { it.uppercase() },
                                                    style = SplitzTypography.TitleMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = SplitzColors.textPrimary()
                                                )
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
                                                progress = { progress },
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
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(SplitzShapes.MD),
                            colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
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
                                        val progress = if (an.totalSpendingPaise > 0L) {
                                            amount.toFloat() / an.totalSpendingPaise.toFloat()
                                        } else 0f

                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    UserAvatar(name = name, size = 30)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = name,
                                                        style = SplitzTypography.TitleMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = SplitzColors.textPrimary()
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
                                                progress = { progress },
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
