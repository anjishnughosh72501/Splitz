package com.paisede.app.ui.screens.group

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.domain.model.Category
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.ui.components.KissatenAddExpenseStickyDrawer
import com.paisede.app.ui.components.KissatenBottomBar
import com.paisede.app.ui.components.KissatenDebtBanner
import com.paisede.app.ui.components.KissatenGroupSelectorCapsule
import com.paisede.app.ui.components.KissatenMemberBalanceCard
import com.paisede.app.ui.components.KissatenNavTab
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.GroupViewModel
import com.paisede.app.util.CurrencyUtils
import com.paisede.app.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDashboardScreen(
    viewModel: GroupViewModel,
    userPreferences: com.paisede.app.data.preferences.UserPreferences? = null,
    onNavigateBack: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onBalancesClick: () -> Unit,
    onSimplifyClick: () -> Unit,
    onSettleUpClick: () -> Unit,
    onExpensesClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onGraphClick: () -> Unit,
    onDebugClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val currentUserName by (userPreferences?.userName ?: remember { kotlinx.coroutines.flow.MutableStateFlow("") }).collectAsState()
    val currentCurrency = com.paisede.app.ui.theme.LocalAppCurrency.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showMembersDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = SplitzColors.surfaceElevated(),
            title = {
                Text(
                    text = "Delete Group?",
                    style = SplitzTypography.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.textPrimary()
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${state.group?.name ?: "this group"}\"? All expenses, balances, and member records will be permanently removed.",
                    style = SplitzTypography.BodyMedium,
                    color = SplitzColors.textSecondary()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteGroup {
                            onNavigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SplitzColors.negative()),
                    shape = RoundedCornerShape(SplitzShapes.Pill)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = SplitzColors.textSecondary())
                }
            }
        )
    }

    if (showMembersDialog) {
        AlertDialog(
            onDismissRequest = { showMembersDialog = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = SplitzColors.surfaceElevated(),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(SplitzColors.brandSoft()),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = SplitzColors.brandPrimary(),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = state.group?.name ?: "Group Members",
                        style = SplitzTypography.TitleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Members (${state.members.size} Splitters)",
                        style = SplitzTypography.LabelLarge,
                        color = SplitzColors.brandAccent(),
                        fontWeight = FontWeight.SemiBold
                    )
                    if (state.members.isEmpty()) {
                        Text(
                            text = "No members in this group",
                            style = SplitzTypography.BodyMedium,
                            color = SplitzColors.textMuted()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.members) { member ->
                                val isMe = member.name.startsWith("Me (") || (currentUserName.isNotBlank() && member.name.equals(currentUserName, ignoreCase = true))
                                val memberBal = state.balances[member.id] ?: 0L
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SplitzColors.backgroundElevated())
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        UserAvatar(name = member.name, size = 34)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = member.name,
                                                    style = SplitzTypography.BodyLarge,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = SplitzColors.textPrimary()
                                                )
                                                if (isMe) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(SplitzShapes.Pill))
                                                            .background(SplitzColors.brandSoft())
                                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                                    ) {
                                                        Text(
                                                            text = "You",
                                                            style = SplitzTypography.Caption,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = SplitzColors.brandPrimary()
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    MoneyText(
                                        amountPaise = memberBal,
                                        style = SplitzTypography.BodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        showSign = true
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMembersDialog = false }) {
                    Text(
                        "Close",
                        color = SplitzColors.brandPrimary(),
                        fontWeight = FontWeight.Bold,
                        style = SplitzTypography.LabelLarge
                    )
                }
            }
        )
    }

    val debtorCount = state.balances.values.count { it < 0 }
    val beforeDebts = if (debtorCount > 0) debtorCount else state.members.size
    val userNetBal = state.userNetBalancePaise
    val isReceiving = userNetBal > 0
    val isOwing = userNetBal < 0
    val groupName = state.group?.name ?: "Kissaten Crawl"

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SplitzTopAppBar(
                centerTitle = false,
                onNavigateBack = onNavigateBack,
                titleContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SplitzColors.brandSoft()),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalCafe,
                                contentDescription = null,
                                tint = SplitzColors.brandPrimary(),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.group?.name ?: "Group",
                                style = SplitzTypography.TitleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${state.members.size} members",
                                style = SplitzTypography.Caption,
                                color = SplitzColors.textMuted(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showMembersDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "Members",
                            tint = SplitzColors.textPrimary()
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = SplitzColors.textPrimary()
                        )
                    }
                }
            )
        },
        bottomBar = {
            KissatenBottomBar(
                currentTab = KissatenNavTab.GROUPS,
                onTabSelected = { tab ->
                    when (tab) {
                        KissatenNavTab.GROUPS -> {} // Current
                        KissatenNavTab.SETTLE_UP -> onSettleUpClick()
                        KissatenNavTab.ACTIVITY -> onExpensesClick()
                        KissatenNavTab.MY_LEDGER -> onAnalyticsClick()
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Active Trip Capsule Selector
                item {
                    KissatenGroupSelectorCapsule(
                        groupName = groupName,
                        memberCount = state.members.size,
                        onSwitchClick = onNavigateBack,
                        japaneseSubtitle = "${groupName} • ${state.members.size} members"
                    )
                }

                // 2. Paper Receipt Summary Card
                item {
                    KissatenReceiptCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Spending & Net Balance Grid (2 Columns)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Total Group Spend Well
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SplitzColors.backgroundElevated())
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "Total Group Spend",
                                        style = SplitzTypography.Caption,
                                        color = SplitzColors.textSecondary(),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = CurrencyUtils.formatPaise(state.totalSpendingPaise, currency = currentCurrency),
                                        style = SplitzTypography.TitleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textPrimary()
                                    )
                                }

                                // Your Net Balance Well
                                val balanceBg = if (isReceiving) SplitzColors.positiveContainer() else if (isOwing) SplitzColors.negativeContainer() else SplitzColors.backgroundElevated()
                                val balanceTextColor = if (isReceiving) SplitzColors.positive() else if (isOwing) SplitzColors.negative() else SplitzColors.textPrimary()
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(balanceBg)
                                        .padding(12.dp)
                                 ) {
                                    Text(
                                        text = "Your Net Balance",
                                        style = SplitzTypography.Caption,
                                        color = if (isReceiving) SplitzColors.positive() else if (isOwing) SplitzColors.negative() else SplitzColors.textSecondary(),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = CurrencyUtils.formatPaise(userNetBal, showSign = true, currency = currentCurrency),
                                        style = SplitzTypography.TitleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = balanceTextColor
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        if (isReceiving) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDownward,
                                                contentDescription = null,
                                                tint = SplitzColors.positive(),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = "You are owed",
                                                style = SplitzTypography.Caption,
                                                fontSize = 11.sp,
                                                color = SplitzColors.positive(),
                                                fontWeight = FontWeight.Medium
                                            )
                                        } else if (isOwing) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowUpward,
                                                contentDescription = null,
                                                tint = SplitzColors.negative(),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = "You owe",
                                                style = SplitzTypography.Caption,
                                                fontSize = 11.sp,
                                                color = SplitzColors.negative(),
                                                fontWeight = FontWeight.Medium
                                            )
                                        } else {
                                            Text(
                                                text = "Settled up (Even)",
                                                style = SplitzTypography.Caption,
                                                fontSize = 11.sp,
                                                color = SplitzColors.textMuted()
                                            )
                                        }
                                    }
                                }
                            }

                            // Receipt Perforation Cutout Divider
                            KissatenPerforationDivider(
                                punchRadius = 6.dp,
                                punchColor = SplitzColors.background(),
                                lineColor = SplitzColors.borderSubtle()
                            )

                            // Debt Optimization Banner
                            KissatenDebtBanner(
                                beforeCount = beforeDebts.coerceAtLeast(1),
                                afterCount = (beforeDebts / 2).coerceAtLeast(1),
                                onPlanClick = onSimplifyClick
                            )
                        }
                    }
                }

                // 3. Member Balances Carousel
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Member Balances",
                                style = SplitzTypography.TitleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary()
                            )
                            Text(
                                text = "${state.members.size} Splitters",
                                style = SplitzTypography.Caption,
                                color = SplitzColors.textMuted(),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp)
                        ) {
                            items(state.members) { member ->
                                val isMe = member.name.startsWith("Me (") || (currentUserName.isNotBlank() && member.name.equals(currentUserName, ignoreCase = true))
                                val memberBal = state.balances[member.id] ?: 0L
                                KissatenMemberBalanceCard(
                                    name = member.name,
                                    netBalancePaise = memberBal,
                                    isCurrentUser = isMe,
                                    onClick = onBalancesClick
                                )
                            }
                        }
                    }
                }

                // 4. Crawl Journal Header & Recent Expenses
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Recent Expenses",
                                style = SplitzTypography.TitleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary()
                            )
                        }
                        Text(
                            text = "All Receipts",
                            style = SplitzTypography.LabelSmall,
                            color = SplitzColors.brandAccent(),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onExpensesClick() }
                        )
                    }
                }

                if (state.expenses.isEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = SplitzColors.surfaceElevated(),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalCafe,
                                    contentDescription = null,
                                    tint = SplitzColors.brandAccent(),
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No receipts yet",
                                    style = SplitzTypography.TitleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SplitzColors.textPrimary()
                                )
                                Text(
                                    text = "Tap 'Add Expense' below to log your first cafe tab or meal.",
                                    style = SplitzTypography.BodySmall,
                                    color = SplitzColors.textMuted(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(state.expenses.take(4), key = { it.first.id }) { (expense, splits) ->
                        KissatenJournalEntryItem(
                            expense = expense,
                            splits = splits,
                            currentUserName = currentUserName,
                            members = state.members.associateBy { it.id },
                            currency = currentCurrency,
                            onClick = onExpensesClick
                        )
                    }
                }

                // 5. Quick Shortcuts Grid
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Quick Actions",
                            style = SplitzTypography.LabelLarge,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            KissatenActionTile(
                                title = "Settle Up",
                                subtitle = "Record Payment",
                                icon = Icons.Default.Handshake,
                                color = SplitzColors.brandAccent(),
                                onClick = onSettleUpClick,
                                modifier = Modifier.weight(1f)
                            )
                            KissatenActionTile(
                                title = "Debt Graph",
                                subtitle = "Flow Chart",
                                icon = Icons.Default.Hub,
                                color = SplitzColors.brandPrimary(),
                                onClick = onGraphClick,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            KissatenActionTile(
                                title = "Analytics",
                                subtitle = "Insights",
                                icon = Icons.Default.PieChart,
                                color = SplitzColors.positive(),
                                onClick = onAnalyticsClick,
                                modifier = Modifier.weight(1f)
                            )
                            KissatenActionTile(
                                title = "Simplify Debts",
                                subtitle = "Optimize Debts",
                                icon = Icons.Default.AutoFixHigh,
                                color = SplitzColors.brandAccent(),
                                onClick = onSimplifyClick,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }

            // Warm Floating Action Sticky Drawer at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                KissatenAddExpenseStickyDrawer(
                    onAddExpenseClick = onAddExpenseClick,
                    onScanClick = onAddExpenseClick
                )
            }
        }
    }
}

