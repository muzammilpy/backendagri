package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.computeInitials
import com.example.ui.theme.*
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

@Composable
fun EditProfileDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var nameInput by remember(currentName) { mutableStateOf(currentName) }
    val dynamicInitials = remember(nameInput) { computeInitials(nameInput) }
    val isValid = nameInput.trim().isNotBlank()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MinimalSurface,
            border = BorderStroke(1.dp, MinimalBorder),
            tonalElevation = 6.dp,
            modifier = modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp)
                .testTag("dialog_profile_edit")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Close Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.farmerProfile(lang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinimalTextPrimary
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_close_profile_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppStrings.cancel(lang),
                            tint = MinimalTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Interactive Dynamic Avatar Preview
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(CleanGreenLime),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dynamicInitials,
                        color = CleanGreenDark,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isValid) nameInput.trim() else AppStrings.defaultFarmerName(lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isValid) MinimalTextPrimary else MinimalTextMuted,
                    textAlign = TextAlign.Center
                )

                Surface(
                    color = SoftGreenCard,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, SoftGreenBorder),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "Active Agronomist Profile",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftGreenText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Name Input Field
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text(AppStrings.fullNameFarmId(lang)) },
                    placeholder = { Text(AppStrings.defaultFarmerName(lang)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = CleanGreenPrimary
                        )
                    },
                    trailingIcon = {
                        if (nameInput.isNotEmpty()) {
                            IconButton(onClick = { nameInput = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear name input",
                                    tint = MinimalTextMuted
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isValid) {
                                onSave(nameInput.trim())
                            }
                        }
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CleanGreenPrimary,
                        unfocusedBorderColor = MinimalBorder,
                        focusedLabelColor = CleanGreenPrimary,
                        cursorColor = CleanGreenPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_name")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "This name appears on diagnostic reports, disease alerts, and voice advisories.",
                    fontSize = 11.sp,
                    color = MinimalTextMuted,
                    lineHeight = 15.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MinimalBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MinimalTextSecondary
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_cancel_profile_edit")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = AppStrings.cancel(lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                softWrap = false,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isValid) {
                                onSave(nameInput.trim())
                            }
                        },
                        enabled = isValid,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CleanGreenPrimary,
                            contentColor = Color.White,
                            disabledContainerColor = MinimalBorder,
                            disabledContentColor = MinimalTextMuted
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_save_profile_name")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = AppStrings.save(lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
