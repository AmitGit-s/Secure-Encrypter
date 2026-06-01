package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecryptSection(viewModel: ConversationViewModel) {
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Frosted Glass Header Card for Decrypting
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x0CFFFFFF)
            ),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color(0x1BFFFFFF))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Teal Glow container for LockOpen
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFF2DD4BF), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = "Message Decryption",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF1F5F9)
                    )
                    Text(
                        text = "Instantly restore original private messages offline by verifying security keys.",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Inner Main Panel: Frosted Glass Panel
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x11FFFFFF)),
            border = BorderStroke(1.dp, Color(0x1EFFFFFF))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cipher text field
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ENCRYPTED CIPHER PAYLOAD",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        // Glass Auto-Paste trigger
                        Button(
                            onClick = {
                                val clipText = clipboardManager.getText()?.text
                                if (!clipText.isNullOrBlank()) {
                                    viewModel.decryptPayload = clipText
                                    viewModel.decryptStatusMsg = "Cipher text pasted from secure clipboard!"
                                    viewModel.decryptIsError = false
                                } else {
                                    viewModel.decryptStatusMsg = "Clipboard buffer is currently empty!"
                                    viewModel.decryptIsError = true
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x14FFFFFF)),
                            border = BorderStroke(1.dp, Color(0x18FFFFFF)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF2DD4BF))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Paste", fontSize = 11.sp, color = Color(0xFF2DD4BF), fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedTextField(
                        value = viewModel.decryptPayload,
                        onValueChange = { viewModel.decryptPayload = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp)
                            .testTag("decrypt_msg_input"),
                        placeholder = { Text("Paste Base64 ciphertext block here...", color = Color(0xFF475569)) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2DD4BF),
                            unfocusedBorderColor = Color(0x1EFFFFFF),
                            focusedContainerColor = Color(0x0CFFFFFF),
                            unfocusedContainerColor = Color(0x08FFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                        maxLines = 8
                    )
                }

                // Decryption Password Input Section
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "DECRYPTION PASSWORD",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.decryptPassword,
                        onValueChange = { viewModel.decryptPassword = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("decrypt_password_input"),
                        placeholder = { Text("••••••••", color = Color(0xFF475569)) },
                        singleLine = true,
                        visualTransformation = if (viewModel.decryptPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.decryptPasswordVisible = !viewModel.decryptPasswordVisible }) {
                                Icon(
                                    imageVector = if (viewModel.decryptPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2DD4BF),
                            unfocusedBorderColor = Color(0x1EFFFFFF),
                            focusedContainerColor = Color(0x0CFFFFFF),
                            unfocusedContainerColor = Color(0x08FFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                // Unlock Button
                Button(
                    onClick = { viewModel.runDecryption() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("decrypt_submit_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2DD4BF) // Teal 400
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Unlock Message", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                }
            }
        }

        // Status Warnings and success displays
        if (viewModel.decryptStatusMsg.isNotEmpty()) {
            val statusBg = if (viewModel.decryptIsError) Color(0x22F87171) else Color(0x1A2DD4BF)
            val statusBorder = if (viewModel.decryptIsError) Color(0x55F87171) else Color(0x442DD4BF)
            val statusColor = if (viewModel.decryptIsError) Color(0xFFF87171) else Color(0xFF2DD4BF)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = statusBg),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, statusBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = if (viewModel.decryptIsError) Icons.Default.Error else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = viewModel.decryptStatusMsg,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = statusColor
                    )
                }
            }
        }

        // Decrypted Result Box View
        AnimatedVisibility(
            visible = viewModel.decryptedResult.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x192DD4BF)), // Translucent Teal
                border = BorderStroke(1.dp, Color(0x3B2DD4BF))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DECRYPTED ORIGINAL CONTENT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2DD4BF),
                            letterSpacing = 0.5.sp
                        )
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Decrypted Integrity Verified",
                            tint = Color(0xFF2DD4BF),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Decrypted Text Box Output
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xE6050B14), shape = RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0x1Affffff), shape = RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = viewModel.decryptedResult,
                            fontSize = 14.sp,
                            color = Color.White,
                            lineHeight = 20.sp,
                            modifier = Modifier.testTag("decrypt_result_text")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Clipboard copy trigger
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(viewModel.decryptedResult))
                            viewModel.decryptStatusMsg = "Decrypted plain message copied successfully!"
                            viewModel.decryptIsError = false
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2DD4BF)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Clear Text", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
