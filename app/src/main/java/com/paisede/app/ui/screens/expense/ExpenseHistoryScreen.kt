package com.paisede.app.ui.screens.expense

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.domain.model.Category
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.Member
import androidx.compose.foundation.layout.navigationBarsPadding
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.KissatenSawtoothTeeth
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

    val groupedByDate = filteredExpenses.groupBy { DateUtils.formatRelativeDate(it.first.createdAt) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SplitzTopAppBar(
                title = "Expense Ledger",
                subtitle = "ITEMIZED RECEIPTS",
                onNavigateBack = onNavigateBack,
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = SplitzColors.surface(),
                        border = BorderStroke(1.dp, SplitzColors.border()),
                        modifier = Modifier.clickable(enabled = state.canUndo) { viewModel.undo() }
                    ) {
                        Box(modifier = Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Undo,
                                contentDescription = "Undo",
                                tint = if (state.canUndo) SplitzColors.textPrimary() else SplitzColors.textTertiary().copy(alpha = 0.4f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = SplitzColors.surface(),
                        border = BorderStroke(1.dp, SplitzColors.border()),
                        modifier = Modifier.clickable(enabled = state.canRedo) { viewModel.redo() }
                    ) {
                        Box(modifier = Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Redo,
                                contentDescription = "Redo",
                                tint = if (state.canRedo) SplitzColors.textPrimary() else SplitzColors.textTertiary().copy(alpha = 0.4f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = SplitzColors.surface(),
                        border = BorderStroke(1.dp, SplitzColors.border()),
                        modifier = Modifier.clickable { viewModel.toggleSort() }
                    ) {
                        Box(modifier = Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort",
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
                // Search Bar in Paper Surface
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = {
                        Text(
                            "Search expenses...",
                            color = SplitzColors.textTertiary()
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = SplitzColors.brandAccent()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SplitzColors.brandAccent(),
                        unfocusedBorderColor = SplitzColors.border(),
                        focusedContainerColor = SplitzColors.surface(),
                        unfocusedContainerColor = SplitzColors.surface(),
                        focusedTextColor = SplitzColors.textPrimary(),
                        unfocusedTextColor = SplitzColors.textPrimary()
                    ),
                    singleLine = true
                )

                // Category Filter Chips
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        val isAllSelected = state.selectedCategory == null
                        Surface(
                            shape = RoundedCornerShape(SplitzShapes.Pill),
                            color = if (isAllSelected) SplitzColors.brandAccent() else SplitzColors.surface(),
                            border = BorderStroke(1.dp, if (isAllSelected) SplitzColors.brandAccent() else SplitzColors.border()),
                            modifier = Modifier.clickable { viewModel.filterByCategory(null) }
                        ) {
                            Text(
                                text = "All",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isAllSelected) SplitzColors.onBrandAccent() else SplitzColors.textPrimary(),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                    items(Category.DEFAULT_CATEGORIES) { cat ->
                        val isSelected = state.selectedCategory == cat.id
                        Surface(
                            shape = RoundedCornerShape(SplitzShapes.Pill),
                            color = if (isSelected) SplitzColors.brandAccent() else SplitzColors.surface(),
                            border = BorderStroke(1.dp, if (isSelected) SplitzColors.brandAccent() else SplitzColors.border()),
                            modifier = Modifier.clickable {
                                viewModel.filterByCategory(
                                    if (state.selectedCategory == cat.id) null else cat.id
                                )
                            }
                        ) {
                            Text(
                                text = cat.name,
                                style = SplitzTypography.LabelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) SplitzColors.onBrandAccent() else SplitzColors.textPrimary(),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                if (filteredExpenses.isEmpty()) {
                    KissatenReceiptCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = SplitzColors.brandAccent(),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No expenses found",
                                style = SplitzTypography.TitleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (state.expenses.isEmpty()) "Record an expense to start your group history."
                                else "No expenses match your search filter.",
                                style = SplitzTypography.BodySmall,
                                color = SplitzColors.textSecondary()
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        groupedByDate.forEach { (dateHeader, exps) ->
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(SplitzColors.brandAccent())
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = dateHeader.uppercase(),
                                        style = SplitzTypography.LabelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textTertiary(),
                                        letterSpacing = 1.sp
                                    )
                                }
                            }

                            items(exps, key = { it.first.id }) { (expense, splits) ->
                                KissatenExpenseSlipItem(
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
fun KissatenExpenseSlipItem(
    expense: Expense,
    splits: List<ExpenseSplit>,
    members: Map<String, Member>
) {
    var expanded by remember { mutableStateOf(false) }
    val payerName = members[expense.payerId]?.name ?: "Payer"
    val currentCurrency = LocalAppCurrency.current

    KissatenReceiptCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Receipt Header / Slip Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAvatar(
                        name = payerName,
                        size = 40,
                        backgroundColor = KissatenTokens.MatchaContainer,
                        textColor = KissatenTokens.MatchaText
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = expense.description,
                            style = SplitzTypography.TitleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Paid by $payerName",
                                style = SplitzTypography.BodySmall,
                                color = SplitzColors.brandAccent(),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = " • Split with ${splits.size}",
                                style = SplitzTypography.BodySmall,
                                color = SplitzColors.textTertiary()
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    MoneyText(
                        amountPaise = expense.amountPaise,
                        style = SplitzTypography.TitleLarge,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.NEUTRAL
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = SplitzColors.textTertiary(),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    KissatenPerforationDivider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Split breakdown (${expense.splitType.name})",
                            style = SplitzTypography.LabelSmall,
                            color = SplitzColors.textTertiary(),
                            fontWeight = FontWeight.Bold
                        )
                        KissatenHankoStamp(text = "PAID")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    splits.forEach { split ->
                        val memberName = members[split.userId]?.name ?: "Member"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                UserAvatar(name = memberName, size = 24)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = memberName,
                                    style = SplitzTypography.BodyMedium,
                                    color = SplitzColors.textPrimary()
                                )
                            }
                            Text(
                                text = CurrencyUtils.formatPaise(split.amountPaise, currency = currentCurrency),
                                style = SplitzTypography.BodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    KissatenSawtoothTeeth(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
