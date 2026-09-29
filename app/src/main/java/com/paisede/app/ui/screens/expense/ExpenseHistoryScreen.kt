package com.paisede.app.ui.screens.expense

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.paisede.app.domain.model.Category
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.Member
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.PaiseDeTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.Slate100
import com.paisede.app.ui.theme.Slate200
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.ui.theme.Slate800
import com.paisede.app.ui.theme.Slate900
import com.paisede.app.ui.viewmodel.ExpenseViewModel
import com.paisede.app.util.CurrencyUtils
import com.paisede.app.util.DateUtils

@Composable
fun ExpenseHistoryScreen(
    viewModel: ExpenseViewModel,
    onNavigateBack: () -> Unit,
    onAddExpenseClick: () -> Unit
) {
    val state by viewModel.historyState.collectAsState()

    // Filter and sort items
    val filteredExpenses = state.expenses
        .filter { (exp, _) ->
            val matchQuery = state.searchQuery.isEmpty() ||
                    exp.description.contains(state.searchQuery, ignoreCase = true)
            val matchCategory = state.selectedCategory == null || exp.category == state.selectedCategory
            val matchPayer = state.selectedPayerId == null || exp.payerId == state.selectedPayerId
            matchQuery && matchCategory && matchPayer
        }
        .sortedWith(
            if (state.sortDescending) compareByDescending { it.first.createdAt }
            else compareBy { it.first.createdAt }
        )

    // Group by relative date (Today, Yesterday, Date)
    val groupedByDate = filteredExpenses.groupBy { DateUtils.formatRelativeDate(it.first.createdAt) }

    Scaffold(
        topBar = {
            PaiseDeTopAppBar(
                title = "Expense History",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { viewModel.undo() },
                        enabled = state.canUndo
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo",
                            tint = if (state.canUndo) BrandTeal else Slate200
                        )
                    }
                    IconButton(
                        onClick = { viewModel.redo() },
                        enabled = state.canRedo
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Redo,
                            contentDescription = "Redo",
                            tint = if (state.canRedo) BrandTeal else Slate200
                        )
                    }
                    IconButton(onClick = { viewModel.toggleSort() }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort",
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
            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search by description...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Category Filter Chips
            LazyRow(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = state.selectedCategory == null,
                        onClick = { viewModel.filterByCategory(null) },
                        label = { Text("All Categories") }
                    )
                }
                items(Category.DEFAULT_CATEGORIES) { cat ->
                    FilterChip(
                        selected = state.selectedCategory == cat.id,
                        onClick = {
                            viewModel.filterByCategory(
                                if (state.selectedCategory == cat.id) null else cat.id
                            )
                        },
                        label = { Text(cat.name) }
                    )
                }
            }

            if (filteredExpenses.isEmpty()) {
                EmptyStateView(
                    title = "No Expenses Found",
                    subtitle = if (state.expenses.isEmpty()) "Add an expense to start tracking group spending."
                    else "No expenses match your search or filter.",
                    icon = Icons.Default.ReceiptLong,
                    actionButtonText = if (state.expenses.isEmpty()) "+ Add Expense" else null,
                    onActionClick = onAddExpenseClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    groupedByDate.forEach { (dateHeader, exps) ->
                        item {
                            Text(
                                text = dateHeader.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate600,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        items(exps, key = { it.first.id }) { (expense, splits) ->
                            ExpenseHistoryItem(
                                expense = expense,
                                splits = splits,
                                members = state.members
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseHistoryItem(
    expense: Expense,
    splits: List<ExpenseSplit>,
    members: Map<String, Member>
) {
    var expanded by remember { mutableStateOf(false) }
    val payerName = members[expense.payerId]?.name ?: "Unknown"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = expense.description,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Paid by $payerName • Split among ${splits.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate600
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    MoneyText(
                        amountPaise = expense.amountPaise,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.NEUTRAL
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Slate600,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    androidx.compose.material3.HorizontalDivider(color = Slate100)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Split Breakdown (${expense.splitType.name}):",
                        style = MaterialTheme.typography.labelMedium,
                        color = Slate600,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    splits.forEach { split ->
                        val memberName = members[split.userId]?.name ?: "Member"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = memberName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Slate800
                            )
                            Text(
                                text = CurrencyUtils.formatPaise(split.amountPaise),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate900
                            )
                        }
                    }
                }
            }
        }
    }
}
