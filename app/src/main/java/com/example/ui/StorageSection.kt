package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.crypto.CryptoHelper
import com.example.data.ConversationEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StorageSection(viewModel: ConversationViewModel) {
    val savedItems by viewModel.savedConversations.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current

    // Inline dialog quick decrypt states
    var showDecryptDialog by remember { mutableStateOf(false) }
    var selectedItemForDecrypt by remember { mutableStateOf<ConversationEntity?>(null) }
    var dialogPassword by remember { mutableStateOf("") }
    var dialogPasswordVisible by remember { mutableStateOf(false) }
    var dialogDecryptedResult by remember { mutableStateOf("") }
    var dialogErrorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Safe Header Banner (Frosted Glass)
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
                // Indigo/Teal Accent Glow Container
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFF6366F1), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Dns,
                        contentDescription = "Vault DB Icon",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = "Encrypted Vault Storage",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF1F5F9)
                    )
                    Text(
                        text = "All messages are preserved in a secure, sandboxed Android SQLite database. Your raw text is never stored in plaintext.",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Vault list header info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saved Encryption Blocks (${savedItems.size})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF1F5F9)
            )

            if (savedItems.isNotEmpty()) {
                Text(
                    text = "OFFLINE & PROTECTED",
                    fontSize = 10.sp,
                    color = Color(0xFF2DD4BF),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Empty state drawing
        if (savedItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "No data",
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Your Secure Vault is Empty",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "Go to the Encrypt tab, cipher a message, and save it to have offline conversations stored safely here.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("conversations_list"),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(savedItems, key = { it.id }) { item ->
                    var isExpanded by remember { mutableStateOf(false) }
                    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }
                    val dateFormatted = dateFormat.format(Date(item.timestamp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0x11FFFFFF) // Frosted list item card
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isExpanded) Color(0xFF6366F1).copy(alpha = 0.5f) else Color(0x18FFFFFF)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .clickable { isExpanded = !isExpanded }
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Circular Icon Indicator (Teal glow)
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .background(
                                                Color(0x242DD4BF),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ChatBubbleOutline,
                                            contentDescription = null,
                                            tint = Color(0xFF2DD4BF),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = dateFormatted,
                                            fontSize = 11.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }
                                }

                                IconButton(onClick = { isExpanded = !isExpanded }) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = "Expand Detail",
                                        tint = Color(0xFF94A3B8)
                                    )
                                }
                            }

                            // Password hint preview
                            if (item.passwordHint.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Hint: ${item.passwordHint}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF2DD4BF).copy(alpha = 0.8f),
                                    modifier = Modifier.padding(start = 50.dp)
                                )
                            }

                            // Expanded Menu
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    HorizontalDivider(color = Color(0x12FFFFFF))

                                    // Preview of ciphered Base64 payload
                                    Column {
                                        Text(
                                            text = "Encrypted Text Preview",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6366F1)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xE6050B14), shape = RoundedCornerShape(8.dp))
                                                .border(1.dp, Color(0x12FFFFFF), shape = RoundedCornerShape(8.dp))
                                                .padding(10.dp)
                                        ) {
                                            Text(
                                                text = item.encryptedText.take(120) + if (item.encryptedText.length > 120) "..." else "",
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 11.sp,
                                                color = Color(0xFF94A3B8)
                                            )
                                        }
                                    }

                                    // Action Operations layout
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // 1. Copy Ciphertext button
                                        IconButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(item.encryptedText))
                                            },
                                            modifier = Modifier
                                                .background(Color(0x1EFFFFFF), CircleShape)
                                                .border(1.dp, Color(0x18FFFFFF), CircleShape)
                                                .size(38.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy Ciphertext",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        // 2. Transfer payload to decrypt screen
                                        IconButton(
                                            onClick = {
                                                viewModel.transferToDecryptScreen(item.encryptedText)
                                            },
                                            modifier = Modifier
                                                .background(Color(0x1EFFFFFF), CircleShape)
                                                .border(1.dp, Color(0x18FFFFFF), CircleShape)
                                                .size(38.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowForward,
                                                contentDescription = "Transfer to Decrypt Tab",
                                                tint = Color(0xFF2DD4BF),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.weight(1f))

                                        // 3. Quick Decrypt Dialog Button
                                        Button(
                                            onClick = {
                                                selectedItemForDecrypt = item
                                                dialogPassword = ""
                                                dialogDecryptedResult = ""
                                                dialogErrorMsg = ""
                                                showDecryptDialog = true
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)), // Indigo
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            modifier = Modifier
                                                .height(36.dp)
                                                .testTag("quick_decrypt_button")
                                        ) {
                                            Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Quick Decrypt", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }

                                        // 4. Delete item from Vault
                                        IconButton(
                                            onClick = {
                                                viewModel.deleteConversation(item.id)
                                            },
                                            modifier = Modifier
                                                .background(Color(0x22F87171), CircleShape)
                                                .border(1.dp, Color(0x3BF87171), CircleShape)
                                                .size(38.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete from local Storage",
                                                tint = Color(0xFFF87171),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialect of Frosted glass Decrypt form Dialog
    if (showDecryptDialog && selectedItemForDecrypt != null) {
        val activeItem = selectedItemForDecrypt!!
        AlertDialog(
            onDismissRequest = {
                showDecryptDialog = false
                selectedItemForDecrypt = null
            },
            containerColor = Color(0xFF0F172A), // Dark Navy Slate solid backdrop for Dialog depth
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = true),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.VpnKey, contentDescription = null, tint = Color(0xFF6366F1))
                    Text("Quick Decrypt - ${activeItem.title}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (activeItem.passwordHint.isNotEmpty()) {
                        Text(
                            text = "Password Hint: ${activeItem.passwordHint}",
                            fontSize = 12.sp,
                            color = Color(0xFF2DD4BF),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Input Field for Password
                    OutlinedTextField(
                        value = dialogPassword,
                        onValueChange = {
                            dialogPassword = it
                            dialogErrorMsg = ""
                        },
                        placeholder = { Text("Enter decryption password", color = Color(0xFF475569)) },
                        singleLine = true,
                        visualTransformation = if (dialogPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { dialogPasswordVisible = !dialogPasswordVisible }) {
                                Icon(
                                    imageVector = if (dialogPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = Color(0x1AFFFFFF),
                            focusedContainerColor = Color(0x0CFFFFFF),
                            unfocusedContainerColor = Color(0x08FFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Error presentation if integrity decoding faults
                    if (dialogErrorMsg.isNotEmpty()) {
                        Text(
                            text = dialogErrorMsg,
                            color = Color(0xFFF87171),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Success Clear Text Display Area
                    if (dialogDecryptedResult.isNotEmpty()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0x12FFFFFF))
                        Text(
                            text = "Decrypted Original Content:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF2DD4BF)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xE6050B14), shape = RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0x18FFFFFF), shape = RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = dialogDecryptedResult,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }

                        // Copy decrypted buffer trigger
                        TextButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(dialogDecryptedResult))
                            },
                            modifier = Modifier.align(Alignment.End),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF2DD4BF))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy Message", fontSize = 12.sp, color = Color(0xFF2DD4BF), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                if (dialogDecryptedResult.isEmpty()) {
                    Button(
                        onClick = {
                            if (dialogPassword.isEmpty()) {
                                dialogErrorMsg = "Please input the password."
                                return@Button
                            }
                            try {
                                val clearText = CryptoHelper.decrypt(activeItem.encryptedText, dialogPassword.toCharArray())
                                dialogDecryptedResult = clearText
                                dialogErrorMsg = ""
                            } catch (e: Exception) {
                                dialogDecryptedResult = ""
                                dialogErrorMsg = "Decryption failed. Please verify password."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Decrypt", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            showDecryptDialog = false
                            selectedItemForDecrypt = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2DD4BF)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Close", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                if (dialogDecryptedResult.isEmpty()) {
                    TextButton(
                        onClick = {
                            showDecryptDialog = false
                            selectedItemForDecrypt = null
                        }
                    ) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                }
            }
        )
    }
}
