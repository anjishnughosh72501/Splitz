package com.paisede.app.ui.screens.group

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.AvatarStack
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SplitzActionCard
import com.paisede.app.ui.components.SplitzGlassCard
import com.paisede.app.ui.components.SplitzSurface
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.AccentAmber
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.IndigoPurple
import com.paisede.app.ui.theme.SplitzBorderWidth
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzGradients
import com.paisede.app.ui.theme.SplitzMotion
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.GroupViewModel

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
            shape = RoundedCornerShape(SplitzShapes.sheet),
            containerColor = SplitzColors.surface(),
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
                    colors = ButtonDefaults.buttonColors(containerColor = DebtRed),
                    shape = RoundedCornerShape(SplitzShapes.button)
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
            icon = {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = SplitzColors.primary(),
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
                        style = SplitzTypography.LabelLarge,
                        color = SplitzColors.textSecondary(),
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
                                val displayName = when {
                                    member.name.startsWith("Me (") -> member.name
                                    member.name.equals(currentUserName, ignoreCase = true) && currentUserName.isNotBlank() -> "Me ($currentUserName)"
                                    else -> member.name
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(SplitzShapes.SM))
                                        .background(SplitzColors.surfaceElevated())
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    UserAvatar(name = displayName, size = 36)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = displayName,
                                        style = SplitzTypography.BodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = SplitzColors.textPrimary()
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
                        color = SplitzColors.primary(),
                        fontWeight = FontWeight.SemiBold,
                        style = SplitzTypography.LabelLarge
                    )
                }
            }
        )
    }

    val memberNames = state.members.map { it.name }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SplitzTopAppBar(
                title = state.group?.name ?: "Group Dashboard",
                subtitle = "${state.members.size} members",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { showMembersDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "Group Members",
                            tint = SplitzColors.textPrimary()
                        )
                    }
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Group",
                            tint = SplitzColors.negative()
                        )
                    }
                    IconButton(
                        onClick = onSettingsClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings & Help",
                            tint = SplitzColors.textPrimary()
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val fabScale by animateFloatAsState(
                targetValue = if (isPressed) 0.94f else 1.0f,
                animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
                label = "FabScale"
            )

            Box(
                modifier = Modifier
                    .scale(fabScale)
                    .clip(RoundedCornerShape(SplitzShapes.LG))
                    .background(SplitzGradients.Primary)
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onAddExpenseClick)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Expense",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Expense",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SplitzColors.background()),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Group Hero Balance Card
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(SplitzShapes.cardHero))
                        .background(SplitzGradients.Hero)
                        .border(
                            width = SplitzBorderWidth.thin,
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(SplitzShapes.cardHero)
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
                                    style = SplitzTypography.LabelMedium,
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                MoneyText(
                                    amountPaise = state.totalSpendingPaise,
                                    style = SplitzTypography.MoneyDisplay,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    animate = true
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Position",
                                style = SplitzTypography.BodyMedium,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                            val (statusText, statusBg, statusColor) = when {
                                state.userNetBalancePaise > 0 -> Triple("You're owed", CreditGreen.copy(alpha = 0.22f), CreditGreen)
                                state.userNetBalancePaise < 0 -> Triple("You owe", DebtRed.copy(alpha = 0.22f), DebtRed)
                                else -> Triple("Settled up", Color.White.copy(alpha = 0.12f), Color.White)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(SplitzShapes.Pill))
                                        .background(statusBg)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = statusText,
                                        style = SplitzTypography.LabelSmall,
                                        color = statusColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                MoneyText(
                                    amountPaise = state.userNetBalancePaise,
                                    style = SplitzTypography.TitleMedium,
                                    fontWeight = FontWeight.Bold,
                                    showSign = true,
                                    animate = true
                                )
                            }
                        }
                    }
                }
            }

            // Flagship Feature: Simplify Debts Hero Interactive Panel
            item(span = { GridItemSpan(2) }) {
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val simplifyScale by animateFloatAsState(
                    targetValue = if (isPressed) 0.985f else 1.0f,
                    animationSpec = tween(SplitzMotion.Duration.Fast, easing = SplitzMotion.Easing.Standard),
                    label = "SimplifyScale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(simplifyScale)
                        .clip(RoundedCornerShape(SplitzShapes.LG))
                        .background(SplitzGradients.Primary)
                        .clickable(interactionSource = interactionSource, indication = null, onClick = onSimplifyClick)
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
                                    text = "✦ Simplify Debts",
                                    style = SplitzTypography.TitleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Minimize group transfers with one tap",
                                    style = SplitzTypography.BodySmall,
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

            // Primary Tier: Financial Actions
            item {
                SplitzActionCard(
                    title = "Balances",
                    subtitle = "Member breakdown",
                    icon = Icons.Default.AccountBalanceWallet,
                    iconTint = CreditGreen,
                    onClick = onBalancesClick
                )
            }

            item {
                SplitzActionCard(
                    title = "Settle Up",
                    subtitle = "Record payments",
                    icon = Icons.Default.Handshake,
                    iconTint = AccentAmber,
                    onClick = onSettleUpClick
                )
            }

            // Secondary Tier: History & Visualization
            item {
                SplitzActionCard(
                    title = "Expenses",
                    subtitle = "Full timeline",
                    icon = Icons.Default.ReceiptLong,
                    iconTint = SplitzColors.primary(),
                    onClick = onExpensesClick
                )
            }

            item {
                SplitzActionCard(
                    title = "Debt Graph",
                    subtitle = "Payment network",
                    icon = Icons.Default.Hub,
                    iconTint = SplitzColors.primary(),
                    onClick = onGraphClick,
                    badgeText = "Visual"
                )
            }

            // Tertiary Tier: Insights & Configuration
            item {
                SplitzActionCard(
                    title = "Analytics",
                    subtitle = "Spending trends",
                    icon = Icons.Default.PieChart,
                    iconTint = IndigoPurple,
                    onClick = onAnalyticsClick
                )
            }

            item {
                SplitzActionCard(
                    title = "Settings",
                    subtitle = "Inspect & prefs",
                    icon = Icons.Default.HelpOutline,
                    iconTint = SplitzColors.textSecondary(),
                    onClick = onSettingsClick
                )
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}
