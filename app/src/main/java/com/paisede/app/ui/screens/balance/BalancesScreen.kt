package com.paisede.app.ui.screens.balance

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.paisede.app.domain.model.Member
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.Slate200
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.ui.theme.Slate800
import com.paisede.app.ui.theme.Slate900
import com.paisede.app.ui.viewmodel.BalanceViewModel
import com.paisede.app.ui.viewmodel.MemberBalanceItem

@Composable
fun BalancesScreen(
    viewModel: BalanceViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SplitzTopAppBar(
                title = "Group Balances",
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
            // Header stats banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Total Spending",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MoneyText(
                        amountPaise = state.totalSpendingPaise,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.NEUTRAL
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Unsettled Debts",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MoneyText(
                        amountPaise = state.totalOutstandingDebtPaise,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.DEBT
                    )
                }
            }

            if (state.items.isEmpty()) {
                EmptyStateView(
                    title = "No Balances",
                    subtitle = "Add members and expenses to view net balances.",
                    icon = Icons.Default.AccountBalance,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.items, key = { it.member.id }) { item ->
                        BalanceCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun BalanceCard(item: MemberBalanceItem) {
    val bal = item.balancePaise
    val statusText = when {
        bal > 0 -> "Should receive"
        bal < 0 -> "Owes"
        else -> "Settled Up"
    }
    val statusColor = when {
        bal > 0 -> CreditGreen
        bal < 0 -> DebtRed
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(name = item.member.name, size = 44)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = item.member.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            MoneyText(
                amountPaise = bal,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                showSign = true
            )
        }
    }
}
