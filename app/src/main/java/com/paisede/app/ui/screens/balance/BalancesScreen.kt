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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzGlassCard
import com.paisede.app.ui.components.SplitzSurface
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.BalanceViewModel
import com.paisede.app.ui.viewmodel.MemberBalanceItem

@Composable
fun BalancesScreen(
    viewModel: BalanceViewModel,
    userPreferences: com.paisede.app.data.preferences.UserPreferences? = null,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val currentUserName by (userPreferences?.userName ?: remember { kotlinx.coroutines.flow.MutableStateFlow("") }).collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
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
        ) {
            // Hero Financial Status Glass Card
            SplitzGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SplitzSpacing.lg, vertical = SplitzSpacing.md),
                cornerRadius = SplitzShapes.cardLarge
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
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
                            .height(40.dp)
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
            }

            if (state.items.isEmpty()) {
                EmptyStateView(
                    title = "Nothing to balance yet",
                    subtitle = "Once your group has expenses, member net balances will appear here.",
                    icon = Icons.Default.AccountBalance,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = SplitzSpacing.lg, vertical = SplitzSpacing.xs),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.items, key = { it.member.id }) { item ->
                        ModernBalanceCard(item = item, currentUserName = currentUserName)
                    }
                }
            }
        }
    }
}

@Composable
fun ModernBalanceCard(item: MemberBalanceItem, currentUserName: String = "") {
    val bal = item.balancePaise
    val displayName = when {
        item.member.name.startsWith("Me (") -> item.member.name
        item.member.name.equals(currentUserName, ignoreCase = true) && currentUserName.isNotBlank() -> "Me ($currentUserName)"
        else -> item.member.name
    }
    val (statusText, statusBg, statusColor) = when {
        bal > 0 -> Triple("Should receive", CreditGreen.copy(alpha = 0.15f), CreditGreen)
        bal < 0 -> Triple("Owes", DebtRed.copy(alpha = 0.15f), DebtRed)
        else -> Triple("Settled Up", SplitzColors.surfaceElevated(), SplitzColors.textMuted())
    }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(name = displayName, size = 42)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = displayName,
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
