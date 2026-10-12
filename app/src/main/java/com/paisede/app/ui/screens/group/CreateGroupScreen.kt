package com.paisede.app.ui.screens.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisede.app.domain.repository.GroupRepository
import com.paisede.app.ui.components.KissatenPrimaryPillButton
import com.paisede.app.ui.components.KissatenReceiptCard
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.KissatenTokens
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzSpacing
import com.paisede.app.ui.theme.SplitzTypography
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen(
    groupRepository: GroupRepository,
    userPreferences: com.paisede.app.data.preferences.UserPreferences? = null,
    onNavigateBack: () -> Unit,
    onGroupCreated: (String) -> Unit
) {
    val userNameState = userPreferences?.userName?.collectAsState()?.value
    val currentUserName = if (!userNameState.isNullOrBlank()) userNameState else "You"
    val defaultUserMemberName = "Me ($currentUserName)"

    var groupName by remember { mutableStateOf("") }
    var currentMemberInput by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf(defaultUserMemberName, "Maya") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(SplitzShapes.Pill),
                    color = SplitzColors.surfaceElevated().copy(alpha = 0.9f),
                    border = BorderStroke(1.dp, SplitzColors.borderSubtle()),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { onNavigateBack() }
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

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create Group",
                        style = SplitzTypography.TitleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Text(
                        text = "OPEN NEW LEDGER",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SplitzColors.brandAccent(),
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 12.dp)
                ) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "GROUP NAME",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary(),
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Trip, flatmates, dinner, or shared project",
                                style = SplitzTypography.BodySmall,
                                color = SplitzColors.textMuted()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = groupName,
                                onValueChange = {
                                    groupName = it
                                    errorMessage = null
                                },
                                placeholder = {
                                    Text(
                                        "e.g. Goa Trip, Flatmates, Coffee Club",
                                        color = SplitzColors.textMuted().copy(alpha = 0.6f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.LocalCafe,
                                        contentDescription = null,
                                        tint = SplitzColors.brandAccent()
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SplitzColors.borderFocus(),
                                    unfocusedBorderColor = SplitzColors.borderSubtle(),
                                    focusedContainerColor = SplitzColors.surfaceElevated(),
                                    unfocusedContainerColor = SplitzColors.surfaceElevated()
                                )
                            )
                        }
                    }
                }

                item {
                    KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "ADD MEMBERS",
                                style = SplitzTypography.LabelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SplitzColors.textPrimary(),
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = currentMemberInput,
                                    onValueChange = { currentMemberInput = it },
                                    placeholder = {
                                        Text("Enter member name", color = SplitzColors.textMuted().copy(alpha = 0.6f))
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = SplitzColors.brandAccent()
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Words,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            val trimmed = currentMemberInput.trim()
                                            if (trimmed.isNotEmpty() && !members.contains(trimmed)) {
                                                members.add(trimmed)
                                                currentMemberInput = ""
                                                errorMessage = null
                                            }
                                        }
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SplitzColors.borderFocus(),
                                        unfocusedBorderColor = SplitzColors.borderSubtle(),
                                        focusedContainerColor = SplitzColors.surfaceElevated(),
                                        unfocusedContainerColor = SplitzColors.surfaceElevated()
                                    )
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SplitzColors.brandPrimary(),
                                    modifier = Modifier
                                        .height(56.dp)
                                        .width(56.dp)
                                        .clickable {
                                            val trimmed = currentMemberInput.trim()
                                            if (trimmed.isNotEmpty() && !members.contains(trimmed)) {
                                                members.add(trimmed)
                                                currentMemberInput = ""
                                                errorMessage = null
                                            }
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add",
                                            tint = SplitzColors.onPrimary()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Members (${members.size})",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary(),
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                itemsIndexed(members) { index, name ->
                    KissatenReceiptCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                UserAvatar(name = name, size = 32)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = name,
                                    style = SplitzTypography.BodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SplitzColors.textPrimary(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (members.size > 2) {
                                IconButton(
                                    onClick = { members.removeAt(index) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = SplitzColors.negative(),
                                        modifier = Modifier.size(18.dp)
                                    )
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
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 12.dp)
            ) {
                KissatenPrimaryPillButton(
                    text = if (isSaving) "Creating..." else "Create Group →",
                    onClick = {
                        val name = groupName.trim()
                        if (name.isEmpty()) {
                            errorMessage = "Please enter a group name"
                            return@KissatenPrimaryPillButton
                        }
                        val finalMembers = members.toMutableList()
                        if (!finalMembers.any { it.startsWith("Me (") || it == "Me ($currentUserName)" }) {
                            finalMembers.add(0, defaultUserMemberName)
                        }
                        isSaving = true
                        coroutineScope.launch {
                            try {
                                val group = groupRepository.createGroup(name, finalMembers.toList())
                                isSaving = false
                                onGroupCreated(group.id)
                            } catch (e: Exception) {
                                isSaving = false
                                errorMessage = e.message ?: "Failed to create group"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSaving
                )
            }
        }
    }
}
}
