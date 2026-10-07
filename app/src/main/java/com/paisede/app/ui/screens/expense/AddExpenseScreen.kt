package com.paisede.app.ui.screens.expense

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.sp
import com.paisede.app.domain.model.Category
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SplitType
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzGlassCard
import com.paisede.app.ui.components.SplitzPrimaryButton
import com.paisede.app.ui.components.SplitzSegmentedControl
import com.paisede.app.ui.components.SplitzSurface
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.SplitzBorderWidth
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
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
    val currentCurrency = com.paisede.app.ui.theme.LocalAppCurrency.current
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
                .background(SplitzColors.background())
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = SplitzSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(SplitzSpacing.md)
            ) {
                // Step 1: Hero Amount Entry
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    SplitzSurface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(SplitzShapes.cardHero),
                        backgroundColor = SplitzColors.surface(),
                        borderColor = SplitzColors.borderSubtle()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(SplitzSpacing.lg),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ENTER AMOUNT",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = SplitzColors.textMuted(),
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = amountInput,
                                onValueChange = { input ->
                                    amountInput = input
                                    val paise = CurrencyUtils.parseCurrencyInput(input, currentCurrency)
                                    if (paise != null) {
                                        viewModel.setAmount(paise)
                                    }
                                },
                                placeholder = {
                                    Text(
                                        "0",
                                        style = SplitzTypography.MoneyDisplay,
                                        color = SplitzColors.textMuted().copy(alpha = 0.4f)
                                    )
                                },
                                leadingIcon = {
                                    val currentCurrency = com.paisede.app.ui.theme.LocalAppCurrency.current
                                    Text(
                                        text = currentCurrency.symbol,
                                        style = SplitzTypography.MoneyDisplay,
                                        color = SplitzColors.primary(),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 16.dp, end = 4.dp)
                                    )
                                },
                                textStyle = SplitzTypography.MoneyDisplay.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SplitzColors.textPrimary()
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(SplitzShapes.MD),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SplitzColors.primary(),
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedContainerColor = SplitzColors.surfaceElevated(),
                                    unfocusedContainerColor = SplitzColors.surfaceElevated()
                                ),
                                singleLine = true
                            )
                        }
                    }
                }

                // Step 2: Description
                item {
                    Text(
                        text = "Description",
                        style = SplitzTypography.LabelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { viewModel.setDescription(it) },
                        placeholder = {
                            Text(
                                "e.g. Goa Dinner, Groceries, Flight tickets",
                                color = SplitzColors.textMuted()
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(SplitzShapes.SM),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SplitzColors.primary(),
                            unfocusedBorderColor = SplitzColors.borderSubtle(),
                            focusedContainerColor = SplitzColors.surface(),
                            unfocusedContainerColor = SplitzColors.surface()
                        ),
                        singleLine = true
                    )
                }

                // Step 3: Paid By (Interactive Avatar Cards)
                item {
                    Text(
                        text = "Paid By",
                        style = SplitzTypography.LabelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(members) { member ->
                            val isSelected = state.payerId == member.id
                            SplitzSurface(
                                shape = RoundedCornerShape(SplitzShapes.Pill),
                                backgroundColor = if (isSelected) SplitzColors.primary() else SplitzColors.surface(),
                                borderColor = if (isSelected) SplitzColors.primary() else SplitzColors.borderSubtle(),
                                onClick = { viewModel.setPayer(member.id) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    UserAvatar(
                                        name = member.name,
                                        size = 26,
                                        backgroundColor = if (isSelected) Color.White.copy(alpha = 0.2f) else null,
                                        textColor = if (isSelected) Color.White else null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = member.name,
                                        style = SplitzTypography.LabelMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else SplitzColors.textPrimary()
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Step 4: Category
                item {
                    Text(
                        text = "Category",
                        style = SplitzTypography.LabelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
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
                                label = {
                                    Text(
                                        cat.name,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                shape = RoundedCornerShape(SplitzShapes.Pill),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SplitzColors.surfaceElevated(),
                                    selectedLabelColor = SplitzColors.primaryBright(),
                                    containerColor = SplitzColors.surface(),
                                    labelColor = SplitzColors.textSecondary()
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) SplitzColors.primary() else SplitzColors.borderSubtle()
                                )
                            )
                        }
                    }
                }

                // Step 5: Split Method (Segmented Glass Control)
                item {
                    Text(
                        text = "Split Method",
                        style = SplitzTypography.LabelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val splitTypes = SplitType.values()
                    val splitTitles = listOf("Equal", "Exact", "% Percent", "Shares")
                    val selectedIndex = splitTypes.indexOf(state.splitType)

                    SplitzSegmentedControl(
                        items = splitTitles,
                        selectedIndex = selectedIndex,
                        onItemSelected = { index ->
                            viewModel.setSplitType(splitTypes[index])
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Split Details input based on selected type
                    when (state.splitType) {
                        SplitType.EQUAL -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Select participants sharing this expense:",
                                    style = SplitzTypography.BodySmall,
                                    color = SplitzColors.textSecondary()
                                )
                                members.forEach { member ->
                                    val isChecked = state.participantIds.contains(member.id)
                                    SplitzSurface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(SplitzShapes.SM),
                                        backgroundColor = if (isChecked) SplitzColors.surfaceElevated() else SplitzColors.surface(),
                                        borderColor = if (isChecked) SplitzColors.primary().copy(alpha = 0.4f) else SplitzColors.borderSubtle(),
                                        onClick = { viewModel.toggleParticipant(member.id) }
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = { viewModel.toggleParticipant(member.id) },
                                                colors = CheckboxDefaults.colors(checkedColor = SplitzColors.primary())
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            UserAvatar(name = member.name, size = 32, isSelected = isChecked)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = member.name,
                                                style = SplitzTypography.BodyLarge,
                                                fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
                                                color = SplitzColors.textPrimary()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        SplitType.EXACT -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Specify exact amount for each member:",
                                    style = SplitzTypography.BodySmall,
                                    color = SplitzColors.textSecondary()
                                )
                                members.forEach { member ->
                                    var exactVal by remember { mutableStateOf("") }
                                    SplitzSurface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(SplitzShapes.SM),
                                        backgroundColor = SplitzColors.surface(),
                                        borderColor = SplitzColors.borderSubtle()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                UserAvatar(name = member.name, size = 32)
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = member.name,
                                                    style = SplitzTypography.BodyLarge,
                                                    color = SplitzColors.textPrimary()
                                                )
                                            }
                                            OutlinedTextField(
                                                value = exactVal,
                                                onValueChange = { input ->
                                                    exactVal = input
                                                    val paise = CurrencyUtils.parseCurrencyInput(input, currentCurrency) ?: 0L
                                                    viewModel.setExactAmount(member.id, paise)
                                                },
                                                label = {
                                                    Text("${currentCurrency.symbol} Amount")
                                                },
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                                modifier = Modifier.width(140.dp),
                                                shape = RoundedCornerShape(SplitzShapes.SM),
                                                singleLine = true
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        SplitType.PERCENTAGE -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Specify percentage share for each member:",
                                    style = SplitzTypography.BodySmall,
                                    color = SplitzColors.textSecondary()
                                )
                                members.forEach { member ->
                                    var percentVal by remember { mutableStateOf("") }
                                    SplitzSurface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(SplitzShapes.SM),
                                        backgroundColor = SplitzColors.surface(),
                                        borderColor = SplitzColors.borderSubtle()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                UserAvatar(name = member.name, size = 32)
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = member.name,
                                                    style = SplitzTypography.BodyLarge,
                                                    color = SplitzColors.textPrimary()
                                                )
                                            }
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
                                                shape = RoundedCornerShape(SplitzShapes.SM),
                                                singleLine = true
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        SplitType.SHARES -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Specify shares for each member:",
                                    style = SplitzTypography.BodySmall,
                                    color = SplitzColors.textSecondary()
                                )
                                members.forEach { member ->
                                    var shareVal by remember { mutableStateOf("1") }
                                    LaunchedEffect(Unit) {
                                        viewModel.setShares(member.id, 1)
                                    }
                                    SplitzSurface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(SplitzShapes.SM),
                                        backgroundColor = SplitzColors.surface(),
                                        borderColor = SplitzColors.borderSubtle()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                UserAvatar(name = member.name, size = 32)
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = member.name,
                                                    style = SplitzTypography.BodyLarge,
                                                    color = SplitzColors.textPrimary()
                                                )
                                            }
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
                                                shape = RoundedCornerShape(SplitzShapes.SM),
                                                singleLine = true
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Step 6: Glass Summary Panel
                item {
                    Text(
                        text = "Summary",
                        style = SplitzTypography.LabelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    SplitzGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = SplitzShapes.MD
                    ) {
                        Column {
                            val payerName = members.firstOrNull { it.id == state.payerId }?.name ?: "Unknown"
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$payerName paid",
                                    style = SplitzTypography.BodyLarge,
                                    color = SplitzColors.textSecondary()
                                )
                                MoneyText(
                                    amountPaise = state.amountPaise,
                                    style = SplitzTypography.TitleMedium,
                                    fontWeight = FontWeight.Bold,
                                    colorMode = MoneyColorMode.NEUTRAL
                                )
                            }
                            Spacer(modifier = Modifier.height(SplitzSpacing.sm))
                            HorizontalDivider(color = SplitzColors.borderSubtle().copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(SplitzSpacing.sm))
                            Text(
                                text = "Split equally among ${state.participantIds.size} participant(s)",
                                style = SplitzTypography.BodySmall,
                                color = SplitzColors.textSecondary()
                            )
                        }
                    }
                }

                if (state.validationError != null) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(SplitzShapes.SM))
                                .background(DebtRed.copy(alpha = 0.12f))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "Error",
                                tint = DebtRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.validationError!!,
                                style = SplitzTypography.BodyMedium,
                                color = DebtRed,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Bottom CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SplitzSpacing.lg)
            ) {
                SplitzPrimaryButton(
                    text = if (state.isSaving) "Saving Expense..." else "Add Expense",
                    onClick = {
                        viewModel.saveExpense {
                            onExpenseAdded()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSaving
                )
            }
        }
    }
}
