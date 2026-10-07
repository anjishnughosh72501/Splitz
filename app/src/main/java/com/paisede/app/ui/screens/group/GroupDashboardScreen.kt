package com.paisede.app.ui.screens.group

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.theme.AccentAmber
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.IndigoPurple
import com.paisede.app.ui.theme.Slate800
import com.paisede.app.ui.theme.Slate900
import com.paisede.app.ui.viewmodel.GroupViewModel
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.paisede.app.ui.components.UserAvatar

@Composable
fun GroupDashboardScreen(
    viewModel: GroupViewModel,
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
            shape = RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.sheet),
            containerColor = com.paisede.app.ui.theme.SplitzColors.surface(),
            title = {
                Text(
                    text = "Delete Group?",
                    style = com.paisede.app.ui.theme.SplitzTypography.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = com.paisede.app.ui.theme.SplitzColors.textPrimary()
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${state.group?.name ?: "this group"}\"? All expenses, balances, and member records will be permanently removed.",
                    style = com.paisede.app.ui.theme.SplitzTypography.BodyMedium,
                    color = com.paisede.app.ui.theme.SplitzColors.textSecondary()
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
                    colors = ButtonDefaults.buttonColors(containerColor = DebtRed),
                    shape = RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.button)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = com.paisede.app.ui.theme.SplitzColors.textSecondary())
                }
            }
        )
    }

    if (showMembersDialog) {
        AlertDialog(
            onDismissRequest = { showMembersDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = BrandTeal,
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    text = state.group?.name ?: "Group Members",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Members (${state.members.size})",
                        style = com.paisede.app.ui.theme.SplitzTypography.LabelLarge,
                        color = com.paisede.app.ui.theme.SplitzColors.textSecondary(),
                        fontWeight = FontWeight.SemiBold
                    )
                    if (state.members.isEmpty()) {
                        Text(
                            text = "No members in this group",
                            style = com.paisede.app.ui.theme.SplitzTypography.BodyMedium,
                            color = com.paisede.app.ui.theme.SplitzColors.textMuted()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.members) { member ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.SM))
                                        .background(com.paisede.app.ui.theme.SplitzColors.surfaceElevated())
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    UserAvatar(name = member.name, size = 36)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = member.name,
                                        style = com.paisede.app.ui.theme.SplitzTypography.BodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = com.paisede.app.ui.theme.SplitzColors.textPrimary()
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
                        color = com.paisede.app.ui.theme.SplitzColors.primary(),
                        fontWeight = FontWeight.SemiBold,
                        style = com.paisede.app.ui.theme.SplitzTypography.LabelLarge
                    )
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SplitzTopAppBar(
                title = state.group?.name ?: "Group Dashboard",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { viewModel.undo() },
                        enabled = state.canUndo
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo",
                            tint = if (state.canUndo) com.paisede.app.ui.theme.SplitzColors.primary() else com.paisede.app.ui.theme.SplitzColors.textMuted()
                        )
                    }
                    IconButton(
                        onClick = { viewModel.redo() },
                        enabled = state.canRedo
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Redo,
                            contentDescription = "Redo",
                            tint = if (state.canRedo) com.paisede.app.ui.theme.SplitzColors.primary() else com.paisede.app.ui.theme.SplitzColors.textMuted()
                        )
                    }
                    IconButton(
                        onClick = { showMembersDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "Show Members",
                            tint = com.paisede.app.ui.theme.SplitzColors.textPrimary()
                        )
                    }
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Group",
                            tint = com.paisede.app.ui.theme.SplitzColors.negative()
                        )
                    }
                    IconButton(
                        onClick = onSettingsClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings & Help",
                            tint = com.paisede.app.ui.theme.SplitzColors.textPrimary()
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpenseClick,
                containerColor = com.paisede.app.ui.theme.SplitzColors.primary(),
                contentColor = Color.White,
                shape = RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.fab)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(com.paisede.app.ui.theme.SplitzColors.background()),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Summary Hero Card
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.cardHero),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.paisede.app.ui.theme.SplitzColors.borderSubtle()),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF0F172A),
                                        Color(0xFF1E293B),
                                        Color(0xFF0D252E)
                                    )
                                )
                            )
                            .padding(22.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "TOTAL SPENDING",
                                        style = com.paisede.app.ui.theme.SplitzTypography.LabelMedium,
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 1.2.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    MoneyText(
                                        amountPaise = state.totalSpendingPaise,
                                        style = com.paisede.app.ui.theme.SplitzTypography.MoneyDisplay,
                                        fontWeight = FontWeight.ExtraBold,
                                        colorMode = MoneyColorMode.NEUTRAL,
                                        animate = true
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.SM))
                                        .background(Color.White.copy(alpha = 0.12f))
                                        .clickable { showMembersDialog = true }
                                        .padding(horizontal = 12.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = "Show Members",
                                        tint = BrandTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${state.members.size} Members",
                                        style = com.paisede.app.ui.theme.SplitzTypography.LabelMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            androidx.compose.material3.HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Your Net Balance",
                                    style = com.paisede.app.ui.theme.SplitzTypography.BodyMedium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                val (statusText, statusBg, statusColor) = when {
                                    state.userNetBalancePaise > 0 -> Triple("You are owed", CreditGreen.copy(alpha = 0.2f), CreditGreen)
                                    state.userNetBalancePaise < 0 -> Triple("You owe", DebtRed.copy(alpha = 0.2f), DebtRed)
                                    else -> Triple("Settled up", Color.White.copy(alpha = 0.12f), Color.White)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.Pill))
                                            .background(statusBg)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = statusText,
                                            style = com.paisede.app.ui.theme.SplitzTypography.LabelSmall,
                                            color = statusColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    MoneyText(
                                        amountPaise = state.userNetBalancePaise,
                                        style = com.paisede.app.ui.theme.SplitzTypography.TitleMedium,
                                        fontWeight = FontWeight.Bold,
                                        showSign = true,
                                        animate = true
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Flagship Feature: Simplify Debts Hero Action Card
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.MD))
                        .clickable { onSimplifyClick() },
                    shape = RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.MD),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.paisede.app.ui.theme.SplitzColors.primary().copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = com.paisede.app.ui.theme.SplitzGradients.Primary)
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.22f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "Simplify Debts",
                                        style = com.paisede.app.ui.theme.SplitzTypography.TitleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Minimize group transactions with one tap",
                                        style = com.paisede.app.ui.theme.SplitzTypography.BodySmall,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Simplify Debts",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Action Grid Cards
            item {
                ModernActionTile(
                    title = "Balances",
                    subtitle = "Net by member",
                    icon = Icons.Default.AccountBalanceWallet,
                    iconTint = CreditGreen,
                    iconBg = CreditGreen.copy(alpha = 0.15f),
                    onClick = onBalancesClick
                )
            }

            item {
                ModernActionTile(
                    title = "Settle Up",
                    subtitle = "Record payments",
                    icon = Icons.Default.Handshake,
                    iconTint = AccentAmber,
                    iconBg = AccentAmber.copy(alpha = 0.15f),
                    onClick = onSettleUpClick
                )
            }

            item {
                ModernActionTile(
                    title = "Expenses",
                    subtitle = "All activity",
                    icon = Icons.Default.ReceiptLong,
                    iconTint = BrandTeal,
                    iconBg = BrandTeal.copy(alpha = 0.15f),
                    onClick = onExpensesClick
                )
            }

            item {
                ModernActionTile(
                    title = "Analytics",
                    subtitle = "Spending insights",
                    icon = Icons.Default.PieChart,
                    iconTint = IndigoPurple,
                    iconBg = IndigoPurple.copy(alpha = 0.15f),
                    onClick = onAnalyticsClick
                )
            }

            item {
                ModernActionTile(
                    title = "Debt Graph",
                    subtitle = "Payment network",
                    icon = Icons.Default.Hub,
                    iconTint = BrandTeal,
                    iconBg = BrandTeal.copy(alpha = 0.15f),
                    onClick = onGraphClick
                )
            }

            item {
                ModernActionTile(
                    title = "Settings & Help",
                    subtitle = "Inspect & guides",
                    icon = Icons.Default.HelpOutline,
                    iconTint = com.paisede.app.ui.theme.SplitzColors.textSecondary(),
                    iconBg = com.paisede.app.ui.theme.SplitzColors.surfaceElevated(),
                    onClick = onSettingsClick
                )
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun ModernActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.MD))
            .clickable { onClick() },
        shape = RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.MD),
        colors = CardDefaults.cardColors(containerColor = com.paisede.app.ui.theme.SplitzColors.surface()),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.paisede.app.ui.theme.SplitzColors.borderSubtle())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(com.paisede.app.ui.theme.SplitzShapes.SM))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = com.paisede.app.ui.theme.SplitzTypography.TitleMedium,
                fontWeight = FontWeight.Bold,
                color = com.paisede.app.ui.theme.SplitzColors.textPrimary()
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                style = com.paisede.app.ui.theme.SplitzTypography.BodySmall,
                color = com.paisede.app.ui.theme.SplitzColors.textSecondary()
            )
        }
    }
}
