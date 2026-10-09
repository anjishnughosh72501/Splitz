package com.paisede.app.ui.screens.simplify

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.paisede.app.domain.algorithm.DebtEdge
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.GraphVisualizer
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzSegmentedControl
import com.paisede.app.ui.components.SplitzSurface
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.SplitzBorderWidth
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzGradients
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.SimplifyViewModel

@Composable
fun SimplifyDebtsScreen(
    viewModel: SimplifyViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedViewTab by remember { mutableStateOf(0) } // 0 = List, 1 = Graph

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SplitzTopAppBar(
                title = "Simplify Debts",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Transformation Metric Card: Before vs After
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SplitzSpacing.lg)
                    .clip(RoundedCornerShape(SplitzShapes.cardHero))
                    .background(SplitzColors.surfaceElevated().copy(alpha = 0.85f))
                    .border(
                        width = SplitzBorderWidth.thin,
                        color = SplitzColors.borderGlass(),
                        shape = RoundedCornerShape(SplitzShapes.cardHero)
                    )
                    .padding(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "DEBT OPTIMIZATION",
                        style = SplitzTypography.LabelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "BEFORE",
                                style = SplitzTypography.LabelSmall,
                                color = DebtRed,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${state.beforeCount}",
                                style = SplitzTypography.DisplayMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "debts",
                                style = SplitzTypography.BodySmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Optimized into",
                            tint = SplitzColors.primaryBright(),
                            modifier = Modifier.size(32.dp)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "AFTER",
                                style = SplitzTypography.LabelSmall,
                                color = CreditGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${state.afterCount}",
                                style = SplitzTypography.DisplayMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "transfers",
                                style = SplitzTypography.BodySmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(10.dp))

                    val reduction = if (state.beforeCount > state.afterCount) state.beforeCount - state.afterCount else 0
                    Text(
                        text = if (reduction > 0) "Eliminated $reduction redundant transaction(s)" else "All debts are in minimal form",
                        style = SplitzTypography.BodySmall,
                        color = SplitzColors.primaryBright(),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Algorithm Strategy Switcher
            if (state.isExactAvailable && state.exactCount != null) {
                SplitzSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SplitzSpacing.lg, vertical = 2.dp),
                    shape = RoundedCornerShape(SplitzShapes.SM),
                    backgroundColor = SplitzColors.surface(),
                    borderColor = SplitzColors.borderSubtle()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Solver Strategy:",
                                style = SplitzTypography.LabelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SplitzColors.textPrimary()
                            )

                            Row {
                                val isGreedy = state.selectedMode == "Greedy"
                                FilterChip(
                                    selected = isGreedy,
                                    onClick = { viewModel.selectAlgorithm("Greedy") },
                                    label = { Text("Greedy (${state.greedyCount})") },
                                    shape = RoundedCornerShape(SplitzShapes.Pill),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = SplitzColors.primary(),
                                        selectedLabelColor = Color.White,
                                        containerColor = SplitzColors.surfaceElevated(),
                                        labelColor = SplitzColors.textSecondary()
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                val isExact = state.selectedMode == "Exact"
                                FilterChip(
                                    selected = isExact,
                                    onClick = { viewModel.selectAlgorithm("Exact") },
                                    label = { Text("Exact Min (${state.exactCount})") },
                                    shape = RoundedCornerShape(SplitzShapes.Pill),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = SplitzColors.primary(),
                                        selectedLabelColor = Color.White,
                                        containerColor = SplitzColors.surfaceElevated(),
                                        labelColor = SplitzColors.textSecondary()
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // View Mode Toggle (List vs Graph)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SplitzSpacing.lg, vertical = SplitzSpacing.xs),
                horizontalArrangement = Arrangement.End
            ) {
                Box(modifier = Modifier.width(180.dp)) {
                    SplitzSegmentedControl(
                        items = listOf("List", "Graph"),
                        selectedIndex = selectedViewTab,
                        onItemSelected = { selectedViewTab = it }
                    )
                }
            }

            // Body Content
            if (state.transactions.isEmpty()) {
                EmptyStateView(
                    title = "Everyone is Settled",
                    subtitle = "No outstanding payments required for this group.",
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            } else {
                if (selectedViewTab == 0) {
                    // List View
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = SplitzSpacing.lg, vertical = SplitzSpacing.xs),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.transactions) { tx ->
                            SimplifiedTransactionCard(
                                tx = tx,
                                members = state.members
                            )
                        }
                    }
                } else {
                    // Graph View with Accessible Transaction Breakdown
                    val membersList = state.members.values.toList()
                    val edges = state.transactions.map {
                        DebtEdge(it.fromUserId, it.toUserId, it.amountPaise)
                    }
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = SplitzSpacing.lg, vertical = SplitzSpacing.xs),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            GraphVisualizer(
                                members = membersList,
                                edges = edges,
                                isSimplified = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Settlement network (${state.transactions.size} direct transfer${if (state.transactions.size == 1) "" else "s"})",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textMuted()
                            )
                        }

                        items(state.transactions) { tx ->
                            SimplifiedTransactionCard(
                                tx = tx,
                                members = state.members
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimplifiedTransactionCard(
    tx: SettlementTransaction,
    members: Map<String, Member>
) {
    val debtorName = members[tx.fromUserId]?.name ?: "Member"
    val creditorName = members[tx.toUserId]?.name ?: "Member"

    SplitzSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SplitzShapes.cardMedium),
        backgroundColor = SplitzColors.surface(),
        borderColor = SplitzColors.borderSubtle()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SplitzSpacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                UserAvatar(name = debtorName, size = 36, backgroundColor = DebtRed.copy(alpha = 0.15f), textColor = DebtRed)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = debtorName,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Text(
                        text = "pays",
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textMuted()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = SplitzColors.primary(),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))

                UserAvatar(name = creditorName, size = 36, backgroundColor = CreditGreen.copy(alpha = 0.15f), textColor = CreditGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = creditorName,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Text(
                        text = "receives",
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textMuted()
                    )
                }
            }

            MoneyText(
                amountPaise = tx.amountPaise,
                style = SplitzTypography.TitleLarge,
                fontWeight = FontWeight.Bold,
                colorMode = MoneyColorMode.NEUTRAL,
                animate = true
            )
        }
    }
}
