package com.paisede.app.ui.screens.simplify

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.paisede.app.ui.theme.AccentAmber
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.Slate100
import com.paisede.app.ui.theme.Slate200
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.ui.theme.Slate800
import com.paisede.app.ui.theme.Slate900
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Transformation Metric Card: Before vs After
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else Slate900
                ),
                border = if (isSystemInDarkTheme()) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "OPTIMIZATION SUMMARY",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "ORIGINAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = DebtRed,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${state.beforeCount}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "debts",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Optimized into",
                            tint = BrandTeal,
                            modifier = Modifier.size(32.dp)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "OPTIMIZED",
                                style = MaterialTheme.typography.labelSmall,
                                color = CreditGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${state.afterCount}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "payments",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    androidx.compose.material3.HorizontalDivider(color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.outlineVariant else Slate800)
                    Spacer(modifier = Modifier.height(10.dp))

                    val reduction = if (state.beforeCount > state.afterCount) state.beforeCount - state.afterCount else 0
                    Text(
                        text = if (reduction > 0) "Eliminated $reduction redundant transactions" else "All debts are in minimal form",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandTeal,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Exact Solver Comparison Box (Section 30)
            if (state.isExactAvailable && state.exactCount != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Strategy:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row {
                                FilterChip(
                                    selected = state.selectedMode == "Greedy",
                                    onClick = { viewModel.selectAlgorithm("Greedy") },
                                    label = { Text("Standard (${state.greedyCount})") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandTeal,
                                        selectedLabelColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                FilterChip(
                                    selected = state.selectedMode == "Exact",
                                    onClick = { viewModel.selectAlgorithm("Exact") },
                                    label = { Text("Exact Min (${state.exactCount})") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primaryContainer else Slate900,
                                        selectedLabelColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else Color.White
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
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TabRow(
                    selectedTabIndex = selectedViewTab,
                    modifier = Modifier.width(220.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = BrandTeal
                ) {
                    Tab(
                        selected = selectedViewTab == 0,
                        onClick = { selectedViewTab = 0 },
                        icon = { Icon(imageVector = Icons.Default.ViewList, contentDescription = "List View", modifier = Modifier.size(18.dp)) },
                        text = { Text("List") }
                    )
                    Tab(
                        selected = selectedViewTab == 1,
                        onClick = { selectedViewTab = 1 },
                        icon = { Icon(imageVector = Icons.Default.Hub, contentDescription = "Graph View", modifier = Modifier.size(18.dp)) },
                        text = { Text("Graph") }
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
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            .padding(20.dp)
                    ) {
                        GraphVisualizer(
                            members = membersList,
                            edges = edges,
                            isSimplified = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Visual settlement flow",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                UserAvatar(name = debtorName, size = 42, backgroundColor = DebtRed.copy(alpha = 0.15f), textColor = DebtRed)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = debtorName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "pays",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = BrandTeal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))

                UserAvatar(name = creditorName, size = 42, backgroundColor = CreditGreen.copy(alpha = 0.15f), textColor = CreditGreen)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = creditorName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "receives",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            MoneyText(
                amountPaise = tx.amountPaise,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                colorMode = MoneyColorMode.NEUTRAL
            )
        }
    }
}
