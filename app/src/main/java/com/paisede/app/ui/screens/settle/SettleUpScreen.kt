package com.paisede.app.ui.screens.settle

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
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.Slate200
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.ui.theme.Slate900
import com.paisede.app.ui.viewmodel.SettlementViewModel
import com.paisede.app.util.CurrencyUtils

@Composable
fun SettleUpScreen(
    viewModel: SettlementViewModel,
    onNavigateBack: () -> Unit,
    onViewHistoryClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    // Confirmation Dialog
    state.pendingSettlement?.let { tx ->
        val debtor = state.members[tx.fromUserId]?.name ?: "Member"
        val creditor = state.members[tx.toUserId]?.name ?: "Member"
        val amountStr = CurrencyUtils.formatPaise(tx.amountPaise)

        AlertDialog(
            onDismissRequest = { viewModel.dismissPrompt() },
            title = { Text("Confirm Settlement") },
            text = {
                Text("Record a payment of $amountStr from $debtor to $creditor? This will reduce both members' balances to settled.")
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmSettlement() },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
                ) {
                    Text("Confirm Payment")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissPrompt() }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SplitzTopAppBar(
                title = "Settle Up",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = onViewHistoryClick) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Settlement History",
                            tint = BrandTeal
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.recommendedPayments.isEmpty()) {
                EmptyStateView(
                    title = "All Settled Up!",
                    subtitle = "No one owes any money in this group. You're all square!",
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text(
                            text = "RECOMMENDED SETTLEMENTS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Minimal set of direct payments to settle all group debts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(state.recommendedPayments) { tx ->
                        SettleUpCard(
                            tx = tx,
                            members = state.members,
                            onSettleClick = { viewModel.promptSettlement(tx) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettleUpCard(
    tx: SettlementTransaction,
    members: Map<String, Member>,
    onSettleClick: () -> Unit
) {
    val debtor = members[tx.fromUserId]?.name ?: "Member"
    val creditor = members[tx.toUserId]?.name ?: "Member"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(name = debtor, size = 38, backgroundColor = DebtRed.copy(alpha = 0.15f), textColor = DebtRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = debtor,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    UserAvatar(name = creditor, size = 38, backgroundColor = CreditGreen.copy(alpha = 0.15f), textColor = CreditGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = creditor,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                MoneyText(
                    amountPaise = tx.amountPaise,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    colorMode = MoneyColorMode.NEUTRAL
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onSettleClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal)
            ) {
                Text(
                    text = "Mark as Settled (${CurrencyUtils.formatPaise(tx.amountPaise)})",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}
