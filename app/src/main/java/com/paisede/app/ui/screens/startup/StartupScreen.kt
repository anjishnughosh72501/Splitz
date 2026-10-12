package com.paisede.app.ui.screens.startup

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.paisede.app.R
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
import com.paisede.app.ui.components.KissatenHankoStamp
import com.paisede.app.ui.components.KissatenPerforationDivider
import com.paisede.app.ui.components.KissatenPrimaryPillButton
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.KissatenTokens
import com.paisede.app.ui.theme.SplitzColors
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
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 560.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 10.dp, bottom = 24.dp)
            ) {
            // Hero Banner
            item {
                Spacer(modifier = Modifier.height(10.dp))
                KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFE3C69A), // Warm Kraft matching logo background
                            border = BorderStroke(1.5.dp, SplitzColors.borderSubtle()),
                            modifier = Modifier.size(92.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.splitz_logo),
                                contentDescription = "Splitz Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Splitz",
                                style = SplitzTypography.HeadlineMedium,
                                fontWeight = FontWeight.Black,
                                color = SplitzColors.textPrimary()
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            KissatenHankoStamp(text = "OFFLINE")
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Group expense splitting made simple",
                            style = SplitzTypography.TitleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = SplitzColors.brandAccent(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Track shared spending, calculate fair splits, and simplify debts with optimal settlement algorithms.",
                            style = SplitzTypography.BodyMedium,
                            color = SplitzColors.textSecondary(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }

            // Section 1: Username
            item {
                KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "1. ENTER YOUR NAME",
                            style = SplitzTypography.LabelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SplitzColors.textPrimary(),
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "This is how you will appear in groups: Me (Your Name)",
                            style = SplitzTypography.BodySmall,
                            color = SplitzColors.textMuted()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = userNameInput,
                            onValueChange = {
                                if (it.length <= 30) {
                                    userNameInput = it
                                    errorMessage = null
                                }
                            },
                            placeholder = {
                                Text(
                                    "e.g. Alex, Sam, Taylor",
                                    color = SplitzColors.textMuted().copy(alpha = 0.6f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = SplitzColors.brandAccent()
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SplitzColors.borderFocus(),
                                unfocusedBorderColor = SplitzColors.borderSubtle(),
                                focusedContainerColor = SplitzColors.surfaceElevated(),
                                unfocusedContainerColor = SplitzColors.surfaceElevated()
                            )
                        )

                        val trimmedPreview = userNameInput.trim()
                        if (trimmedPreview.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SplitzColors.backgroundElevated(),
                                border = BorderStroke(1.dp, SplitzColors.borderSubtle()),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    UserAvatar(name = "Me ($trimmedPreview)", size = 32)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Preview:",
                                            style = SplitzTypography.LabelSmall,
                                            color = SplitzColors.textMuted()
                                        )
                                        Text(
                                            text = "Me ($trimmedPreview)",
                                            style = SplitzTypography.BodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = SplitzColors.textPrimary()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Currency
            item {
                Text(
                    text = "2. SELECT CURRENCY",
                    style = SplitzTypography.LabelSmall,
                    fontWeight = FontWeight.Bold,
                    color = SplitzColors.textPrimary(),
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                )
            }

            items(AppCurrency.entries.size) { index ->
                val curr = AppCurrency.entries[index]
                val isSelected = selectedCurrency == curr

                KissatenReceiptCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCurrency = curr }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) SplitzColors.positiveContainer() else SplitzColors.backgroundElevated(),
                                border = BorderStroke(1.dp, if (isSelected) SplitzColors.positive() else SplitzColors.borderSubtle())
                            ) {
                                Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = curr.symbol,
                                        style = SplitzTypography.TitleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) SplitzColors.positive() else SplitzColors.textPrimary()
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = curr.displayName,
                                    style = SplitzTypography.BodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = SplitzColors.textPrimary(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Subunit: ${curr.subunitName} (100 ${curr.subunitName} = 1 ${curr.code})",
                                    style = SplitzTypography.BodySmall,
                                    color = SplitzColors.textMuted(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = CircleShape,
                                color = SplitzColors.brandPrimary()
                            ) {
                                Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = SplitzColors.onPrimary(),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (errorMessage != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SplitzColors.negativeContainer(),
                        border = BorderStroke(1.dp, SplitzColors.negative().copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage!!,
                            style = SplitzTypography.BodyMedium,
                            color = SplitzColors.negative(),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            // Submit CTA
            item {
                Spacer(modifier = Modifier.height(10.dp))
                KissatenPrimaryPillButton(
                    text = "Get Started →",
                    onClick = {
                        val trimmed = userNameInput.trim()
                        if (trimmed.isEmpty()) {
                            errorMessage = "Please enter your name to continue"
                            return@KissatenPrimaryPillButton
                        }
                        if (trimmed.length > 30) {
                            errorMessage = "Name must be 30 characters or fewer"
                            return@KissatenPrimaryPillButton
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
