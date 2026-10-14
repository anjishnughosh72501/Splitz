package com.paisede.app.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.AvatarStack
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.SectionHeader
import com.paisede.app.ui.components.ShimmerCard
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.KissatenTokens
import com.paisede.app.ui.theme.LocalAppCurrency
import com.paisede.app.ui.theme.SplitzColors
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
            shape = RoundedCornerShape(20.dp),
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
                    text = "All members, expenses, and settlement records in \"${groupToDelete?.group?.name}\" will be permanently deleted.",
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
                    colors = ButtonDefaults.buttonColors(containerColor = SplitzColors.error()),
                    shape = RoundedCornerShape(SplitzShapes.Pill)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { groupToDelete = null }) {
                    Text("Cancel", color = SplitzColors.textPrimary(), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    // Members Dialog
    if (groupForMembersDialog != null) {
        val groupData = groupForMembersDialog!!
        AlertDialog(
            onDismissRequest = { groupForMembersDialog = null },
            shape = RoundedCornerShape(20.dp),
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
                        fontWeight = FontWeight.Bold
                    )
                    if (groupData.members.isEmpty()) {
                        Text(
                            text = "No members registered yet",
                            style = SplitzTypography.BodyMedium,
                            color = SplitzColors.textTertiary()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 260.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(groupData.members) { member ->
                                val displayName = when {
                                    member.name.startsWith("Me (") -> member.name
                                    member.name.equals(currentUserName, ignoreCase = true) && currentUserName.isNotBlank() -> "Me ($currentUserName)"
                                    else -> member.name
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SplitzColors.surfaceElevated(),
                                    border = BorderStroke(1.dp, SplitzColors.border()),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        UserAvatar(name = displayName, size = 30)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = displayName,
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
            },
            confirmButton = {
                TextButton(onClick = { groupForMembersDialog = null }) {
                    Text("Close", color = SplitzColors.brandAccent(), fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    val totalNetBalance = state.groups.sumOf { it.userNetBalancePaise }
    val totalSpending = state.groups.sumOf { it.totalSpendingPaise }
    val totalMembers = state.groups.sumOf { it.memberCount }
    val listState = rememberLazyListState()
    val isFabExpanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 100
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SplitzTopAppBar(
                title = "Splitz",
                subtitle = if (currentUserName.isNotBlank()) "Welcome back, $currentUserName" else "Group expense splitting",
                centerTitle = false,
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = SplitzColors.surface(),
                        border = BorderStroke(1.dp, SplitzColors.border()),
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { onSettingsClick() }
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Open Settings",
                                tint = SplitzColors.textPrimary(),
                                modifier = Modifier.size(20.dp)
                            )
                        }
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

            Surface(
                shape = RoundedCornerShape(SplitzShapes.Pill),
                color = SplitzColors.brandAccent(),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .scale(fabScale)
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onCreateGroupClick)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = if (isFabExpanded) 20.dp else 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Group",
                        tint = SplitzColors.onBrandAccent(),
                        modifier = Modifier.size(20.dp)
                    )
                    AnimatedVisibility(visible = isFabExpanded) {
                        Row {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "New Group",
                                style = SplitzTypography.LabelLarge,
                                color = SplitzColors.onBrandAccent(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

                // Hero Financial Overview Card (Washi Receipt Card)
                item {
                    KissatenHeroBalanceOverview(
                        netBalancePaise = totalNetBalance,
                        totalSpendingPaise = totalSpending,
                        groupCount = state.groups.size,
                        memberCount = totalMembers
                    )
                }

                // Groups Section Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GROUPS (${state.groups.size})",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary(),
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                // Loading / Empty / Group Cards
                if (state.isLoading) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            ShimmerCard(height = 110.dp, cornerRadius = 20.dp)
                            ShimmerCard(height = 110.dp, cornerRadius = 20.dp)
                        }
                    }
                } else if (state.groups.isEmpty()) {
                    item {
                        KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalCafe,
                                    contentDescription = null,
                                    tint = SplitzColors.brandAccent(),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Welcome to Splitz",
                                    style = SplitzTypography.TitleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SplitzColors.textPrimary()
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Create a group below to start tracking shared expenses and settling debts seamlessly.",
                                    style = SplitzTypography.BodySmall,
                                    color = SplitzColors.textSecondary(),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                } else {
                    itemsIndexed(state.groups, key = { _, it -> it.group.id }) { index, cardData ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(cardData.group.id) {
                            delay(index * 60L)
                            visible = true
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { it / 4 }
                        ) {
                            KissatenGroupCard(
                                data = cardData,
                                onClick = { onGroupClick(cardData.group.id) },
                                onDeleteClick = { groupToDelete = cardData },
                                onMembersClick = { groupForMembersDialog = cardData }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

@Composable
private fun KissatenHeroBalanceOverview(
    netBalancePaise: Long,
    totalSpendingPaise: Long,
    groupCount: Int,
    memberCount: Int
) {
    val isPositive = netBalancePaise > 0L
    val isNegative = netBalancePaise < 0L

    KissatenReceiptCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TOTAL NET BALANCE",
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textTertiary(),
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MoneyText(
                        amountPaise = netBalancePaise,
                        style = SplitzTypography.DisplayMedium,
                        fontWeight = FontWeight.Black,
                        colorMode = if (isPositive) MoneyColorMode.CREDIT else if (isNegative) MoneyColorMode.DEBT else MoneyColorMode.SETTLED,
                        showSign = true,
                        animate = true
                    )
                }

                val balanceDescription = when {
                    isPositive -> "Total net balance: positive. You are owed money."
                    isNegative -> "Total net balance: negative. You owe money."
                    else -> "Total net balance: all settled up."
                }

                Surface(
                    shape = CircleShape,
                    color = if (isPositive) SplitzColors.positiveContainer()
                    else if (isNegative) SplitzColors.negativeContainer()
                    else SplitzColors.surfaceElevated(),
                    border = BorderStroke(
                        1.dp,
                        if (isPositive) SplitzColors.positive().copy(alpha = 0.3f)
                        else if (isNegative) SplitzColors.negative().copy(alpha = 0.3f)
                        else SplitzColors.border()
                    )
                ) {
                    Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isPositive) Icons.AutoMirrored.Filled.TrendingUp else if (isNegative) Icons.AutoMirrored.Filled.TrendingDown else Icons.Default.LocalCafe,
                            contentDescription = balanceDescription,
                            tint = if (isPositive) SplitzColors.positive() else if (isNegative) SplitzColors.negative() else SplitzColors.brandAccent(),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            KissatenPerforationDivider()
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Group Spend",
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textTertiary()
                    )
                    MoneyText(
                        amountPaise = totalSpendingPaise,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.NEUTRAL
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = SplitzColors.brandAccent(),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$groupCount groups • $memberCount members",
                        style = SplitzTypography.BodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SplitzColors.textPrimary()
                    )
                }
            }
        }
    }
}

@Composable
fun KissatenGroupCard(
    data: GroupCardData,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMembersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val netBalance = data.userNetBalancePaise
    val statusText = when {
        netBalance > 0 -> "You are owed"
        netBalance < 0 -> "You owe"
        else -> "Settled up"
    }

    val memberNames = data.members.map { it.name }

    KissatenReceiptCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAvatar(name = data.group.name, size = 44)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = data.group.name,
                            style = SplitzTypography.TitleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onMembersClick() }
                        ) {
                            Text(
                                text = "${data.memberCount} members",
                                style = SplitzTypography.BodySmall,
                                color = SplitzColors.textSecondary(),
                                fontWeight = FontWeight.SemiBold
                            )
                            if (memberNames.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
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
                            contentDescription = "Delete ${data.group.name}",
                            tint = SplitzColors.textTertiary().copy(alpha = 0.5f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = SplitzColors.surface(),
                        border = BorderStroke(1.dp, SplitzColors.border())
                    ) {
                        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Open ${data.group.name}",
                                tint = SplitzColors.textPrimary(),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            KissatenPerforationDivider()
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Spent",
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textTertiary()
                    )
                    MoneyText(
                        amountPaise = data.totalSpendingPaise,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        colorMode = MoneyColorMode.NEUTRAL
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = statusText,
                        style = SplitzTypography.LabelSmall,
                        color = SplitzColors.textTertiary()
                    )
                    MoneyText(
                        amountPaise = data.userNetBalancePaise,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        showSign = true
                    )
                }
            }
        }
    }
}

// Backward compatible aliases
@Composable
fun ModernGroupCard(
    data: GroupCardData,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMembersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KissatenGroupCard(
        data = data,
        onClick = onClick,
        onDeleteClick = onDeleteClick,
        onMembersClick = onMembersClick,
        modifier = modifier
    )
}

@Composable
fun GroupCard(
    data: GroupCardData,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMembersClick: () -> Unit
) {
    KissatenGroupCard(
        data = data,
        onClick = onClick,
        onDeleteClick = onDeleteClick,
        onMembersClick = onMembersClick
    )
}
