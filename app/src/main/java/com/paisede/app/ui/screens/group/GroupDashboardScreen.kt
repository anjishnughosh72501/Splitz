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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

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

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Delete Group?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${state.group?.name ?: "this group"}\"? All expenses, balances, and member records will be permanently removed.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    colors = ButtonDefaults.buttonColors(containerColor = DebtRed)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                    // Undo & Redo top bar actions
                    IconButton(
                        onClick = { viewModel.undo() },
                        enabled = state.canUndo
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo",
                            tint = if (state.canUndo) BrandTeal else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                    IconButton(
                        onClick = { viewModel.redo() },
                        enabled = state.canRedo
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Redo,
                            contentDescription = "Redo",
                            tint = if (state.canRedo) BrandTeal else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Group",
                            tint = DebtRed.copy(alpha = 0.85f)
                        )
                    }
                    IconButton(
                        onClick = onSettingsClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings & Help",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpenseClick,
                containerColor = BrandTeal,
                contentColor = Color.White
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
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Summary Hero Card
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else Slate900
                    ),
                    border = if (isSystemInDarkTheme()) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TOTAL SPENDING",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.LightGray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                MoneyText(
                                    amountPaise = state.totalSpendingPaise,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    colorMode = MoneyColorMode.NEUTRAL
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceVariant else Slate800)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${state.members.size} Members",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        androidx.compose.material3.HorizontalDivider(color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.outlineVariant else Slate800)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Status:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.LightGray
                            )
                            val userStatus = when {
                                state.userNetBalancePaise > 0 -> "You're owed"
                                state.userNetBalancePaise < 0 -> "You owe"
                                else -> "Settled Up"
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "$userStatus ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                                MoneyText(
                                    amountPaise = state.userNetBalancePaise,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    showSign = true
                                )
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
                        .clickable { onSimplifyClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandTeal),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
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
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Minimize group payments",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Simplify Debts",
                            tint = Color.White
                        )
                    }
                }
            }

            // Action Grid Cards
            item {
                ActionTile(
                    title = "Balances",
                    subtitle = "Net balance per member",
                    icon = Icons.Default.AccountBalanceWallet,
                    iconBg = CreditGreen.copy(alpha = 0.15f),
                    iconTint = CreditGreen,
                    onClick = onBalancesClick
                )
            }

            item {
                ActionTile(
                    title = "Settle Up",
                    subtitle = "Record payments",
                    icon = Icons.Default.Handshake,
                    iconBg = AccentAmber.copy(alpha = 0.15f),
                    iconTint = AccentAmber,
                    onClick = onSettleUpClick
                )
            }

            item {
                ActionTile(
                    title = "Expenses",
                    subtitle = "All expenses",
                    icon = Icons.Default.ReceiptLong,
                    iconBg = MaterialTheme.colorScheme.surfaceVariant,
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = onExpensesClick
                )
            }

            item {
                ActionTile(
                    title = "Analytics",
                    subtitle = "Spending breakdown",
                    icon = Icons.Default.PieChart,
                    iconBg = IndigoPurple.copy(alpha = 0.15f),
                    iconTint = IndigoPurple,
                    onClick = onAnalyticsClick
                )
            }

            item {
                ActionTile(
                    title = "Debt Graph",
                    subtitle = "Payment network",
                    icon = Icons.Default.Hub,
                    iconBg = BrandTeal.copy(alpha = 0.15f),
                    iconTint = BrandTeal,
                    onClick = onGraphClick
                )
            }

            item {
                ActionTile(
                    title = "Settings & Help",
                    subtitle = "Guides & DS inspect",
                    icon = Icons.Default.HelpOutline,
                    iconBg = MaterialTheme.colorScheme.surfaceVariant,
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
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
fun ActionTile(
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
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
