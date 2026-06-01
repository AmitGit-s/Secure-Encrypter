package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val encryptedText: String,
    val passwordHint: String,
    val timestamp: Long = System.currentTimeMillis()
)
