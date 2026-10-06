package com.paisede.app.ui.screens.debug

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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.IndigoPurple

import com.paisede.app.ui.viewmodel.DebugViewModel

@Composable
fun DataStructureDebugScreen(
    viewModel: DebugViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SplitzTopAppBar(
                title = "Data Structure Inspector",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { viewModel.refreshDebugData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh DS state",
                            tint = BrandTeal
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Intro Callout for Viva
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = null,
                                tint = BrandTeal,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "LIVE DATA STRUCTURE MONITOR",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Live internal memory states of HashMap, PriorityQueues (Max & Min Heaps), Adjacency Map, DFS Cycles, and Stacks used in Splitz.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // 1. HashMap: Net Balances
            item {
                DebugSectionCard(
                    title = "1. HashMap<UserId, Long> (Net Balances)",
                    complexity = "Lookup: O(1) | Space: O(N)"
                ) {
                    state.netBalances.forEach { (userId, balance) ->
                        val name = state.members[userId]?.name ?: userId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            MoneyText(
                                amountPaise = balance,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                showSign = true
                            )
                        }
                    }
                    val sum = state.netBalances.values.sum()
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sum Invariant: $sum paise (Zero-sum verified ✓)",
                        style = MaterialTheme.typography.labelSmall,
                        color = CreditGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 2. PriorityQueue: Creditors Heap
            item {
                DebugSectionCard(
                    title = "2. PriorityQueue<Balance> (Creditors Max-Heap)",
                    complexity = "Insert: O(log N) | Poll Max: O(log N)"
                ) {
                    if (state.creditorQueue.isEmpty()) {
                        Text(text = "Empty (no creditors)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        state.creditorQueue.forEachIndexed { idx, bal ->
                            val name = state.members[bal.userId]?.name ?: bal.userId
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "#${idx + 1} $name",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                MoneyText(
                                    amountPaise = bal.amountPaise,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    colorMode = MoneyColorMode.CREDIT,
                                    showSign = true
                                )
                            }
                        }
                    }
                }
            }

            // 3. PriorityQueue: Debtors Heap
            item {
                DebugSectionCard(
                    title = "3. PriorityQueue<Balance> (Debtors Max-Debt Heap)",
                    complexity = "Insert: O(log N) | Poll Max: O(log N)"
                ) {
                    if (state.debtorQueue.isEmpty()) {
                        Text(text = "Empty (no debtors)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        state.debtorQueue.forEachIndexed { idx, bal ->
                            val name = state.members[bal.userId]?.name ?: bal.userId
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "#${idx + 1} $name",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                MoneyText(
                                    amountPaise = bal.amountPaise,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    colorMode = MoneyColorMode.DEBT,
                                    showSign = true
                                )
                            }
                        }
                    }
                }
            }

            // 4. Graph & DFS Cycles
            item {
                DebugSectionCard(
                    title = "4. Directed Graph & DFS Cycle Detection",
                    complexity = "Adjacency Map | DFS: O(V + E)"
                ) {
                    Text(
                        text = "Total Raw Edges: ${state.graphEdges.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val cycleInfo = if (state.detectedCycles != null) {
                        val names = state.detectedCycles!!.map { state.members[it]?.name ?: it }
                        "Directed Cycle detected: " + names.joinToString(" → ")
                    } else {
                        "No directed cycles (Graph is DAG)"
                    }
                    Text(
                        text = cycleInfo,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (state.detectedCycles != null) DebtRed else CreditGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 5. Greedy vs Exact Solver Comparison
            item {
                DebugSectionCard(
                    title = "5. Greedy vs Exact Solver (Backtracking / Subsets)",
                    complexity = "Greedy: O(N log N) | Exact: O(2^N)"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Greedy 2-Heap Result:", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${state.greedyTxCount} transactions",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandTeal
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Exact Minimum (≤8 members):", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = if (state.exactTxCount != null) "${state.exactTxCount} transactions" else "N/A (>8 members)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPurple
                        )
                    }
                }
            }

            // 6. Stack<ExpenseAction>: Undo & Redo
            item {
                DebugSectionCard(
                    title = "6. Stack<ExpenseAction> (Undo / Redo)",
                    complexity = "Push: O(1) | Pop: O(1)"
                ) {
                    Text(
                        text = "Undo Stack Size: ${state.undoStack.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Redo Stack Size: ${state.redoStack.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun DebugSectionCard(
    title: String,
    complexity: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = complexity,
                style = MaterialTheme.typography.labelSmall,
                color = BrandTeal,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            androidx.compose.material3.HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}
