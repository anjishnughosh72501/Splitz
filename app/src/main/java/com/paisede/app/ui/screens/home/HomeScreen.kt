package com.paisede.app.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.AvatarStack
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SectionHeader
import com.paisede.app.ui.components.SplitzGlassCard
import com.paisede.app.ui.components.SplitzSurface
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.SplitzBorderWidth
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzGradients
import com.paisede.app.ui.theme.SplitzMotion
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.GroupCardData
import com.paisede.app.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userPreferences: com.paisede.app.data.preferences.UserPreferences? = null,
    onCreateGroupClick: () -> Unit,
    onGroupClick: (String) -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val currentUserName by (userPreferences?.userName ?: remember { kotlinx.coroutines.flow.MutableStateFlow("") }).collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var groupToDelete by remember { mutableStateOf<GroupCardData?>(null) }
    var groupForMembersDialog by remember { mutableStateOf<GroupCardData?>(null) }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    // Confirmation Dialog for Group Deletion
    if (groupToDelete != null) {
        AlertDialog(
            onDismissRequest = { groupToDelete = null },
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
                    text = "Are you sure you want to delete \"${groupToDelete?.group?.name}\"? All members, expenses, balances, and records will be permanently removed.",
                    style = SplitzTypography.BodyMedium,
                    color = SplitzColors.textSecondary()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val toDelete = groupToDelete
                        groupToDelete = null
                        toDelete?.let { viewModel.deleteGroup(it.group.id) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DebtRed),
                    shape = RoundedCornerShape(SplitzShapes.button)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { groupToDelete = null }) {
                    Text("Cancel", color = SplitzColors.textSecondary())
                }
            }
        )
    }

    // Members Bottom Sheet / Dialog
    if (groupForMembersDialog != null) {
        val groupData = groupForMembersDialog!!
        AlertDialog(
            onDismissRequest = { groupForMembersDialog = null },
            shape = RoundedCornerShape(SplitzShapes.sheet),
            containerColor = SplitzColors.surface(),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(name = groupData.group.name, size = 36)
                    Spacer(modifier = Modifier.width(SplitzSpacing.sm))
                    Text(
                        text = groupData.group.name,
                        style = SplitzTypography.TitleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(SplitzSpacing.sm)
                ) {
                    Text(
                        text = "Members (${groupData.members.size})",
                        style = SplitzTypography.LabelLarge,
                        color = SplitzColors.textSecondary(),
                        fontWeight = FontWeight.SemiBold
                    )
                    if (groupData.members.isEmpty()) {
                        Text(
                            text = "No members in this group",
                            style = SplitzTypography.BodyMedium,
                            color = SplitzColors.textMuted()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 260.dp),
                            verticalArrangement = Arrangement.spacedBy(SplitzSpacing.xs)
                        ) {
                            items(groupData.members) { member ->
                                val displayName = when {
                                    member.name.startsWith("Me (") -> member.name
                                    member.name.equals(currentUserName, ignoreCase = true) && currentUserName.isNotBlank() -> "Me ($currentUserName)"
                                    else -> member.name
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(SplitzShapes.cardSmall))
                                        .background(SplitzColors.surfaceVariant().copy(alpha = 0.5f))
                                        .padding(horizontal = SplitzSpacing.md, vertical = SplitzSpacing.xs),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    UserAvatar(name = displayName, size = 32)
                                    Spacer(modifier = Modifier.width(SplitzSpacing.sm))
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
                TextButton(onClick = { groupForMembersDialog = null }) {
                    Text("Close", color = SplitzColors.primary(), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    val totalNetBalance = state.groups.sumOf { it.userNetBalancePaise }
    val totalSpending = state.groups.sumOf { it.totalSpendingPaise }
    val totalMembers = state.groups.sumOf { it.memberCount }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onCreateGroupClick)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Group",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "New Group",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SplitzColors.background()),
            contentPadding = PaddingValues(horizontal = SplitzSpacing.lg, vertical = SplitzSpacing.md),
            verticalArrangement = Arrangement.spacedBy(SplitzSpacing.md)
        ) {
            // Header Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = SplitzSpacing.xs),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentUserName.isNotBlank()) "Hello, $currentUserName" else "Splitz",
                            style = SplitzTypography.HeadlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Text(
                            text = if (currentUserName.isNotBlank()) "Splitz • Financial clarity among friends" else "Financial clarity among friends",
                            style = SplitzTypography.BodyMedium,
                            color = SplitzColors.textSecondary()
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(SplitzShapes.Pill))
                                .background(SplitzColors.primary().copy(alpha = 0.12f))
                                .border(SplitzBorderWidth.thin, SplitzColors.primary().copy(alpha = 0.3f), RoundedCornerShape(SplitzShapes.Pill))
                                .clickable {
                                    viewModel.loadSampleData { groupId ->
                                        onGroupClick(groupId)
                                    }
                                }
                                .padding(horizontal = SplitzSpacing.sm, vertical = SplitzSpacing.xxs)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Demo Data",
                                    tint = SplitzColors.primary(),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Demo",
                                    style = SplitzTypography.LabelMedium,
                                    color = SplitzColors.primary(),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(SplitzSpacing.xs))

                        IconButton(onClick = onSettingsClick) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SplitzColors.surfaceGlass())
                                    .border(SplitzBorderWidth.thin, SplitzColors.borderGlass(), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings & Help",
                                    tint = SplitzColors.textSecondary(),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Hero Financial Overview Card
            item {
                HeroBalanceOverview(
                    netBalancePaise = totalNetBalance,
                    totalSpendingPaise = totalSpending,
                    groupCount = state.groups.size,
                    memberCount = totalMembers
                )
            }

            // Groups Section Header
            item {
                SectionHeader(
                    title = "Your Groups",
                    actionText = if (state.groups.size > 3) "${state.groups.size} active" else null,
                    onActionClick = {}
                )
            }

            // Group Cards or Modern Empty State
            if (state.groups.isEmpty() && !state.isLoading) {
                item {
                    EmptyStateView(
                        title = "Welcome to Splitz",
                        subtitle = "Split expenses, track balances, and settle up without the headache. Create your first group or tap 'Demo' to try sample data.",
                        icon = Icons.Default.Group,
                        actionButtonText = "+ Create Group",
                        onActionClick = onCreateGroupClick,
                        modifier = Modifier.padding(vertical = SplitzSpacing.xl)
                    )
                }
            } else {
                items(state.groups, key = { it.group.id }) { cardData ->
                    ModernGroupCard(
                        data = cardData,
                        onClick = { onGroupClick(cardData.group.id) },
                        onDeleteClick = { groupToDelete = cardData },
                        onMembersClick = { groupForMembersDialog = cardData }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun HeroBalanceOverview(
    netBalancePaise: Long,
    totalSpendingPaise: Long,
    groupCount: Int,
    memberCount: Int
) {
    val isPositive = netBalancePaise > 0L
    val isNegative = netBalancePaise < 0L

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
            .padding(SplitzSpacing.lg)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TOTAL NET BALANCE",
                        style = SplitzTypography.LabelMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MoneyText(
                        amountPaise = netBalancePaise,
                        style = SplitzTypography.MoneyDisplay,
                        fontWeight = FontWeight.Bold,
                        colorMode = if (isPositive) MoneyColorMode.CREDIT else if (isNegative) MoneyColorMode.DEBT else MoneyColorMode.SETTLED,
                        showSign = true,
                        animate = true
                    )
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPositive) SplitzGradients.Success
                            else if (isNegative) SplitzGradients.Debt
                            else SplitzGradients.Primary
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPositive) Icons.Default.TrendingUp else if (isNegative) Icons.Default.TrendingDown else Icons.Default.Group,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(SplitzSpacing.md))
            HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
            Spacer(modifier = Modifier.height(SplitzSpacing.md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Spending",
                        style = SplitzTypography.BodySmall,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                    MoneyText(
                        amountPaise = totalSpendingPaise,
                        style = SplitzTypography.MoneySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.65f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$groupCount groups · $memberCount people",
                        style = SplitzTypography.BodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
fun ModernGroupCard(
    data: GroupCardData,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMembersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val netBalance = data.userNetBalancePaise
    val statusText = when {
        netBalance > 0 -> "You're owed"
        netBalance < 0 -> "You owe"
        else -> "Settled up"
    }

    val memberNames = data.members.map { it.name }

    SplitzSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SplitzShapes.cardLarge),
        backgroundColor = SplitzColors.surface(),
        borderColor = SplitzColors.borderSubtle(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SplitzSpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAvatar(name = data.group.name, size = 46)
                    Spacer(modifier = Modifier.width(SplitzSpacing.sm))
                    Column {
                        Text(
                            text = data.group.name,
                            style = SplitzTypography.TitleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onMembersClick() }
                        ) {
                            Text(
                                text = "${data.memberCount} members",
                                style = SplitzTypography.BodySmall,
                                color = SplitzColors.textSecondary()
                            )
                            if (memberNames.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(SplitzSpacing.sm))
                                AvatarStack(names = memberNames, avatarSize = 20, maxVisible = 3)
                            }
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Group",
                            tint = DebtRed.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SplitzColors.surfaceVariant().copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open Group",
                            tint = SplitzColors.textPrimary(),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(SplitzSpacing.sm))
            HorizontalDivider(color = SplitzColors.borderSubtle().copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(SplitzSpacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Spending",
                        style = SplitzTypography.LabelMedium,
                        color = SplitzColors.textSecondary()
                    )
                    MoneyText(
                        amountPaise = data.totalSpendingPaise,
                        style = SplitzTypography.MoneyMedium,
                        fontWeight = FontWeight.SemiBold,
                        colorMode = MoneyColorMode.NEUTRAL
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = statusText,
                        style = SplitzTypography.LabelMedium,
                        color = SplitzColors.textSecondary()
                    )
                    MoneyText(
                        amountPaise = data.userNetBalancePaise,
                        style = SplitzTypography.MoneyMedium,
                        fontWeight = FontWeight.Bold,
                        showSign = true
                    )
                }
            }
        }
    }
}

// Backward compatible GroupCard
@Composable
fun GroupCard(
    data: GroupCardData,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMembersClick: () -> Unit
) {
    ModernGroupCard(
        data = data,
        onClick = onClick,
        onDeleteClick = onDeleteClick,
        onMembersClick = onMembersClick
    )
}
