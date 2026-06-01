package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawBehind

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: ConversationViewModel) {
    // Standard high-fidelity "Frosted Glass" parent mesh orbs container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // Background solid Slate-950 foundation
                drawRect(color = Color(0xFF020617))

                // Left top Indigo glow (indigo-600/20)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x3B6366F1), Color(0x006366F1)),
                        center = androidx.compose.ui.geometry.Offset(-size.width * 0.15f, -size.height * 0.05f),
                        radius = size.minDimension * 0.95f
                    )
                )

                // Right bottom Teal glow (teal-500/20)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x312DD4BF), Color(0x002DD4BF)),
                        center = androidx.compose.ui.geometry.Offset(size.width * 1.15f, size.height * 0.95f),
                        radius = size.minDimension * 0.85f
                    )
                )
            }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Logo Box container (Indigo 500)
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(
                                        Color(0xFF6366F1),
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "CipherVault",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    letterSpacing = (-0.2).sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "OFFLINE SECURE",
                                    fontSize = 9.sp,
                                    color = Color(0xFF2DD4BF), // Teal 400
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp
                                )
                            }
                        }
                    },
                    actions = {
                        // Action Quick Clear trigger
                        IconButton(
                            onClick = {
                                when (viewModel.currentTab) {
                                    AppTab.ENCRYPT -> viewModel.resetEncryptSection()
                                    AppTab.DECRYPT -> viewModel.resetDecryptSection()
                                    AppTab.STORAGE -> { /* No-op */ }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Screen State",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent, // Allows background mesh orbs to shine through
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                Column {
                    // Glass border dividing line
                    HorizontalDivider(color = Color(0x18FFFFFF), thickness = 1.dp)

                    NavigationBar(
                        containerColor = Color(0xF2050B14), // Slate-950 95% opacity
                        tonalElevation = 0.dp,
                        modifier = Modifier.testTag("main_nav_bar")
                    ) {
                        // Section 1: Encrypt
                        NavigationBarItem(
                            selected = viewModel.currentTab == AppTab.ENCRYPT,
                            onClick = { viewModel.selectTab(AppTab.ENCRYPT) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Encrypt Tab"
                                )
                            },
                            label = { Text("Encrypt", fontWeight = if (viewModel.currentTab == AppTab.ENCRYPT) FontWeight.Bold else FontWeight.Normal, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8),
                                indicatorColor = Color(0xFF6366F1) // Indigo Active Indicator
                            ),
                            modifier = Modifier.testTag("nav_item_encrypt")
                        )

                        // Section 2: Decrypt
                        NavigationBarItem(
                            selected = viewModel.currentTab == AppTab.DECRYPT,
                            onClick = { viewModel.selectTab(AppTab.DECRYPT) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = "Decrypt Tab"
                                )
                            },
                            label = { Text("Decrypt", fontWeight = if (viewModel.currentTab == AppTab.DECRYPT) FontWeight.Bold else FontWeight.Normal, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = Color(0xFF2DD4BF),
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8),
                                indicatorColor = Color(0xFF2DD4BF) // Teal Active Indicator
                            ),
                            modifier = Modifier.testTag("nav_item_decrypt")
                        )

                        // Section 3: Saved local vault list
                        NavigationBarItem(
                            selected = viewModel.currentTab == AppTab.STORAGE,
                            onClick = { viewModel.selectTab(AppTab.STORAGE) },
                            icon = {
                                Icon(
                                    imageVector = if (viewModel.currentTab == AppTab.STORAGE) Icons.Default.FolderSpecial else Icons.Default.Folder,
                                    contentDescription = "Saved Vault Tab"
                                )
                            },
                            label = { Text("Vault", fontWeight = if (viewModel.currentTab == AppTab.STORAGE) FontWeight.Bold else FontWeight.Normal, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8),
                                indicatorColor = Color(0xFF6366F1)
                            ),
                            modifier = Modifier.testTag("nav_item_storage")
                        )
                    }
                }
            },
            containerColor = Color.Transparent // Make scaffold transparent to display the background orbs
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Silk state transition animations
                AnimatedContent(
                    targetState = viewModel.currentTab,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(150)) + scaleIn(initialScale = 0.98f, animationSpec = tween(150)))
                            .togetherWith(fadeOut(animationSpec = tween(100)))
                    },
                    label = "TabTransition"
                ) { targetTab ->
                    when (targetTab) {
                        AppTab.ENCRYPT -> EncryptSection(viewModel)
                        AppTab.DECRYPT -> DecryptSection(viewModel)
                        AppTab.STORAGE -> StorageSection(viewModel)
                    }
                }
            }
        }
    }
}
