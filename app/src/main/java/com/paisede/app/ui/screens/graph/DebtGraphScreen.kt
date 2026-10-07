package com.paisede.app.ui.screens.graph

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
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.paisede.app.ui.components.GraphVisualizer
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.theme.AccentAmber
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.DebtGraphViewModel
import com.paisede.app.util.CurrencyUtils

@Composable
fun DebtGraphScreen(
    viewModel: DebtGraphViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val memberMap = state.members.associateBy { it.id }

    Scaffold(
        topBar = {
            SplitzTopAppBar(
                title = "Debt Network",
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
            // Tabs: All Debts vs Simplified Debts
            val selectedTabIndex = if (state.isSimplifiedTab) 1 else 0
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = SplitzColors.surface(),
                contentColor = SplitzColors.primary(),
                indicator = { tabPositions ->
                    if (selectedTabIndex in tabPositions.indices) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = SplitzColors.primary()
                        )
                    }
                }
            ) {
                Tab(
                    selected = !state.isSimplifiedTab,
                    onClick = { viewModel.setTab(false) },
                    text = {
                        Text(
                            "All Debts (${state.rawEdges.size})",
                            style = SplitzTypography.LabelLarge,
                            fontWeight = if (!state.isSimplifiedTab) FontWeight.Bold else FontWeight.Medium,
                            color = if (!state.isSimplifiedTab) SplitzColors.primary() else SplitzColors.textSecondary()
                        )
                    }
                )
                Tab(
                    selected = state.isSimplifiedTab,
                    onClick = { viewModel.setTab(true) },
                    text = {
                        Text(
                            "Simplified (${state.simplifiedEdges.size})",
                            style = SplitzTypography.LabelLarge,
                            fontWeight = if (state.isSimplifiedTab) FontWeight.Bold else FontWeight.Medium,
                            color = if (state.isSimplifiedTab) SplitzColors.primary() else SplitzColors.textSecondary()
                        )
                    }
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Interactive Canvas Graph Visualizer
                item {
                    val activeEdges = if (state.isSimplifiedTab) state.simplifiedEdges else state.rawEdges
                    GraphVisualizer(
                        members = state.members,
                        edges = activeEdges,
                        isSimplified = state.isSimplifiedTab
                    )
                }

                // Circular Debt Resolution Card (for Raw Debts)
                if (!state.isSimplifiedTab) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(SplitzShapes.MD),
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.detectedCycle != null) AccentAmber.copy(alpha = 0.12f)
                                else SplitzColors.surface()
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (state.detectedCycle != null) AccentAmber.copy(alpha = 0.5f) else SplitzColors.borderSubtle()
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "CIRCULAR DEBTS",
                                            style = SplitzTypography.LabelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SplitzColors.textPrimary()
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        val statusDesc = if (state.detectedCycle != null) {
                                            val cycleNames = state.detectedCycle!!.map { memberMap[it]?.name ?: it }
                                            "Circular loop: " + cycleNames.joinToString(" → ")
                                        } else {
                                            "No circular debts found"
                                        }
                                        Text(
                                            text = statusDesc,
                                            style = SplitzTypography.BodyMedium,
                                            color = SplitzColors.textSecondary()
                                        )
                                    }
                                }

                                if (state.detectedCycle != null) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { viewModel.cancelCycles() },
                                        shape = RoundedCornerShape(SplitzShapes.button),
                                        colors = ButtonDefaults.buttonColors(containerColor = SplitzColors.primary())
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Sync,
                                            contentDescription = "Resolve Circular Debt",
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Resolve Circular Debt", color = Color.White, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                if (state.cyclesCanceledAmount > 0L) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Resolved in circular flows: ${CurrencyUtils.formatPaise(state.cyclesCanceledAmount)}",
                                        style = SplitzTypography.BodySmall,
                                        color = CreditGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // List of edges currently displayed
                item {
                    Text(
                        text = if (state.isSimplifiedTab) "SIMPLIFIED DEBTS" else "INDIVIDUAL DEBTS",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textMuted()
                    )
                }

                val activeEdges = if (state.isSimplifiedTab) state.simplifiedEdges else state.rawEdges
                items(activeEdges) { edge ->
                    val fromName = memberMap[edge.fromUserId]?.name ?: "Member"
                    val toName = memberMap[edge.toUserId]?.name ?: "Member"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(SplitzShapes.SM),
                        colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = fromName,
                                    style = SplitzTypography.BodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SplitzColors.textPrimary()
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = SplitzColors.textMuted(),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = toName,
                                    style = SplitzTypography.BodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SplitzColors.textPrimary()
                                )
                            }

                            MoneyText(
                                amountPaise = edge.amountPaise,
                                style = SplitzTypography.TitleMedium,
                                fontWeight = FontWeight.Bold,
                                colorMode = MoneyColorMode.NEUTRAL,
                                animate = true
                            )
                        }
                    }
                }
            }
        }
    }
}
