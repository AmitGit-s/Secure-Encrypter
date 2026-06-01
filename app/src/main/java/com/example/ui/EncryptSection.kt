package com.example.ui

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
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
fun EncryptSection(viewModel: ConversationViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Frosted Glass Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x0CFFFFFF) // Translucent white
            ),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color(0x1BFFFFFF)) // Thin glass border
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Indigo Glow container
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFF6366F1), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = "Message Encryption",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF1F5F9)
                    )
                    Text(
                        text = "Derive military-grade AES-256 keys to lock sensitive text securely offline.",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Inner Main Panel: Glass Card container matching the spec
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x11FFFFFF)), // Translucent frosted surface
            border = BorderStroke(1.dp, Color(0x1EFFFFFF))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Secret Message Textfield section
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "SECRET MESSAGE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.encryptMessage,
                        onValueChange = { viewModel.encryptMessage = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp)
                            .testTag("encrypt_msg_input"),
                        placeholder = { Text("Type your secret message here...", color = Color(0xFF475569)) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = Color(0x1EFFFFFF),
                            focusedContainerColor = Color(0x0CFFFFFF),
                            unfocusedContainerColor = Color(0x08FFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        maxLines = 8
                    )
                }

                // Password Input Section
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
                        value = viewModel.encryptPassword,
                        onValueChange = { viewModel.encryptPassword = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("encrypt_password_input"),
                        placeholder = { Text("••••••••", color = Color(0xFF475569)) },
                        singleLine = true,
                        visualTransformation = if (viewModel.encryptPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                            IconButton(onClick = { viewModel.encryptPasswordVisible = !viewModel.encryptPasswordVisible }) {
                                Icon(
                                    imageVector = if (viewModel.encryptPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = Color(0x1EFFFFFF),
                            focusedContainerColor = Color(0x0CFFFFFF),
                            unfocusedContainerColor = Color(0x08FFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                // Seal Button
                Button(
                    onClick = { viewModel.runEncryption() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("encrypt_submit_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6366F1) // Indigo 500
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.EnhancedEncryption, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Seal Message", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                }
            }
        }

        // Status Messages View
        if (viewModel.encryptStatusMsg.isNotEmpty()) {
            val statusBg = if (viewModel.encryptIsError) Color(0x22F87171) else Color(0x1A2DD4BF)
            val statusBorder = if (viewModel.encryptIsError) Color(0x55F87171) else Color(0x442DD4BF)
            val statusColor = if (viewModel.encryptIsError) Color(0xFFF87171) else Color(0xFF2DD4BF)

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
                        imageVector = if (viewModel.encryptIsError) Icons.Default.Error else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = viewModel.encryptStatusMsg,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = statusColor
                    )
                }
            }
        }

        // Results displays (cipher output blocks)
        AnimatedVisibility(
            visible = viewModel.encryptedResult.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cipher Result Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x11FFFFFF)),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0x15FFFFFF))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ENCRYPTED OUTPUT (BASE64)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2DD4BF),
                                letterSpacing = 0.5.sp
                            )
                            Badge(containerColor = Color(0x242DD4BF)) {
                                Text("AES-256 GCM", color = Color(0xFF2DD4BF), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Output Block
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xE6050B14), shape = RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0x12FFFFFF), shape = RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = viewModel.encryptedResult,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color(0xFF2DD4BF),
                                modifier = Modifier.testTag("encrypt_result_text")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Copy & Share utilities
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(viewModel.encryptedResult))
                                    viewModel.encryptStatusMsg = "Copied encrypted payload to clipboard."
                                    viewModel.encryptIsError = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x19FFFFFF)),
                                border = BorderStroke(1.dp, Color(0x22FFFFFF))
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy Cipher", fontSize = 12.sp, color = Color.White)
                            }

                            Button(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, viewModel.encryptedResult)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Share Encrypted Cipher")
                                    context.startActivity(shareIntent)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x19FFFFFF)),
                                border = BorderStroke(1.dp, Color(0x22FFFFFF))
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Share", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }

                // Database Local Persist Form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x0CFFFFFF)),
                    border = BorderStroke(1.dp, Color(0x18FFFFFF))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Save Locally to Secure Storage",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Save this encrypted note to the sandboxed SQLite database. The note will be private and stored only on this device.",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )

                        // Title Field
                        OutlinedTextField(
                            value = viewModel.encryptTitle,
                            onValueChange = { viewModel.encryptTitle = it },
                            placeholder = { Text("e.g. Secret Protocol Alpha", color = Color(0xFF475569)) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6366F1),
                                unfocusedBorderColor = Color(0x14FFFFFF),
                                focusedContainerColor = Color(0x0AFFFFFF),
                                unfocusedContainerColor = Color(0x05FFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        // Password Hint Field
                        OutlinedTextField(
                            value = viewModel.encryptHint,
                            onValueChange = { viewModel.encryptHint = it },
                            placeholder = { Text("Password hint (optional)", color = Color(0xFF475569)) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6366F1),
                                unfocusedBorderColor = Color(0x14FFFFFF),
                                focusedContainerColor = Color(0x0AFFFFFF),
                                unfocusedContainerColor = Color(0x05FFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        // Save Trigger
                        Button(
                            onClick = { viewModel.saveToLocalStore() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2DD4BF)), // Teal-400
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .align(Alignment.End)
                                .testTag("save_local_button")
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Securing Note", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
