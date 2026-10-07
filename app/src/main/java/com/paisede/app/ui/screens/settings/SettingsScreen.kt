package com.paisede.app.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.FormatSize
import com.paisede.app.data.preferences.AppFontSize
import com.paisede.app.data.preferences.AppThemeMode
import com.paisede.app.data.preferences.ThemePreferences
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.theme.AccentAmber
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.IndigoPurple
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzTypography

@Composable
fun SettingsScreen(
    groupId: String? = null,
    themePreferences: ThemePreferences? = null,
    onNavigateBack: () -> Unit,
    onInspectDataStructures: ((String) -> Unit)? = null
) {
    val currentThemeMode by (themePreferences?.themeMode ?: remember { kotlinx.coroutines.flow.MutableStateFlow(AppThemeMode.SYSTEM) }).collectAsState()
    val currentFontSize by (themePreferences?.fontSize ?: remember { kotlinx.coroutines.flow.MutableStateFlow(AppFontSize.MEDIUM) }).collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showFontSizeDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            shape = RoundedCornerShape(SplitzShapes.sheet),
            containerColor = SplitzColors.surface(),
            title = {
                Text(
                    text = "Choose Appearance",
                    style = SplitzTypography.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.textPrimary()
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    AppThemeMode.entries.forEach { mode ->
                        val isSelected = currentThemeMode == mode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(SplitzShapes.SM))
                                .clickable {
                                    themePreferences?.setThemeMode(mode)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    themePreferences?.setThemeMode(mode)
                                    showThemeDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = SplitzColors.primary())
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = mode.displayName,
                                style = SplitzTypography.BodyLarge,
                                color = SplitzColors.textPrimary(),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Close", color = SplitzColors.primary(), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (showFontSizeDialog) {
        AlertDialog(
            onDismissRequest = { showFontSizeDialog = false },
            shape = RoundedCornerShape(SplitzShapes.sheet),
            containerColor = SplitzColors.surface(),
            title = {
                Text(
                    text = "Choose Font Size",
                    style = SplitzTypography.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.textPrimary()
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    AppFontSize.entries.forEach { size ->
                        val isSelected = currentFontSize == size
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(SplitzShapes.SM))
                                .clickable {
                                    themePreferences?.setFontSize(size)
                                    showFontSizeDialog = false
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    themePreferences?.setFontSize(size)
                                    showFontSizeDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = SplitzColors.primary())
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = size.displayName,
                                style = SplitzTypography.BodyLarge,
                                color = SplitzColors.textPrimary(),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontSizeDialog = false }) {
                    Text("Close", color = SplitzColors.primary(), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            SplitzTopAppBar(
                title = "Settings & Help",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SplitzColors.background()),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Branding Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(SplitzShapes.cardHero),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle()),
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
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(SplitzColors.primary()),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "S",
                                    style = SplitzTypography.HeadlineLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Splitz",
                                    style = SplitzTypography.TitleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Version 2.0 • Modern Fintech & Offline First",
                                    style = SplitzTypography.BodySmall,
                                    color = Color.White.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }
                }
            }

            // General Preferences
            item {
                Text(
                    text = "PREFERENCES",
                    style = SplitzTypography.LabelSmall,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.textMuted(),
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(SplitzShapes.MD),
                    colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SettingRow(
                            icon = Icons.Default.CurrencyRupee,
                            iconColor = SplitzColors.primary(),
                            title = "Currency",
                            value = "Indian Rupee (₹ INR)"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SplitzColors.borderSubtle())
                        SettingRow(
                            icon = Icons.Default.Shield,
                            iconColor = CreditGreen,
                            title = "Math Engine",
                            value = "Integer Paise (0 Rounding Error)"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SplitzColors.borderSubtle())
                        SettingRow(
                            icon = Icons.Default.Palette,
                            iconColor = IndigoPurple,
                            title = "Appearance",
                            value = currentThemeMode.displayName,
                            onClick = { showThemeDialog = true }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SplitzColors.borderSubtle())
                        SettingRow(
                            icon = Icons.Default.FormatSize,
                            iconColor = SplitzColors.primary(),
                            title = "Font Size",
                            value = currentFontSize.displayName,
                            onClick = { showFontSizeDialog = true }
                        )
                    }
                }
            }

            // Optional Data Structure Inspector Link (if opened from a group)
            if (groupId != null && onInspectDataStructures != null) {
                item {
                    Text(
                        text = "ADVANCED TOOLS",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textMuted(),
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(SplitzShapes.MD))
                            .clickable { onInspectDataStructures(groupId) },
                        shape = RoundedCornerShape(SplitzShapes.MD),
                        colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(DebtRed.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BugReport,
                                        contentDescription = "Data Structures Inspector",
                                        tint = DebtRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "Data Structure Inspector",
                                        style = SplitzTypography.TitleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.textPrimary()
                                    )
                                    Text(
                                        text = "Inspect live heaps, HashMaps, and stacks",
                                        style = SplitzTypography.BodySmall,
                                        color = SplitzColors.textSecondary()
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = SplitzColors.textMuted()
                            )
                        }
                    }
                }
            }

            // Help Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Help Guide",
                        tint = SplitzColors.primary(),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "How Splitz Works",
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                }
            }

            // Help Item 1: Debt Simplification
            item {
                HelpAccordionCard(
                    icon = Icons.Default.AutoAwesome,
                    iconTint = SplitzColors.primary(),
                    title = "Smart Debt Simplification",
                    summary = "Minimizes payments so friends settle with the fewest transactions.",
                    details = "Without simplification, if person A owes B, B owes C, and C owes A, multiple back-and-forth transfers occur.\n\n" +
                            "Splitz calculates everyone's net balance, then pairs the largest debtor with the largest creditor using a 2-Heap Greedy algorithm (O((V+E) log V)).\n\n" +
                            "For smaller groups (≤8 members), Splitz additionally solves an Exact Subset-Sum Partition (O(3^N)) to guarantee the absolute mathematical minimum payments."
                )
            }

            // Help Item 2: Split Methods
            item {
                HelpAccordionCard(
                    icon = Icons.Default.CallSplit,
                    iconTint = IndigoPurple,
                    title = "Splitting Expenses",
                    summary = "Equal, Exact, Percentage, or Share-based allocations.",
                    details = "• Equal Split: Divides the total evenly. Any remainder paise are distributed +1 paise to initial members so the exact total is always conserved.\n\n" +
                            "• Exact Split: Enter specific amounts in rupees for each participant.\n\n" +
                            "• Percentage Split: Specify each person's percentage using basis points (100% = 10,000 bp).\n\n" +
                            "• Shares: Specify integer weight ratios (e.g. 2 shares vs 1 share)."
                )
            }

            // Help Item 3: Circular Debt Resolution
            item {
                HelpAccordionCard(
                    icon = Icons.Default.Hub,
                    iconTint = AccentAmber,
                    title = "Debt Network & Circular Debts",
                    summary = "Visualizes debt links and detects circular cycles.",
                    details = "The Debt Graph models members as nodes and debts as directed edges.\n\n" +
                            "Using Depth-First Search (DFS) with 3-state vertex coloring (Unvisited, Visiting, Visited), Splitz identifies directed loops (e.g. A → B → C → A) and allows resolving them by subtracting the minimum cyclic edge, reducing everyone's balance without changing net positions."
                )
            }

            // Help Item 4: Integer Precision
            item {
                HelpAccordionCard(
                    icon = Icons.Default.CurrencyRupee,
                    iconTint = CreditGreen,
                    title = "Strict Integer Precision",
                    summary = "Zero floating-point rounding errors.",
                    details = "Standard floating-point numbers (Float, Double) suffer from precision drift (e.g. 0.1 + 0.2 ≠ 0.3).\n\n" +
                            "Splitz stores every financial value in 64-bit integer paise (₹1 = 100 paise), guaranteeing exact zero-sum ledger balance at all times."
                )
            }

            // Help Item 5: Undo & Redo
            item {
                HelpAccordionCard(
                    icon = Icons.AutoMirrored.Filled.Undo,
                    iconTint = SplitzColors.primary(),
                    title = "Undo & Redo",
                    summary = "Reverse or replay any expense changes effortlessly.",
                    details = "All ledger mutations are tracked on a Last-In First-Out (LIFO) stack. Tapping Undo reverts the last operation, and Redo restores it, keeping your group state completely safe."
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    val rowModifier = if (onClick != null) {
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SplitzShapes.SM))
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    } else {
        Modifier.fillMaxWidth()
    }

    Row(
        modifier = rowModifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = SplitzTypography.BodyMedium,
                fontWeight = FontWeight.Medium,
                color = SplitzColors.textPrimary()
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = SplitzTypography.BodyMedium,
                color = SplitzColors.textSecondary()
            )
            if (onClick != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = SplitzColors.textMuted(),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun HelpAccordionCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    summary: String,
    details: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SplitzShapes.MD))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(SplitzShapes.MD),
        colors = CardDefaults.cardColors(containerColor = SplitzColors.surface()),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SplitzColors.borderSubtle())
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
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(iconTint.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = SplitzTypography.TitleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary()
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = summary,
                            style = SplitzTypography.BodySmall,
                            color = SplitzColors.textSecondary()
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = SplitzColors.textMuted()
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = SplitzColors.borderSubtle())
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = details,
                        style = SplitzTypography.BodyMedium,
                        color = SplitzColors.textSecondary(),
                        lineHeight = SplitzTypography.BodyMedium.lineHeight * 1.25f
                    )
                }
            }
        }
    }
}
