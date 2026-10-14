package com.paisede.app.ui.screens.simplify

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.domain.algorithm.DebtEdge
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.GraphVisualizer
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.KissatenTokens
import com.paisede.app.ui.theme.LocalAppCurrency
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.SimplifyViewModel

@Composable
fun SimplifyDebtsScreen(
    viewModel: SimplifyViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedViewTab by remember { mutableStateOf(0) } // 0 = List, 1 = Graph
    val currentCurrency = LocalAppCurrency.current

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SplitzTopAppBar(
                title = "Simplify Debts",
                subtitle = "SMART DEBT SETTLEMENT",
                onNavigateBack = onNavigateBack,
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = SplitzColors.brandSoft(),
                        border = BorderStroke(1.dp, SplitzColors.brandAccent().copy(alpha = 0.3f))
                    ) {
                        Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = "Optimization active",
                                tint = SplitzColors.brandAccent(),
                                modifier = Modifier.size(20.dp)
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Item 1: Hero Optimization Card (Deep Espresso / Wood Paper aesthetic)
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            color = SplitzColors.surfaceElevated(),
                            border = BorderStroke(1.dp, SplitzColors.border()),
                            shadowElevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                // Top Badges
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(SplitzShapes.Pill),
                                        color = SplitzColors.positiveContainer(),
                                        border = BorderStroke(1.dp, SplitzColors.positive().copy(alpha = 0.4f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(SplitzColors.positive())
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "DIRECT SETTLE ENGINE",
                                                style = SplitzTypography.LabelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = SplitzColors.positive(),
                                                letterSpacing = 0.6.sp
                                            )
                                        }
                                    }

                                    val reductionPercent = if (state.beforeCount > 0) {
                                        val diff = state.beforeCount - state.afterCount
                                        if (diff > 0) "-${(diff * 100) / state.beforeCount}%" else "Optimal"
                                    } else "0%"

                                    Surface(
                                        shape = RoundedCornerShape(SplitzShapes.Pill),
                                        color = SplitzColors.brandSoft(),
                                        border = BorderStroke(1.dp, SplitzColors.brandAccent().copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = "$reductionPercent STEPS",
                                            style = SplitzTypography.LabelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SplitzColors.brandAccent(),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Debt Simplification",
                                    style = SplitzTypography.HeadlineSmall,
                                    fontWeight = FontWeight.Black,
                                    color = SplitzColors.textPrimary()
                                )
                                Text(
                                    text = "Resolve circular balances and settle all group debts with minimal transfers.",
                                    style = SplitzTypography.BodySmall,
                                    color = SplitzColors.textSecondary()
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                // Transformation Metrics row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(SplitzColors.backgroundElevated())
                                        .border(1.dp, SplitzColors.borderSubtle(), RoundedCornerShape(16.dp))
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "BEFORE OPTIMIZATION",
                                            style = SplitzTypography.LabelSmall,
                                            color = SplitzColors.textSecondary(),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${state.beforeCount}",
                                            style = SplitzTypography.HeadlineMedium,
                                            fontWeight = FontWeight.Black,
                                            color = SplitzColors.textPrimary()
                                        )
                                        Text(
                                            text = "raw debts",
                                            style = SplitzTypography.LabelSmall,
                                            color = SplitzColors.textMuted()
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "to",
                                        tint = SplitzColors.brandAccent(),
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "AFTER OPTIMIZATION",
                                            style = SplitzTypography.LabelSmall,
                                            color = SplitzColors.positive(),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${state.afterCount}",
                                            style = SplitzTypography.HeadlineMedium,
                                            fontWeight = FontWeight.Black,
                                            color = SplitzColors.positive()
                                        )
                                        Text(
                                            text = "transfers",
                                            style = SplitzTypography.LabelSmall,
                                            color = SplitzColors.textMuted()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Item 2: Algorithm Switcher (Greedy vs Exact)
                    if (state.isExactAvailable && state.exactCount != null) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SplitzColors.surfaceElevated(),
                                border = BorderStroke(1.dp, SplitzColors.borderSubtle()),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Algorithm:",
                                        style = SplitzTypography.LabelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textPrimary()
                                    )

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        val isGreedy = state.selectedMode == "Greedy"
                                        val isExact = state.selectedMode == "Exact"

                                        Surface(
                                            shape = RoundedCornerShape(SplitzShapes.Pill),
                                            color = if (isGreedy) SplitzColors.brandAccent() else SplitzColors.surface(),
                                            border = BorderStroke(1.dp, if (isGreedy) SplitzColors.brandAccent() else SplitzColors.border()),
                                            modifier = Modifier.clickable { viewModel.selectAlgorithm("Greedy") }
                                        ) {
                                            Text(
                                                text = "Greedy (${state.greedyCount})",
                                                style = SplitzTypography.LabelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isGreedy) SplitzColors.onBrandAccent() else SplitzColors.textPrimary(),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(SplitzShapes.Pill),
                                            color = if (isExact) SplitzColors.brandAccent() else SplitzColors.surface(),
                                            border = BorderStroke(1.dp, if (isExact) SplitzColors.brandAccent() else SplitzColors.border()),
                                            modifier = Modifier.clickable { viewModel.selectAlgorithm("Exact") }
                                        ) {
                                            Text(
                                                text = "Exact Min (${state.exactCount})",
                                                style = SplitzTypography.LabelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isExact) SplitzColors.onBrandAccent() else SplitzColors.textPrimary(),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Item 3: View Switcher (List vs Graph)
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SETTLEMENT TRANSFERS (${state.transactions.size})",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary(),
                                letterSpacing = 0.8.sp
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(SplitzShapes.Pill))
                                    .background(SplitzColors.surface())
                                    .border(1.dp, SplitzColors.border(), RoundedCornerShape(SplitzShapes.Pill))
                                    .padding(2.dp)
                            ) {
                                val isList = selectedViewTab == 0
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(SplitzShapes.Pill))
                                        .background(if (isList) SplitzColors.brandAccent() else Color.Transparent)
                                        .clickable { selectedViewTab = 0 }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "List",
                                        style = SplitzTypography.LabelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isList) SplitzColors.onBrandAccent() else SplitzColors.textPrimary()
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(SplitzShapes.Pill))
                                        .background(if (!isList) SplitzColors.brandAccent() else Color.Transparent)
                                        .clickable { selectedViewTab = 1 }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Graph",
                                        style = SplitzTypography.LabelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (!isList) SplitzColors.onBrandAccent() else SplitzColors.textPrimary()
                                    )
                                }
                            }
                        }
                    }

                    // Body Transactions / Empty State
                    if (state.transactions.isEmpty()) {
                        item {
                            KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    KissatenHankoStamp(text = "SETTLED")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "All debts are settled!",
                                        style = SplitzTypography.TitleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textPrimary()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Everyone in this group is squared up. No transfers needed.",
                                        style = SplitzTypography.BodySmall,
                                        color = SplitzColors.textMuted(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        if (selectedViewTab == 1) {
                            // Graph View item
                            item {
                                val membersList = state.members.values.toList()
                                val edges = state.transactions.map {
                                    DebtEdge(it.fromUserId, it.toUserId, it.amountPaise)
                                }
                                KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        GraphVisualizer(
                                            members = membersList,
                                            edges = edges,
                                            isSimplified = true
                                        )
                                    }
                                }
                            }
                        }

                        // Transfer Cards
                        items(state.transactions) { tx ->
                            KissatenSimplifiedTransferCard(
                                tx = tx,
                                members = state.members
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }

@Composable
fun KissatenSimplifiedTransferCard(
    tx: SettlementTransaction,
    members: Map<String, Member>
) {
    val debtorName = members[tx.fromUserId]?.name ?: "Member"
    val creditorName = members[tx.toUserId]?.name ?: "Member"
    val currentCurrency = LocalAppCurrency.current
    var isMarkedSettled by remember { mutableStateOf(false) }

    KissatenReceiptCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Debtor (Payer)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    UserAvatar(
                        name = debtorName,
                        size = 38,
                        backgroundColor = SplitzColors.negativeContainer(),
                        textColor = SplitzColors.negative()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = debtorName,
                            style = SplitzTypography.BodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Text(
                            text = "Owes",
                            style = SplitzTypography.LabelSmall,
                            color = SplitzColors.negative(),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Middle: Coffee cup arrow
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalCafe,
                        contentDescription = null,
                        tint = SplitzColors.brandAccent(),
                        modifier = Modifier.size(16.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "transfers to",
                        tint = SplitzColors.brandAccent(),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Right: Creditor (Receiver)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = creditorName,
                            style = SplitzTypography.BodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Text(
                            text = "Receives",
                            style = SplitzTypography.LabelSmall,
                            color = SplitzColors.positive(),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    UserAvatar(
                        name = creditorName,
                        size = 38,
                        backgroundColor = SplitzColors.positiveContainer(),
                        textColor = SplitzColors.positive()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            KissatenPerforationDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Amount & Settle action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SETTLEMENT AMOUNT",
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textMuted(),
                        letterSpacing = 0.5.sp
                    )
                    MoneyText(
                        amountPaise = tx.amountPaise,
                        style = SplitzTypography.HeadlineSmall,
                        fontWeight = FontWeight.Black,
                        colorMode = MoneyColorMode.NEUTRAL
                    )
                }

                if (isMarkedSettled) {
                    KissatenHankoStamp(text = "SETTLED")
                } else {
                    Surface(
                        shape = RoundedCornerShape(SplitzShapes.Pill),
                        color = SplitzColors.brandPrimary(),
                        modifier = Modifier.clickable { isMarkedSettled = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = "Settle",
                                tint = SplitzColors.onPrimary(),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Settle Debt",
                                style = SplitzTypography.LabelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.onPrimary()
                            )
                        }
                    }
                }
            }
        }
    }
}
