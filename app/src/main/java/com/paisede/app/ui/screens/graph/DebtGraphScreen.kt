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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.GraphVisualizer
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.theme.AccentAmber
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.Slate100
import com.paisede.app.ui.theme.Slate200
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.ui.theme.Slate800
import com.paisede.app.ui.theme.Slate900
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Tabs: All Debts vs Simplified Debts
            TabRow(
                selectedTabIndex = if (state.isSimplifiedTab) 1 else 0,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = BrandTeal
            ) {
                Tab(
                    selected = !state.isSimplifiedTab,
                    onClick = { viewModel.setTab(false) },
                    text = { Text("All Debts (${state.rawEdges.size})") }
                )
                Tab(
                    selected = state.isSimplifiedTab,
                    onClick = { viewModel.setTab(true) },
                    text = { Text("Simplified (${state.simplifiedEdges.size})") }
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.detectedCycle != null) AccentAmber.copy(alpha = 0.1f)
                                else Slate100
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (state.detectedCycle != null) AccentAmber else Slate200
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
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate800
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
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Slate600
                                        )
                                    }
                                }

                                if (state.detectedCycle != null) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { viewModel.cancelCycles() },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Sync,
                                            contentDescription = "Resolve Circular Debt",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Resolve Circular Debt")
                                    }
                                }

                                if (state.cyclesCanceledAmount > 0L) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Resolved in circular flows: ${CurrencyUtils.formatPaise(state.cyclesCanceledAmount)}",
                                        style = MaterialTheme.typography.bodySmall,
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
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                }

                val activeEdges = if (state.isSimplifiedTab) state.simplifiedEdges else state.rawEdges
                items(activeEdges) { edge ->
                    val fromName = memberMap[edge.fromUserId]?.name ?: "Member"
                    val toName = memberMap[edge.toUserId]?.name ?: "Member"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
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
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Slate600,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = toName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate900
                                )
                            }

                            MoneyText(
                                amountPaise = edge.amountPaise,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                colorMode = MoneyColorMode.NEUTRAL
                            )
                        }
                    }
                }
            }
        }
    }
}
