package com.paisede.app.ui.screens.expense

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzTypography
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
            SplitzTopAppBar(
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
                            tint = if (state.canUndo) SplitzColors.primary() else SplitzColors.textMuted()
                        )
                    }
                    IconButton(
                        onClick = { viewModel.redo() },
                        enabled = state.canRedo
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Redo,
                            contentDescription = "Redo",
                            tint = if (state.canRedo) SplitzColors.primary() else SplitzColors.textMuted()
                        )
                    }
                    IconButton(onClick = { viewModel.toggleSort() }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort",
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
                .background(SplitzColors.background())
        ) {
            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search by description...", color = SplitzColors.textMuted()) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SplitzColors.textMuted()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(SplitzShapes.SM),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SplitzColors.primary(),
                    unfocusedBorderColor = SplitzColors.borderSubtle(),
                    focusedContainerColor = SplitzColors.surface(),
                    unfocusedContainerColor = SplitzColors.surface()
                ),
                singleLine = true
            )

            // Category Filter Chips
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAllSelected = state.selectedCategory == null
                    FilterChip(
                        selected = isAllSelected,
                        onClick = { viewModel.filterByCategory(null) },
                        label = { Text("All Categories") },
                        shape = RoundedCornerShape(SplitzShapes.Pill),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SplitzColors.primary(),
                            selectedLabelColor = Color.White,
                            containerColor = SplitzColors.surface(),
                            labelColor = SplitzColors.textSecondary()
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isAllSelected,
                            borderColor = if (isAllSelected) Color.Transparent else SplitzColors.borderSubtle()
                        )
                    )
                }
                items(Category.DEFAULT_CATEGORIES) { cat ->
                    val isSelected = state.selectedCategory == cat.id
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.filterByCategory(
                                if (state.selectedCategory == cat.id) null else cat.id
                            )
                        },
                        label = { Text(cat.name) },
                        shape = RoundedCornerShape(SplitzShapes.Pill),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SplitzColors.primary(),
                            selectedLabelColor = Color.White,
                            containerColor = SplitzColors.surface(),
                            labelColor = SplitzColors.textSecondary()
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) Color.Transparent else SplitzColors.borderSubtle()
                        )
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    groupedByDate.forEach { (dateHeader, exps) ->
                        item {
                            Text(
                                text = dateHeader.uppercase(),
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textMuted(),
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
            .clip(RoundedCornerShape(SplitzShapes.MD))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(SplitzShapes.MD),
        colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
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
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Paid by $payerName • Split among ${splits.size}",
                        style = SplitzTypography.BodySmall,
                        color = SplitzColors.textSecondary()
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    MoneyText(
                        amountPaise = expense.amountPaise,
                        style = SplitzTypography.TitleLarge,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.NEUTRAL,
                        animate = true
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = SplitzColors.textMuted(),
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
                    androidx.compose.material3.HorizontalDivider(color = SplitzColors.borderSubtle())
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Split Breakdown (${expense.splitType.name}):",
                        style = SplitzTypography.LabelMedium,
                        color = SplitzColors.textSecondary(),
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
                                style = SplitzTypography.BodyMedium,
                                color = SplitzColors.textPrimary()
                            )
                            Text(
                                text = CurrencyUtils.formatPaise(split.amountPaise),
                                style = SplitzTypography.BodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SplitzColors.textPrimary()
                            )
                        }
                    }
                }
            }
        }
    }
}
