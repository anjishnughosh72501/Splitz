package com.paisede.app.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paisede.app.domain.algorithm.ExpenseSplitter
import com.paisede.app.domain.model.Category
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SplitType
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.Slate200
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.ui.theme.Slate800
import com.paisede.app.ui.theme.Slate900
import com.paisede.app.ui.viewmodel.ExpenseViewModel
import com.paisede.app.util.CurrencyUtils

@Composable
fun AddExpenseScreen(
    viewModel: ExpenseViewModel,
    members: List<Member>,
    onNavigateBack: () -> Unit,
    onExpenseAdded: () -> Unit
) {
    val state by viewModel.addState.collectAsState()
    var amountInput by remember { mutableStateOf("") }

    // Auto-select all members if participants are empty
    LaunchedEffect(members) {
        if (state.participantIds.isEmpty() && members.isNotEmpty()) {
            members.forEach { viewModel.toggleParticipant(it.id) }
            if (state.payerId.isEmpty()) {
                viewModel.setPayer(members.first().id)
            }
        }
    }

    Scaffold(
        topBar = {
            SplitzTopAppBar(
                title = "Add Expense",
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Amount
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { input ->
                            amountInput = input
                            val paise = CurrencyUtils.parseCurrencyInput(input)
                            if (paise != null) {
                                viewModel.setAmount(paise)
                            }
                        },
                        label = { Text("Amount (₹)") },
                        placeholder = { Text("0.00") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.CurrencyRupee, contentDescription = "Rupee")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                // Description
                item {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { viewModel.setDescription(it) },
                        label = { Text("Description") },
                        placeholder = { Text("e.g. Dinner, Groceries") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                // Paid By
                item {
                    Text(
                        text = "Paid By",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(members) { member ->
                            val isSelected = state.payerId == member.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setPayer(member.id) },
                                label = { Text(member.name) },
                                leadingIcon = {
                                    UserAvatar(
                                        name = member.name,
                                        size = 24,
                                        backgroundColor = if (isSelected) Color.White else BrandTeal.copy(alpha = 0.2f),
                                        textColor = if (isSelected) BrandTeal else Slate800
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandTeal,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Category
                item {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(Category.DEFAULT_CATEGORIES) { cat ->
                            val isSelected = state.category == cat.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setCategory(cat.id) },
                                label = { Text(cat.name) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Slate800,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Split Method
                item {
                    Text(
                        text = "Split Method",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val splitTypes = SplitType.values()
                    val selectedTabIndex = splitTypes.indexOf(state.splitType)

                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = BrandTeal
                    ) {
                        splitTypes.forEachIndexed { index, type ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { viewModel.setSplitType(type) },
                                text = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Split Details input based on type
                    when (state.splitType) {
                        SplitType.EQUAL -> {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Split equally among participants:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate600
                                )
                                members.forEach { member ->
                                    val isChecked = state.participantIds.contains(member.id)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.toggleParticipant(member.id) }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { viewModel.toggleParticipant(member.id) }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = member.name, style = MaterialTheme.typography.bodyLarge)
                                    }
                                }
                            }
                        }
                        SplitType.EXACT -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Specify amount for each member:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate600
                                )
                                members.forEach { member ->
                                    var exactVal by remember { mutableStateOf("") }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = member.name, modifier = Modifier.weight(1f))
                                        OutlinedTextField(
                                            value = exactVal,
                                            onValueChange = { input ->
                                                exactVal = input
                                                val paise = CurrencyUtils.parseCurrencyInput(input) ?: 0L
                                                viewModel.setExactAmount(member.id, paise)
                                            },
                                            label = { Text("₹ Amount") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            modifier = Modifier.width(140.dp),
                                            singleLine = true
                                        )
                                    }
                                }
                            }
                        }
                        SplitType.PERCENTAGE -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Specify percentage share for each member:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate600
                                )
                                members.forEach { member ->
                                    var percentVal by remember { mutableStateOf("") }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = member.name, modifier = Modifier.weight(1f))
                                        OutlinedTextField(
                                            value = percentVal,
                                            onValueChange = { input ->
                                                percentVal = input
                                                val p = input.toDoubleOrNull() ?: 0.0
                                                val bp = (p * 100).toInt()
                                                viewModel.setPercentageBp(member.id, bp)
                                            },
                                            label = { Text("% Share") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.width(120.dp),
                                            singleLine = true
                                        )
                                    }
                                }
                            }
                        }
                        SplitType.SHARES -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Specify shares for each member:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate600
                                )
                                members.forEach { member ->
                                    var shareVal by remember { mutableStateOf("1") }
                                    LaunchedEffect(Unit) {
                                        viewModel.setShares(member.id, 1)
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = member.name, modifier = Modifier.weight(1f))
                                        OutlinedTextField(
                                            value = shareVal,
                                            onValueChange = { input ->
                                                shareVal = input
                                                val s = input.toIntOrNull() ?: 0
                                                viewModel.setShares(member.id, s)
                                            },
                                            label = { Text("Shares") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.width(120.dp),
                                            singleLine = true
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Summary
                item {
                    Text(
                        text = "Summary",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val payerName = members.firstOrNull { it.id == state.payerId }?.name ?: "Unknown"
                            Text(
                                text = "$payerName paid ${CurrencyUtils.formatPaise(state.amountPaise)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Split among ${state.participantIds.size} participants",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate600
                            )
                        }
                    }
                }

                if (state.validationError != null) {
                    item {
                        Text(
                            text = state.validationError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DebtRed,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Save Button
            Button(
                onClick = {
                    viewModel.saveExpense {
                        onExpenseAdded()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                enabled = !state.isSaving
            ) {
                Text(
                    text = if (state.isSaving) "Saving Expense..." else "Add Expense",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}
