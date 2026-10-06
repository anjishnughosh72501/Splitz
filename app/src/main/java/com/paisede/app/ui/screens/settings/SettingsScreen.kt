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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.theme.AccentAmber
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.IndigoPurple
import com.paisede.app.ui.theme.Slate100
import com.paisede.app.ui.theme.Slate200
import com.paisede.app.ui.theme.Slate600
import com.paisede.app.ui.theme.Slate800
import com.paisede.app.ui.theme.Slate900

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import com.paisede.app.data.preferences.AppThemeMode
import com.paisede.app.data.preferences.ThemePreferences

@Composable
fun SettingsScreen(
    groupId: String? = null,
    themePreferences: ThemePreferences? = null,
    onNavigateBack: () -> Unit,
    onInspectDataStructures: ((String) -> Unit)? = null
) {
    val currentThemeMode by (themePreferences?.themeMode ?: remember { kotlinx.coroutines.flow.MutableStateFlow(AppThemeMode.SYSTEM) }).collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    text = "Choose Appearance",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    AppThemeMode.entries.forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    themePreferences?.setThemeMode(mode)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 10.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (currentThemeMode == mode),
                                onClick = {
                                    themePreferences?.setThemeMode(mode)
                                    showThemeDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = mode.displayName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Close", color = BrandTeal)
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
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Branding Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else Slate900
                    ),
                    border = if (isSystemInDarkTheme()) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(BrandTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "S",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Splitz",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Version 1.0.0 • Clean & Offline",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }

            // General Preferences
            item {
                Text(
                    text = "Preferences",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SettingRow(
                            icon = Icons.Default.CurrencyRupee,
                            iconColor = BrandTeal,
                            title = "Currency",
                            value = "Indian Rupee (₹ INR)"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        SettingRow(
                            icon = Icons.Default.Shield,
                            iconColor = CreditGreen,
                            title = "Math Engine",
                            value = "Integer Paise (0 Rounding Error)"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        SettingRow(
                            icon = Icons.Default.Palette,
                            iconColor = IndigoPurple,
                            title = "Appearance",
                            value = currentThemeMode.displayName,
                            onClick = { showThemeDialog = true }
                        )
                    }
                }
            }

            // Optional Data Structure Inspector Link (if opened from a group)
            if (groupId != null && onInspectDataStructures != null) {
                item {
                    Text(
                        text = "Advanced Tools",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onInspectDataStructures(groupId) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
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
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Inspect live heaps, HashMaps, and stacks",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                        tint = BrandTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "How Splitz Works",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Help Item 1: Debt Simplification
            item {
                HelpAccordionCard(
                    icon = Icons.Default.AutoAwesome,
                    iconTint = BrandTeal,
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
                    iconTint = BrandTeal,
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
            .clip(RoundedCornerShape(8.dp))
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
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (onClick != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
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
            .clickable { expanded = !expanded },
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
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = details,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.25f
                    )
                }
            }
        }
    }
}
