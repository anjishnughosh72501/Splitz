package com.paisede.app.ui.screens.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.paisede.app.domain.repository.GroupRepository
import com.paisede.app.ui.components.SplitzTopAppBar
import com.paisede.app.ui.components.UserAvatar
import com.paisede.app.ui.theme.DebtRed
import com.paisede.app.ui.theme.SplitzColors
import com.paisede.app.ui.theme.SplitzShapes
import com.paisede.app.ui.theme.SplitzTypography
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen(
    groupRepository: GroupRepository,
    onNavigateBack: () -> Unit,
    onGroupCreated: (String) -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    var currentMemberInput by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf("Ali", "Maya") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SplitzTopAppBar(
                title = "Create New Group",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SplitzColors.background())
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Group Details",
                        style = SplitzTypography.LabelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = groupName,
                        onValueChange = {
                            groupName = it
                            errorMessage = null
                        },
                        placeholder = { Text("e.g. Goa Trip, Flatmates, Weekend Trek", color = SplitzColors.textMuted()) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Group, contentDescription = null, tint = SplitzColors.primary())
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(SplitzShapes.SM),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SplitzColors.primary(),
                            unfocusedBorderColor = SplitzColors.borderSubtle(),
                            focusedContainerColor = SplitzColors.surface(),
                            unfocusedContainerColor = SplitzColors.surface()
                        ),
                        singleLine = true
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add Members",
                        style = SplitzTypography.LabelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textPrimary()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = currentMemberInput,
                            onValueChange = { currentMemberInput = it },
                            placeholder = { Text("Member Name", color = SplitzColors.textMuted()) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(SplitzShapes.SM),
                            singleLine = true,
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = SplitzColors.textMuted())
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SplitzColors.primary(),
                                unfocusedBorderColor = SplitzColors.borderSubtle(),
                                focusedContainerColor = SplitzColors.surface(),
                                unfocusedContainerColor = SplitzColors.surface()
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val trimmed = currentMemberInput.trim()
                                if (trimmed.isNotEmpty() && !members.contains(trimmed)) {
                                    members.add(trimmed)
                                    currentMemberInput = ""
                                    errorMessage = null
                                }
                            },
                            shape = RoundedCornerShape(SplitzShapes.SM),
                            colors = ButtonDefaults.buttonColors(containerColor = SplitzColors.primary())
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                        }
                    }
                }

                item {
                    Text(
                        text = "MEMBERS (${members.size})",
                        style = SplitzTypography.LabelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SplitzColors.textMuted(),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                itemsIndexed(members) { index, name ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(SplitzShapes.SM))
                            .background(SplitzColors.surface())
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            UserAvatar(name = name, size = 36)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = name,
                                style = SplitzTypography.BodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = SplitzColors.textPrimary()
                            )
                        }

                        if (members.size > 2) {
                            IconButton(onClick = { members.removeAt(index) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = DebtRed,
                                    modifier = Modifier.size(18.dp)
                                )
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
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val name = groupName.trim()
                    if (name.isEmpty()) {
                        errorMessage = "Please enter a group name"
                        return@Button
                    }
                    if (members.size < 2) {
                        errorMessage = "At least 2 members are required"
                        return@Button
                    }
                    isSaving = true
                    coroutineScope.launch {
                        val group = groupRepository.createGroup(name, members.toList())
                        isSaving = false
                        onGroupCreated(group.id)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(SplitzShapes.button),
                colors = ButtonDefaults.buttonColors(containerColor = SplitzColors.primary()),
                enabled = !isSaving
            ) {
                Text(
                    text = if (isSaving) "Creating..." else "Create Group",
                    style = SplitzTypography.TitleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
