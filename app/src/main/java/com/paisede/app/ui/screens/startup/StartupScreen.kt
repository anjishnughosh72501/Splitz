package com.paisede.app.ui.screens.startup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.data.preferences.AppCurrency
import com.paisede.app.data.preferences.UserPreferences
import com.paisede.app.ui.components.SplitzGlassCard
import com.paisede.app.ui.components.SplitzPrimaryButton
import com.paisede.app.ui.components.SplitzSurface
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.CreditGreen
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.IndigoPurple
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzGradients
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography

@Composable
fun StartupScreen(
    userPreferences: UserPreferences,
    onComplete: () -> Unit
) {
    var userNameInput by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf(AppCurrency.DEFAULT) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SplitzColors.background())
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = SplitzSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(SplitzSpacing.md),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 24.dp)
            ) {
                // Hero Banner
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(SplitzShapes.cardHero))
                            .background(SplitzGradients.Hero)
                            .padding(24.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Welcome to Splitz",
                                        style = SplitzTypography.HeadlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Set up your profile to get started",
                                        style = SplitzTypography.BodyMedium,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 1: Username
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. CHOOSE YOUR NAME",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textMuted()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "This is how you'll appear to everyone in your groups as 'Me (your name)'.",
                        style = SplitzTypography.BodyMedium,
                        color = SplitzColors.textSecondary()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = userNameInput,
                        onValueChange = {
                            userNameInput = it
                            errorMessage = null
                        },
                        placeholder = { Text("e.g. Anjishnu, Alex, Sam", color = SplitzColors.textMuted()) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = SplitzColors.primary()
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(SplitzShapes.MD),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SplitzColors.primary(),
                            unfocusedBorderColor = SplitzColors.borderSubtle(),
                            focusedContainerColor = SplitzColors.surface(),
                            unfocusedContainerColor = SplitzColors.surface()
                        )
                    )

                    // Live preview of profile badge
                    val trimmedPreview = userNameInput.trim()
                    if (trimmedPreview.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        SplitzSurface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(SplitzShapes.SM),
                            backgroundColor = SplitzColors.surfaceElevated(),
                            borderColor = SplitzColors.primary().copy(alpha = 0.3f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                UserAvatar(name = "Me ($trimmedPreview)", size = 36)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "In your groups you'll be shown as:",
                                        style = SplitzTypography.LabelSmall,
                                        color = SplitzColors.textSecondary()
                                    )
                                    Text(
                                        text = "Me ($trimmedPreview)",
                                        style = SplitzTypography.TitleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SplitzColors.primary()
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 2: Currency
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "2. CHOOSE YOUR CURRENCY",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textMuted()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Select your preferred currency. You can also change this anytime in Settings.",
                        style = SplitzTypography.BodyMedium,
                        color = SplitzColors.textSecondary()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(AppCurrency.entries.size) { index ->
                    val curr = AppCurrency.entries[index]
                    val isSelected = selectedCurrency == curr

                    SplitzSurface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(SplitzShapes.SM),
                        backgroundColor = if (isSelected) SplitzColors.surfaceElevated() else SplitzColors.surface(),
                        borderColor = if (isSelected) SplitzColors.primary() else SplitzColors.borderSubtle(),
                        onClick = { selectedCurrency = curr }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) SplitzColors.primary().copy(alpha = 0.15f) else SplitzColors.borderSubtle().copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = curr.symbol,
                                        style = SplitzTypography.TitleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) SplitzColors.primary() else SplitzColors.textSecondary()
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = curr.displayName,
                                        style = SplitzTypography.BodyLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = SplitzColors.textPrimary()
                                    )
                                    Text(
                                        text = "Subunit: ${curr.subunitName} (100 ${curr.subunitName} = 1 ${curr.code})",
                                        style = SplitzTypography.BodySmall,
                                        color = SplitzColors.textMuted()
                                    )
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(SplitzColors.primary()),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (errorMessage != null) {
                    item {
                        Text(
                            text = errorMessage!!,
                            style = SplitzTypography.BodyMedium,
                            color = DebtRed,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                // Submit CTA
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SplitzPrimaryButton(
                        text = "Get Started →",
                        onClick = {
                            val trimmed = userNameInput.trim()
                            if (trimmed.isEmpty()) {
                                errorMessage = "Please enter your name to continue"
                                return@SplitzPrimaryButton
                            }
                            userPreferences.completeStartup(trimmed, selectedCurrency)
                            onComplete()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
