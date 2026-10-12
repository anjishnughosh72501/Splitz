package com.paisede.app.ui.screens.graph

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.GraphVisualizer
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
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
import com.paisede.app.ui.viewmodel.DebtGraphViewModel
import com.paisede.app.util.CurrencyUtils

@Composable
fun DebtGraphScreen(
    viewModel: DebtGraphViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val memberMap = state.members.associateBy { it.id }
    val currentCurrency = LocalAppCurrency.current

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(SplitzShapes.Pill),
                    color = SplitzColors.surface(),
                    border = BorderStroke(1.dp, SplitzColors.border()),
                    modifier = Modifier.clickable { onNavigateBack() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = SplitzColors.textPrimary(),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Back",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Debt Flow Graph",
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Text(
                        text = "DEBT FLOW GRAPH",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SplitzColors.brandAccent(),
                        letterSpacing = 1.sp
                    )
                }

                Box(modifier = Modifier.size(40.dp))
            }
        }
    ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Segmented Switcher: All Debts vs Simplified Debts
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(SplitzShapes.Pill))
                        .background(SplitzColors.surface())
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val isAll = !state.isSimplifiedTab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(SplitzShapes.Pill))
                            .background(if (isAll) SplitzColors.brandAccent() else Color.Transparent)
                            .clickable { viewModel.setTab(false) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "All Debts (${state.rawEdges.size})",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isAll) Color.White else SplitzColors.textPrimary()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(SplitzShapes.Pill))
                            .background(if (!isAll) SplitzColors.brandAccent() else Color.Transparent)
                            .clickable { viewModel.setTab(true) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Simplified (${state.simplifiedEdges.size})",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (!isAll) Color.White else SplitzColors.textPrimary()
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Canvas Graph Visualizer inside Kissaten Receipt Card
                    item {
                        KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val activeEdges = if (state.isSimplifiedTab) state.simplifiedEdges else state.rawEdges
                                GraphVisualizer(
                                    members = state.members,
                                    edges = activeEdges,
                                    isSimplified = state.isSimplifiedTab
                                )
                            }
                        }
                    }

                    // Circular Debt Resolution Card
                    if (!state.isSimplifiedTab) {
                        item {
                            KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "CIRCULAR DEBT RESOLUTION",
                                                style = SplitzTypography.LabelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = SplitzColors.textPrimary(),
                                                letterSpacing = 0.6.sp
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            val statusDesc = if (state.detectedCycle != null) {
                                                val cycleNames = state.detectedCycle!!.map { memberMap[it]?.name ?: it }
                                                "Loop detected: " + cycleNames.joinToString(" → ")
                                            } else {
                                                "No circular debts detected"
                                            }
                                            Text(
                                                text = statusDesc,
                                                style = SplitzTypography.BodyMedium,
                                                color = if (state.detectedCycle != null) SplitzColors.error() else SplitzColors.brandAccent(),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        if (state.detectedCycle == null) {
                                            KissatenHankoStamp(text = "CLEAR")
                                        }
                                    }

                                    if (state.detectedCycle != null) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Surface(
                                            shape = RoundedCornerShape(SplitzShapes.Pill),
                                            color = SplitzColors.brandAccent(),
                                            modifier = Modifier.clickable { viewModel.cancelCycles() }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Sync,
                                                    contentDescription = "Resolve",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Resolve Circular Debts",
                                                    style = SplitzTypography.LabelMedium,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    if (state.cyclesCanceledAmount > 0L) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Eliminated debt: ${CurrencyUtils.formatPaise(state.cyclesCanceledAmount, currency = currentCurrency)}",
                                            style = SplitzTypography.BodySmall,
                                            color = SplitzColors.brandAccent(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Edge transfers breakdown
                    val activeEdges = if (state.isSimplifiedTab) state.simplifiedEdges else state.rawEdges
                    item {
                        Text(
                            text = "TRANSFER FLOWS (${activeEdges.size})",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary(),
                            letterSpacing = 0.8.sp
                        )
                    }

                    items(activeEdges) { edge ->
                        val fromName = memberMap[edge.fromUserId]?.name ?: "Member"
                        val toName = memberMap[edge.toUserId]?.name ?: "Member"

                        KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    UserAvatar(name = fromName, size = 32)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = fromName,
                                        style = SplitzTypography.BodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textPrimary()
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "to",
                                        tint = SplitzColors.brandAccent(),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    UserAvatar(name = toName, size = 32)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = toName,
                                        style = SplitzTypography.BodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textPrimary()
                                    )
                                }

                                MoneyText(
                                    amountPaise = edge.amountPaise,
                                    style = SplitzTypography.TitleMedium,
                                    fontWeight = FontWeight.Bold,
                                    colorMode = MoneyColorMode.NEUTRAL
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
