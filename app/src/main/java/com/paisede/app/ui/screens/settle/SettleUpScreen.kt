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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.paisede.app.ui.components.SplitzPrimaryButton
import com.paisede.app.ui.components.SplitzSurface
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
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
        val currentCurrency = com.paisede.app.ui.theme.LocalAppCurrency.current
        val amountStr = CurrencyUtils.formatPaise(tx.amountPaise, currency = currentCurrency)

        AlertDialog(
            onDismissRequest = { viewModel.dismissPrompt() },
            shape = RoundedCornerShape(SplitzShapes.sheet),
            containerColor = SplitzColors.surface(),
            title = {
                Text(
                    text = "Confirm Settlement",
                    style = SplitzTypography.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.textPrimary()
                )
            },
            text = {
                Text(
                    text = "Record a payment of $amountStr from $debtor to $creditor? This will record the transaction and adjust outstanding balances.",
                    style = SplitzTypography.BodyMedium,
                    color = SplitzColors.textSecondary()
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmSettlement() },
                    colors = ButtonDefaults.buttonColors(containerColor = SplitzColors.primary()),
                    shape = RoundedCornerShape(SplitzShapes.button)
                ) {
                    Text("Confirm Payment", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissPrompt() }) {
                    Text("Cancel", color = SplitzColors.textSecondary())
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
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
                            tint = SplitzColors.primary()
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
        ) {
            if (state.recommendedPayments.isEmpty()) {
                EmptyStateView(
                    title = "All Settled Up!",
                    subtitle = "No one owes any money in this group. You're completely square!",
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(SplitzSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(SplitzSpacing.md)
                ) {
                    item {
                        Text(
                            text = "RECOMMENDED SETTLEMENTS",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textMuted()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Minimal set of direct transfers calculated to settle debts with the fewest transactions.",
                            style = SplitzTypography.BodySmall,
                            color = SplitzColors.textSecondary()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    items(state.recommendedPayments) { tx ->
                        ModernSettleUpCard(
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
fun ModernSettleUpCard(
    tx: SettlementTransaction,
    members: Map<String, Member>,
    onSettleClick: () -> Unit
) {
    val debtor = members[tx.fromUserId]?.name ?: "Member"
    val creditor = members[tx.toUserId]?.name ?: "Member"

    SplitzSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SplitzShapes.cardMedium),
        backgroundColor = SplitzColors.surface(),
        borderColor = SplitzColors.borderSubtle()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SplitzSpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    UserAvatar(name = debtor, size = 36, backgroundColor = DebtRed.copy(alpha = 0.15f), textColor = DebtRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = debtor,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
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
                    UserAvatar(name = creditor, size = 36, backgroundColor = CreditGreen.copy(alpha = 0.15f), textColor = CreditGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = creditor,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                }

                MoneyText(
                    amountPaise = tx.amountPaise,
                    style = SplitzTypography.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    colorMode = MoneyColorMode.NEUTRAL,
                    animate = true
                )
            }

            Spacer(modifier = Modifier.height(SplitzSpacing.md))

            val currentCurrency = com.paisede.app.ui.theme.LocalAppCurrency.current
            SplitzPrimaryButton(
                text = "Mark as Settled (${CurrencyUtils.formatPaise(tx.amountPaise, currency = currentCurrency)})",
                onClick = onSettleClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
