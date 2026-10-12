package com.paisede.app.ui.screens.balance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.ui.components.EmptyStateView
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.MoneyColorMode
import com.paisede.app.ui.components.MoneyText
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.KissatenTokens
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import com.paisede.app.ui.viewmodel.BalanceViewModel
import com.paisede.app.ui.viewmodel.MemberBalanceItem

@Composable
fun BalancesScreen(
    viewModel: BalanceViewModel,
    userPreferences: com.paisede.app.data.preferences.UserPreferences? = null,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val currentUserName by (userPreferences?.userName ?: remember { kotlinx.coroutines.flow.MutableStateFlow("") }).collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(SplitzShapes.Pill),
                    color = SplitzColors.surfaceElevated().copy(alpha = 0.9f),
                    border = BorderStroke(1.dp, SplitzColors.borderSubtle()),
                    modifier = Modifier.clickable { onNavigateBack() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = SplitzColors.textPrimary(),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Back",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Member Balances",
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Text(
                        text = "GROUP BALANCES",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SplitzColors.brandAccent(),
                        letterSpacing = 1.sp
                    )
                }

                Box(modifier = Modifier.size(40.dp))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Hero Balance Overview (Kissaten Washi Receipt)
            KissatenReceiptCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TOTAL SPENT",
                            style = SplitzTypography.LabelSmall,
                            color = SplitzColors.textMuted(),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        MoneyText(
                            amountPaise = state.totalSpendingPaise,
                            style = SplitzTypography.TitleLarge,
                            fontWeight = FontWeight.Black,
                            colorMode = MoneyColorMode.NEUTRAL,
                            animate = true
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(SplitzColors.borderSubtle())
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "NET PENDING",
                            style = SplitzTypography.LabelSmall,
                            color = SplitzColors.textMuted(),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        MoneyText(
                            amountPaise = state.totalOutstandingDebtPaise,
                            style = SplitzTypography.TitleLarge,
                            fontWeight = FontWeight.Black,
                            colorMode = MoneyColorMode.DEBT,
                            animate = true
                        )
                    }
                }
            }

            if (state.items.isEmpty()) {
                KissatenReceiptCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = null,
                            tint = SplitzColors.brandAccent(),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No Balance Data",
                            style = SplitzTypography.TitleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "When you record expenses in the group, member balances will appear here.",
                            style = SplitzTypography.BodySmall,
                            color = SplitzColors.textMuted()
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(state.items, key = { _, it -> it.member.id }) { index, item ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(item.member.id) {
                            delay(index * 50L)
                            visible = true
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { it / 4 }
                        ) {
                            KissatenBalanceCard(item = item, currentUserName = currentUserName)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KissatenBalanceCard(item: MemberBalanceItem, currentUserName: String = "") {
    val bal = item.balancePaise
    val displayName = when {
        item.member.name.startsWith("Me (") -> item.member.name
        item.member.name.equals(currentUserName, ignoreCase = true) && currentUserName.isNotBlank() -> "Me ($currentUserName)"
        else -> item.member.name
    }
    val (statusText, statusBg, statusColor) = when {
        bal > 0 -> Triple("Gets back", SplitzColors.positiveContainer(), SplitzColors.positive())
        bal < 0 -> Triple("Owes", SplitzColors.negativeContainer(), SplitzColors.negative())
        else -> Triple("Settled", SplitzColors.surfaceElevated(), SplitzColors.textMuted())
    }

    KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(
                    name = displayName,
                    size = 42,
                    backgroundColor = if (bal > 0) SplitzColors.positiveContainer() else if (bal < 0) SplitzColors.negativeContainer() else null,
                    textColor = if (bal > 0) SplitzColors.positive() else if (bal < 0) SplitzColors.negative() else null
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = displayName,
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(SplitzShapes.Pill),
                        color = statusBg,
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = statusText,
                            style = SplitzTypography.LabelSmall,
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            MoneyText(
                amountPaise = bal,
                style = SplitzTypography.TitleLarge,
                fontWeight = FontWeight.Bold,
                showSign = true,
                animate = true
            )
        }
    }
}

// Backward compatible alias
@Composable
fun ModernBalanceCard(item: MemberBalanceItem, currentUserName: String = "") {
    KissatenBalanceCard(item, currentUserName)
}
