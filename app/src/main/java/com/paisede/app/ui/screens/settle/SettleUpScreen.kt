package com.paisede.app.ui.screens.settle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenPrimaryPillButton
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.KissatenTokens
import com.paisede.app.ui.theme.LocalAppCurrency
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
    val currentCurrency = LocalAppCurrency.current

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
        val amountStr = CurrencyUtils.formatPaise(tx.amountPaise, currency = currentCurrency)

        AlertDialog(
            onDismissRequest = { viewModel.dismissPrompt() },
            shape = RoundedCornerShape(20.dp),
            containerColor = SplitzColors.surfaceElevated(),
            title = {
                Text(
                    text = "Record Settlement",
                    style = SplitzTypography.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.textPrimary()
                )
            },
            text = {
                Text(
                    text = "Record payment of $amountStr from $debtor to $creditor? Group balances will be updated.",
                    style = SplitzTypography.BodyMedium,
                    color = SplitzColors.textSecondary()
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmSettlement() },
                    colors = ButtonDefaults.buttonColors(containerColor = SplitzColors.brandPrimary()),
                    shape = RoundedCornerShape(SplitzShapes.Pill)
                ) {
                    Text("Record Payment", color = SplitzColors.onPrimary(), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissPrompt() }) {
                    Text("Cancel", color = SplitzColors.textSecondary(), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            com.paisede.app.ui.components.SplitzTopAppBar(
                title = "Settle Up",
                subtitle = "SETTLE BALANCES",
                onNavigateBack = onNavigateBack,
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = SplitzColors.surfaceElevated(),
                        border = BorderStroke(1.dp, SplitzColors.borderSubtle()),
                        modifier = Modifier.clickable { onViewHistoryClick() }
                    ) {
                        Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Settlement History",
                                tint = SplitzColors.textPrimary(),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .navigationBarsPadding()
        ) {
            if (state.recommendedPayments.isEmpty()) {
                KissatenReceiptCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        KissatenHankoStamp(text = "ALL SETTLED")
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "All balances are settled ☕",
                            style = SplitzTypography.TitleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "All member balances in this group are even. No pending debts.",
                            style = SplitzTypography.BodySmall,
                            color = SplitzColors.textMuted()
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text(
                            text = "RECOMMENDED SETTLEMENT TRANSFERS",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary(),
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Optimized direct payments calculated to balance all group debts with minimal transactions.",
                            style = SplitzTypography.BodySmall,
                            color = SplitzColors.textMuted()
                        )
                    }

                    items(state.recommendedPayments) { tx ->
                        KissatenSettleUpCard(
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
fun KissatenSettleUpCard(
    tx: SettlementTransaction,
    members: Map<String, Member>,
    onSettleClick: () -> Unit
) {
    val debtor = members[tx.fromUserId]?.name ?: "Member"
    val creditor = members[tx.toUserId]?.name ?: "Member"
    val currentCurrency = LocalAppCurrency.current

    KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {
                    UserAvatar(
                        name = debtor,
                        size = 36,
                        backgroundColor = SplitzColors.negativeContainer(),
                        textColor = SplitzColors.negative()
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = debtor,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "to",
                        tint = SplitzColors.brandAccent(),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    UserAvatar(
                        name = creditor,
                        size = 36,
                        backgroundColor = SplitzColors.positiveContainer(),
                        textColor = SplitzColors.positive()
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = creditor,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                MoneyText(
                    amountPaise = tx.amountPaise,
                    style = SplitzTypography.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    colorMode = MoneyColorMode.NEUTRAL
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            KissatenPerforationDivider()
            Spacer(modifier = Modifier.height(12.dp))

            KissatenPrimaryPillButton(
                text = "Record Payment (${CurrencyUtils.formatPaise(tx.amountPaise, currency = currentCurrency)})",
                onClick = onSettleClick,
                icon = Icons.Default.Check,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Backward compatible alias
@Composable
fun ModernSettleUpCard(
    tx: SettlementTransaction,
    members: Map<String, Member>,
    onSettleClick: () -> Unit
) {
    KissatenSettleUpCard(tx, members, onSettleClick)
}
