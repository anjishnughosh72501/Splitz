package com.paisede.app.ui.screens.analytics

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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.IndigoPurple
import com.paisede.app.ui.theme.Slate100
import com.paisede.app.ui.theme.Slate200
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.ui.theme.Slate800
import com.paisede.app.ui.theme.Slate900
import com.paisede.app.ui.viewmodel.AnalyticsViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val an = state.analytics

    Scaffold(
        topBar = {
            SplitzTopAppBar(
                title = "Spending Analytics",
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
            if (an.expenseCount == 0) {
                EmptyStateView(
                    title = "No Analytics Available",
                    subtitle = "Add expenses to see spending analytics and breakdown.",
                    icon = Icons.Default.Analytics,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Top Metrics Overview Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Slate900),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "TOTAL SPENT",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.LightGray,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                MoneyText(
                                    amountPaise = an.totalSpendingPaise,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    colorMode = MoneyColorMode.NEUTRAL
                                )

                                Spacer(modifier = Modifier.height(18.dp))
                                androidx.compose.material3.HorizontalDivider(color = Slate800)
                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "AVERAGE EXPENSE",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.LightGray
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        MoneyText(
                                            amountPaise = an.averageExpensePaise,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            colorMode = MoneyColorMode.NEUTRAL
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "EXPENSES COUNT",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.LightGray
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${an.expenseCount}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Spending by Category
                    item {
                        Text(
                            text = "SPENDING BY CATEGORY",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate600
                        )
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                an.spendingByCategoryPaise.entries.sortedByDescending { it.value }
                                    .forEach { (cat, amount) ->
                                        val progress = if (an.totalSpendingPaise > 0L) {
                                            amount.toFloat() / an.totalSpendingPaise.toFloat()
                                        } else 0f

                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = cat.replaceFirstChar { it.uppercase() },
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Slate900
                                                )
                                                MoneyText(
                                                    amountPaise = amount,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    colorMode = MoneyColorMode.NEUTRAL
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            LinearProgressIndicator(
                                                progress = { progress },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(8.dp)
                                                    .clip(RoundedCornerShape(4.dp)),
                                                color = BrandTeal,
                                                trackColor = Slate100
                                            )
                                        }
                                    }
                            }
                        }
                    }

                    // Spending by Person
                    item {
                        Text(
                            text = "SPENDING BY PERSON (CONSUMED)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate600
                        )
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                an.spendingByPersonPaise.entries.sortedByDescending { it.value }
                                    .forEach { (userId, amount) ->
                                        val name = state.members[userId]?.name ?: "Member"
                                        val progress = if (an.totalSpendingPaise > 0L) {
                                            amount.toFloat() / an.totalSpendingPaise.toFloat()
                                        } else 0f

                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    UserAvatar(name = name, size = 30)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = name,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = Slate900
                                                    )
                                                }
                                                MoneyText(
                                                    amountPaise = amount,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    colorMode = MoneyColorMode.NEUTRAL
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            LinearProgressIndicator(
                                                progress = { progress },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(8.dp)
                                                    .clip(RoundedCornerShape(4.dp)),
                                                color = IndigoPurple,
                                                trackColor = Slate100
                                            )
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}
