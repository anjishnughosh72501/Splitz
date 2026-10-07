package com.paisede.app.ui.screens.simplify

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
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
                .background(SplitzColors.background())
        ) {
            // Transformation Metric Card: Before vs After
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                            text = "OPTIMIZATION SUMMARY",
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
                                    text = "ORIGINAL",
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
                                    text = "OPTIMIZED",
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
                                    text = "payments",
                                    style = SplitzTypography.BodySmall,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
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
            }

            // Exact Solver Comparison Box
            if (state.isExactAvailable && state.exactCount != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(SplitzShapes.SM),
                    colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Strategy:",
                                style = SplitzTypography.LabelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SplitzColors.textPrimary()
                            )

                            Row {
                                val isGreedy = state.selectedMode == "Greedy"
                                FilterChip(
                                    selected = isGreedy,
                                    onClick = { viewModel.selectAlgorithm("Greedy") },
                                    label = { Text("Standard (${state.greedyCount})") },
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TabRow(
                    selectedTabIndex = selectedViewTab,
                    modifier = Modifier
                        .width(200.dp)
                        .clip(RoundedCornerShape(SplitzShapes.Pill)),
                    containerColor = SplitzColors.surfaceElevated(),
                    contentColor = SplitzColors.primary()
                ) {
                    Tab(
                        selected = selectedViewTab == 0,
                        onClick = { selectedViewTab = 0 },
                        icon = { Icon(imageVector = Icons.Default.ViewList, contentDescription = "List View", modifier = Modifier.size(18.dp)) },
                        text = { Text("List", style = SplitzTypography.LabelSmall) }
                    )
                    Tab(
                        selected = selectedViewTab == 1,
                        onClick = { selectedViewTab = 1 },
                        icon = { Icon(imageVector = Icons.Default.Hub, contentDescription = "Graph View", modifier = Modifier.size(18.dp)) },
                        text = { Text("Graph", style = SplitzTypography.LabelSmall) }
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
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
                    // Graph View
                    val membersList = state.members.values.toList()
                    val edges = state.transactions.map {
                        DebtEdge(it.fromUserId, it.toUserId, it.amountPaise)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        GraphVisualizer(
                            members = membersList,
                            edges = edges,
                            isSimplified = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Visual settlement flow",
                            style = SplitzTypography.BodySmall,
                            color = SplitzColors.textSecondary(),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SplitzShapes.MD),
        colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                UserAvatar(name = debtorName, size = 38, backgroundColor = DebtRed.copy(alpha = 0.15f), textColor = DebtRed)
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

                UserAvatar(name = creditorName, size = 38, backgroundColor = CreditGreen.copy(alpha = 0.15f), textColor = CreditGreen)
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
