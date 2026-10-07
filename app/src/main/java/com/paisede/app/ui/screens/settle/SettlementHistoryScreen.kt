package com.paisede.app.ui.screens.settle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.SettlementViewModel

@Composable
fun SettlementHistoryScreen(
    viewModel: SettlementViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SplitzTopAppBar(
                title = "Settlement History",
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
            if (state.pastSettlements.isEmpty()) {
                EmptyStateView(
                    title = "No Past Settlements",
                    subtitle = "When payments are recorded via 'Settle Up', they will appear here.",
                    icon = Icons.Default.History,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.pastSettlements) { tx ->
                        val fromName = state.members[tx.fromUserId]?.name ?: "Member"
                        val toName = state.members[tx.toUserId]?.name ?: "Member"

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
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = CreditGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "$fromName paid $toName",
                                            style = SplitzTypography.TitleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = SplitzColors.textPrimary()
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Completed Settlement",
                                            style = SplitzTypography.BodySmall,
                                            color = CreditGreen,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                MoneyText(
                                    amountPaise = tx.amountPaise,
                                    style = SplitzTypography.TitleLarge,
                                    fontWeight = FontWeight.Bold,
                                    colorMode = MoneyColorMode.NEUTRAL,
                                    animate = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