@Composable
private fun KissatenJournalEntryItem(
    expense: Expense,
    splits: List<ExpenseSplit>,
    currentUserName: String,
    members: Map<String, com.paisede.app.domain.model.Member>,
    currency: com.paisede.app.data.preferences.AppCurrency,
    onClick: () -> Unit
) {
    val payer = members[expense.payerId]
    val payerName = payer?.name ?: "Someone"
    val isPayerMe = payerName.startsWith("Me (") || (currentUserName.isNotBlank() && payerName.equals(currentUserName, ignoreCase = true))

    val mySplit = splits.firstOrNull { split ->
        val m = members[split.userId]
        m != null && (m.name.startsWith("Me (") || (currentUserName.isNotBlank() && m.name.equals(currentUserName, ignoreCase = true)))
    }

    val icon: ImageVector = when (expense.category?.lowercase()) {
        "food" -> Icons.Default.Coffee
        "travel" -> Icons.Default.Train
        "entertainment" -> Icons.Default.WineBar
        "shopping" -> Icons.Default.BakeryDining
        "bills" -> Icons.Default.Restaurant
        else -> Icons.Default.Coffee
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SplitzColors.borderSubtle(), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = SplitzColors.surfaceElevated(),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SplitzColors.backgroundElevated()),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = SplitzColors.brandPrimary(),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = expense.description,
                            style = SplitzTypography.TitleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isPayerMe) "Paid by Me (You)" else "Paid by $payerName",
                            style = SplitzTypography.Caption,
                            color = SplitzColors.textSecondary()
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyUtils.formatPaise(expense.amountPaise, currency = currency),
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    if (isPayerMe) {
                        val lentPaise = expense.amountPaise - (mySplit?.amountPaise ?: 0L)
                        Text(
                            text = "You lent ${CurrencyUtils.formatPaise(lentPaise, currency = currency)}",
                            style = SplitzTypography.Caption,
                            fontWeight = FontWeight.SemiBold,
                            color = SplitzColors.positive()
                        )
                    } else if (mySplit != null && mySplit.amountPaise > 0L) {
                        Text(
                            text = "You owe ${CurrencyUtils.formatPaise(mySplit.amountPaise, currency = currency)}",
                            style = SplitzTypography.Caption,
                            fontWeight = FontWeight.SemiBold,
                            color = SplitzColors.negative()
                        )
                    }
                }
            }

            // Bottom chip strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SplitzColors.backgroundElevated())
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${expense.splitType.name.lowercase().replaceFirstChar { it.uppercase() }} (${splits.size})",
                            style = SplitzTypography.Caption,
                            fontSize = 10.sp,
                            color = SplitzColors.textSecondary(),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = DateUtils.formatRelativeDate(expense.createdAt),
                        style = SplitzTypography.Caption,
                        fontSize = 10.sp,
                        color = SplitzColors.textMuted()
                    )
                }

                Text(
                    text = "${splits.size} shares",
                    style = SplitzTypography.Caption,
                    fontSize = 11.sp,
                    color = SplitzColors.textMuted()
                )
            }
        }
    }
}

@Composable
private fun KissatenActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, SplitzColors.borderSubtle(), RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = SplitzColors.surfaceElevated(),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = SplitzTypography.TitleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.textPrimary()
                )
                Text(
                    text = subtitle,
                    style = SplitzTypography.Caption,
                    color = SplitzColors.textMuted()
                )
            }
        }
    }
}
