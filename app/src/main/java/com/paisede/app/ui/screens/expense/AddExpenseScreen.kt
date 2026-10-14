package com.paisede.app.ui.screens.expense

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import com.paisede.app.ui.components.SplitzTopAppBar
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.domain.model.Category
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SplitType
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenPrimaryPillButton
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzAmbientBackground
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.KissatenTokens
import com.paisede.app.ui.theme.LocalAppCurrency
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.ExpenseViewModel
import com.paisede.app.util.CurrencyUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddExpenseScreen(
    viewModel: ExpenseViewModel,
    members: List<Member>,
    onNavigateBack: () -> Unit,
    onExpenseAdded: () -> Unit
) {
    val state by viewModel.addState.collectAsState()
    val currentCurrency = LocalAppCurrency.current
    var amountInput by remember { mutableStateOf("") }
    val haptic = LocalHapticFeedback.current

    // Auto-select all members if participants are empty
    LaunchedEffect(members) {
        if (state.participantIds.isEmpty() && members.isNotEmpty()) {
            members.forEach { viewModel.toggleParticipant(it.id) }
            if (state.payerId.isEmpty()) {
                viewModel.setPayer(members.first().id)
            }
        }
    }

    val todayFormatted = remember {
        SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(Date())
    }

    Scaffold(
        containerColor = Color.Transparent,
            topBar = {
                SplitzTopAppBar(
                    backButtonText = "Cancel",
                    onNavigateBack = onNavigateBack,
                    titleContent = {
                        Surface(
                            shape = RoundedCornerShape(SplitzShapes.Pill),
                            color = SplitzColors.surfaceElevated(),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(SplitzColors.brandAccent())
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "NEW EXPENSE",
                                    style = SplitzTypography.LabelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SplitzColors.textPrimary(),
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    },
                    actions = {
                        Surface(
                            shape = CircleShape,
                            color = SplitzColors.brandAccent(),
                            modifier = Modifier.clickable(enabled = !state.isSaving) {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                viewModel.saveExpense { onExpenseAdded() }
                            }
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save",
                                    tint = Color.White,
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
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Item 1: Primary Paper Voucher Card (Stitch Hero Voucher)
                    item {
                        KissatenReceiptCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                // Voucher Top Bar: Category Pill & Table Note
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val currentCat = Category.DEFAULT_CATEGORIES.firstOrNull { it.id == state.category }
                                        ?: Category.DEFAULT_CATEGORIES.first()

                                    Surface(
                                        shape = RoundedCornerShape(SplitzShapes.Pill),
                                        color = KissatenTokens.CaramelSecondary.copy(alpha = 0.12f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, KissatenTokens.CaramelSecondary.copy(alpha = 0.35f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocalCafe,
                                                contentDescription = null,
                                                tint = KissatenTokens.CaramelSecondary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = currentCat.name,
                                                style = SplitzTypography.LabelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = KissatenTokens.EspressoPrimary
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(SplitzShapes.Pill),
                                        color = KissatenTokens.PaperSurface,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, KissatenTokens.BorderWashi)
                                    ) {
                                        Text(
                                            text = todayFormatted,
                                            style = SplitzTypography.LabelSmall,
                                            color = KissatenTokens.TextMuted,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Large Currency + Amount Input
                                Text(
                                    text = "TOTAL AMOUNT",
                                    style = SplitzTypography.LabelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = KissatenTokens.TextMuted,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(KissatenTokens.PaperSurface)
                                        .border(1.dp, KissatenTokens.BorderWashi, RoundedCornerShape(16.dp))
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = currentCurrency.symbol,
                                        style = SplitzTypography.DisplaySmall,
                                        fontWeight = FontWeight.Black,
                                        color = KissatenTokens.CaramelSecondary
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
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
                                                style = SplitzTypography.DisplaySmall,
                                                color = KissatenTokens.TextMuted.copy(alpha = 0.35f)
                                            )
                                        },
                                        textStyle = SplitzTypography.DisplaySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = KissatenTokens.EspressoPrimary
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Description / Note Input
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(KissatenTokens.PaperSurface)
                                        .border(1.dp, KissatenTokens.BorderWashi, RoundedCornerShape(14.dp))
                                        .padding(horizontal = 14.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Storefront,
                                        contentDescription = null,
                                        tint = KissatenTokens.CaramelSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    OutlinedTextField(
                                        value = state.description,
                                        onValueChange = { viewModel.setDescription(it) },
                                        placeholder = {
                                            Text(
                                                "Description (e.g. Coffee, Dinner, Groceries)",
                                                style = SplitzTypography.BodyMedium,
                                                color = KissatenTokens.TextMuted.copy(alpha = 0.6f)
                                            )
                                        },
                                        textStyle = SplitzTypography.BodyLarge.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = KissatenTokens.EspressoPrimary
                                        ),
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Item 2: Category Selector Pill Row
                    item {
                        Column {
                            Text(
                                text = "CATEGORY",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary(),
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(Category.DEFAULT_CATEGORIES) { cat ->
                                    val isSelected = state.category == cat.id
                                    Surface(
                                        shape = RoundedCornerShape(SplitzShapes.Pill),
                                        color = if (isSelected) SplitzColors.brandAccent() else SplitzColors.surface(),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) SplitzColors.brandAccent() else SplitzColors.border()
                                        ),
                                        modifier = Modifier.clickable { viewModel.setCategory(cat.id) }
                                    ) {
                                        Text(
                                            text = cat.name,
                                            style = SplitzTypography.LabelSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else SplitzColors.textPrimary(),
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Item 3: "Paid by" Horizontal Selector
                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "PAID BY",
                                    style = SplitzTypography.LabelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SplitzColors.textPrimary(),
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "Who covered this bill?",
                                    style = SplitzTypography.LabelSmall,
                                    color = SplitzColors.textSecondary()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(members) { member ->
                                    val isSelected = state.payerId == member.id
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = if (isSelected) KissatenTokens.MatchaContainer else KissatenTokens.PaperSurface,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) KissatenTokens.MatchaTertiary else KissatenTokens.BorderWashi
                                        ),
                                        modifier = Modifier.clickable { viewModel.setPayer(member.id) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            UserAvatar(
                                                name = member.name,
                                                size = 28,
                                                isSelected = isSelected
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = member.name,
                                                style = SplitzTypography.BodyMedium,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) KissatenTokens.MatchaText else KissatenTokens.EspressoPrimary
                                            )
                                            if (isSelected) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = KissatenTokens.MatchaTertiary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Item 4: Split Method Segmented Control
                    item {
                        Column {
                            Text(
                                text = "SPLIT METHOD",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = KissatenTokens.EspressoPrimary,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val splitTypes = SplitType.values()
                            val splitLabels = listOf(
                                "Equally",
                                "Exact",
                                "Percentage",
                                "Shares"
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(SplitzColors.surface())
                                    .border(1.dp, SplitzColors.borderSubtle(), RoundedCornerShape(16.dp))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                splitTypes.forEachIndexed { index, splitType ->
                                    val isSelected = state.splitType == splitType
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) SplitzColors.brandAccent() else Color.Transparent)
                                            .clickable { viewModel.setSplitType(splitType) }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = splitLabels[index],
                                            style = SplitzTypography.LabelSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else SplitzColors.textPrimary(),
                                            textAlign = TextAlign.Center,
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Item 5: Split Calculation & Participant Breakdown
                    item {
                        when (state.splitType) {
                            SplitType.EQUAL -> {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Calculation summary pill
                                    val participantCount = state.participantIds.size
                                    val perPersonPaise = if (participantCount > 0) state.amountPaise / participantCount else 0L
                                    val remainderPaise = if (participantCount > 0) state.amountPaise % participantCount else 0L

                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = KissatenTokens.CaramelSecondary.copy(alpha = 0.08f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, KissatenTokens.CaramelSecondary.copy(alpha = 0.25f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Equal Split ($participantCount people)",
                                                    style = SplitzTypography.LabelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = KissatenTokens.EspressoPrimary
                                                )
                                                Text(
                                                    text = if (remainderPaise > 0L) "Remainder: ${CurrencyUtils.formatPaise(remainderPaise, currency = currentCurrency)} (covered by payer)" else "Even split",
                                                    style = SplitzTypography.BodySmall,
                                                    color = KissatenTokens.TextMuted
                                                )
                                            }
                                            Row(verticalAlignment = Alignment.Bottom) {
                                                Text(
                                                    text = "Per person: ",
                                                    style = SplitzTypography.BodySmall,
                                                    color = KissatenTokens.TextMuted
                                                )
                                                Text(
                                                    text = CurrencyUtils.formatPaise(perPersonPaise, currency = currentCurrency),
                                                    style = SplitzTypography.TitleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = KissatenTokens.CaramelSecondary
                                                )
                                            }
                                        }
                                    }

                                    // Member checkboxes
                                    members.forEach { member ->
                                        val isChecked = state.participantIds.contains(member.id)
                                        val isPayer = state.payerId == member.id

                                        KissatenReceiptCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { viewModel.toggleParticipant(member.id) }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Checkbox(
                                                        checked = isChecked,
                                                        onCheckedChange = { viewModel.toggleParticipant(member.id) },
                                                        colors = CheckboxDefaults.colors(
                                                            checkedColor = KissatenTokens.EspressoPrimary,
                                                            uncheckedColor = KissatenTokens.BorderWashi
                                                        )
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    UserAvatar(name = member.name, size = 32, isSelected = isChecked)
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Column {
                                                        Text(
                                                            text = member.name,
                                                            style = SplitzTypography.BodyLarge,
                                                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                                            color = KissatenTokens.EspressoPrimary
                                                        )
                                                        if (isPayer) {
                                                            Text(
                                                                text = "Payer",
                                                                style = SplitzTypography.LabelSmall,
                                                                color = KissatenTokens.CaramelSecondary,
                                                                fontWeight = FontWeight.SemiBold
                                                            )
                                                        }
                                                    }
                                                }

                                                if (isChecked) {
                                                    Text(
                                                        text = CurrencyUtils.formatPaise(perPersonPaise, currency = currentCurrency),
                                                        style = SplitzTypography.BodyMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = KissatenTokens.EspressoPrimary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            SplitType.EXACT -> {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Enter exact amounts for each member:",
                                        style = SplitzTypography.BodySmall,
                                        color = KissatenTokens.TextMuted
                                    )
                                    members.forEach { member ->
                                        var exactVal by remember { mutableStateOf("") }
                                        KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
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
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = member.name,
                                                        style = SplitzTypography.BodyMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = KissatenTokens.EspressoPrimary
                                                    )
                                                }
                                                OutlinedTextField(
                                                    value = exactVal,
                                                    onValueChange = { input ->
                                                        exactVal = input
                                                        val paise = CurrencyUtils.parseCurrencyInput(input, currentCurrency) ?: 0L
                                                        viewModel.setExactAmount(member.id, paise)
                                                    },
                                                    label = { Text("${currentCurrency.symbol} Amount") },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                                    modifier = Modifier.width(130.dp),
                                                    shape = RoundedCornerShape(12.dp),
                                                    singleLine = true,
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = KissatenTokens.EspressoPrimary,
                                                        unfocusedBorderColor = KissatenTokens.BorderWashi
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            SplitType.PERCENTAGE -> {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Enter percentage share (%) for each member:",
                                        style = SplitzTypography.BodySmall,
                                        color = KissatenTokens.TextMuted
                                    )
                                    members.forEach { member ->
                                        var percentVal by remember { mutableStateOf("") }
                                        KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
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
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = member.name,
                                                        style = SplitzTypography.BodyMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = KissatenTokens.EspressoPrimary
                                                    )
                                                }
                                                OutlinedTextField(
                                                    value = percentVal,
                                                    onValueChange = { input ->
                                                        percentVal = input
                                                        val bp = CurrencyUtils.parsePercentageToBasisPoints(input)
                                                        viewModel.setPercentageBp(member.id, bp)
                                                    },
                                                    label = { Text("% Share") },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    modifier = Modifier.width(110.dp),
                                                    shape = RoundedCornerShape(12.dp),
                                                    singleLine = true,
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = KissatenTokens.EspressoPrimary,
                                                        unfocusedBorderColor = KissatenTokens.BorderWashi
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            SplitType.SHARES -> {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Enter share portions for each member:",
                                        style = SplitzTypography.BodySmall,
                                        color = KissatenTokens.TextMuted
                                    )
                                    members.forEach { member ->
                                        var shareCount by remember { mutableStateOf(1) }
                                        LaunchedEffect(Unit) {
                                            viewModel.setShares(member.id, 1)
                                        }
                                        KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
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
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = member.name,
                                                        style = SplitzTypography.BodyMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = KissatenTokens.EspressoPrimary
                                                    )
                                                }
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Surface(
                                                        shape = CircleShape,
                                                        color = KissatenTokens.PaperSurface,
                                                        border = androidx.compose.foundation.BorderStroke(1.dp, KissatenTokens.BorderWashi),
                                                        modifier = Modifier.clickable {
                                                            if (shareCount > 0) {
                                                                shareCount--
                                                                viewModel.setShares(member.id, shareCount)
                                                            }
                                                        }
                                                    ) {
                                                        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                                                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = KissatenTokens.EspressoPrimary, modifier = Modifier.size(16.dp))
                                                        }
                                                    }
                                                    Text(
                                                        text = "$shareCount shares",
                                                        style = SplitzTypography.TitleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = KissatenTokens.EspressoPrimary,
                                                        modifier = Modifier.padding(horizontal = 12.dp)
                                                    )
                                                    Surface(
                                                        shape = CircleShape,
                                                        color = KissatenTokens.PaperSurface,
                                                        border = androidx.compose.foundation.BorderStroke(1.dp, KissatenTokens.BorderWashi),
                                                        modifier = Modifier.clickable {
                                                            shareCount++
                                                            viewModel.setShares(member.id, shareCount)
                                                        }
                                                    ) {
                                                        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                                                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = KissatenTokens.EspressoPrimary, modifier = Modifier.size(16.dp))
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

                    // Item 6: Summary Well
                    item {
                        val payerName = members.firstOrNull { it.id == state.payerId }?.name ?: "Payer"
                        KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Paid in full by $payerName",
                                        style = SplitzTypography.BodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = KissatenTokens.EspressoPrimary
                                    )
                                    MoneyText(
                                        amountPaise = state.amountPaise,
                                        style = SplitzTypography.TitleMedium,
                                        fontWeight = FontWeight.Bold,
                                        colorMode = MoneyColorMode.NEUTRAL
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                KissatenPerforationDivider()
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Split between ${state.participantIds.size} members",
                                        style = SplitzTypography.BodySmall,
                                        color = KissatenTokens.TextMuted
                                    )
                                    Text(
                                        text = "Expense Voucher",
                                        style = SplitzTypography.LabelSmall,
                                        color = KissatenTokens.CaramelSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Validation error notice
                    if (state.validationError != null) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = KissatenTokens.VermilionRed.copy(alpha = 0.1f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, KissatenTokens.VermilionRed.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = "Error",
                                        tint = KissatenTokens.VermilionRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = state.validationError!!,
                                        style = SplitzTypography.BodyMedium,
                                        color = KissatenTokens.VermilionRed,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Sticky Bottom CTA: Physical Stamp Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SplitzColors.background().copy(alpha = 0.95f))
                        .imePadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    KissatenPrimaryPillButton(
                        text = if (state.isSaving) "Saving..." else "Record Expense",
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            viewModel.saveExpense {
                                onExpenseAdded()
                            }
                        },
                        icon = Icons.Default.Check,
                        enabled = !state.isSaving,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
