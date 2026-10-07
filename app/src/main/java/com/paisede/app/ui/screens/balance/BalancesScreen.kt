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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzTypography
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
                .background(SplitzColors.background())
        ) {
            // Header stats banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(SplitzShapes.MD))
                    .background(SplitzColors.surface())
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TOTAL SPENDING",
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textMuted(),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MoneyText(
                        amountPaise = state.totalSpendingPaise,
                        style = SplitzTypography.TitleLarge,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.NEUTRAL,
                        animate = true
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(SplitzColors.borderSubtle())
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "OUTSTANDING DEBT",
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textMuted(),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MoneyText(
                        amountPaise = state.totalOutstandingDebtPaise,
                        style = SplitzTypography.TitleLarge,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.DEBT,
                        animate = true
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.items, key = { it.member.id }) { item ->
                        ModernBalanceCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun ModernBalanceCard(item: MemberBalanceItem) {
    val bal = item.balancePaise
    val (statusText, statusBg, statusColor) = when {
        bal > 0 -> Triple("Should receive", CreditGreen.copy(alpha = 0.15f), CreditGreen)
        bal < 0 -> Triple("Owes", DebtRed.copy(alpha = 0.15f), DebtRed)
        else -> Triple("Settled Up", SplitzColors.surfaceElevated(), SplitzColors.textMuted())
    }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(name = item.member.name, size = 44)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = item.member.name,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(SplitzShapes.Pill))
                            .background(statusBg)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = SplitzTypography.LabelSmall,
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            MoneyText(
                amountPaise = bal,
                style = SplitzTypography.TitleLarge,
                fontWeight = FontWeight.Bold,
                showSign = true,
                animate = true
            )
        }
    }
}
