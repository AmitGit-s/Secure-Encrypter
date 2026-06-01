package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto.CryptoHelper
import com.example.data.AppDatabase
import com.example.data.ConversationEntity
import com.example.data.ConversationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    ENCRYPT, DECRYPT, STORAGE
}

class ConversationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ConversationRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ConversationRepository(database.conversationDao())
    }

    // --- State: General Switch Tab ---
    var currentTab by mutableStateOf(AppTab.ENCRYPT)
        private set

    fun selectTab(tab: AppTab) {
        currentTab = tab
    }

    // --- State: Encrypt Section ---
    var encryptMessage by mutableStateOf("")
    var encryptPassword by mutableStateOf("")
    var encryptPasswordVisible by mutableStateOf(false)
    var encryptedResult by mutableStateOf("")
    var encryptTitle by mutableStateOf("")
    var encryptHint by mutableStateOf("")
    var encryptStatusMsg by mutableStateOf("")
    var encryptIsError by mutableStateOf(false)

    // --- State: Decrypt Section ---
    var decryptPayload by mutableStateOf("")
    var decryptPassword by mutableStateOf("")
    var decryptPasswordVisible by mutableStateOf(false)
    var decryptedResult by mutableStateOf("")
    var decryptStatusMsg by mutableStateOf("")
    var decryptIsError by mutableStateOf(false)

    // --- State: Local Room Persistence Storage ---
    val savedConversations: StateFlow<List<ConversationEntity>> = repository.allConversations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Action: Encrypt Message ---
    fun runEncryption() {
        encryptStatusMsg = ""
        encryptIsError = false
        if (encryptMessage.isBlank()) {
            encryptStatusMsg = "Please enter a message to encrypt."
            encryptIsError = true
            return
        }
        if (encryptPassword.isEmpty()) {
            encryptStatusMsg = "Please enter a security password."
            encryptIsError = true
            return
        }

        try {
            val result = CryptoHelper.encrypt(encryptMessage, encryptPassword.toCharArray())
            encryptedResult = result
            encryptStatusMsg = "Message encrypted successfully!"
            encryptIsError = false
        } catch (e: Exception) {
            encryptedResult = ""
            encryptStatusMsg = "Encryption failed: ${e.message}"
            encryptIsError = true
        }
    }

    // --- Action: Decrypt Message ---
    fun runDecryption() {
        decryptStatusMsg = ""
        decryptIsError = false
        decryptedResult = ""

        if (decryptPayload.isBlank()) {
            decryptStatusMsg = "Please enter or paste the encrypted payload."
            decryptIsError = true
            return
        }
        if (decryptPassword.isEmpty()) {
            decryptStatusMsg = "Please enter the password used to encrypt."
            decryptIsError = true
            return
        }

        try {
            val result = CryptoHelper.decrypt(decryptPayload, decryptPassword.toCharArray())
            decryptedResult = result
            decryptStatusMsg = "Decryption successful!"
            decryptIsError = false
        } catch (e: Exception) {
            decryptedResult = ""
            decryptStatusMsg = "Decryption failed. Ensure the password or payload is typed correctly."
            decryptIsError = true
        }
    }

    // --- Action: Save Crypt-Payload to Database ---
    fun saveToLocalStore() {
        if (encryptedResult.isBlank()) {
            encryptStatusMsg = "Nothing to save. Please encrypt a message first."
            encryptIsError = true
            return
        }

        val finalTitle = encryptTitle.trim().ifBlank { "Encrypted Note" }

        viewModelScope.launch {
            try {
                val entity = ConversationEntity(
                    title = finalTitle,
                    encryptedText = encryptedResult,
                    passwordHint = encryptHint.trim()
                )
                repository.insert(entity)
                encryptStatusMsg = "Note saved locally for secure storage!"
                encryptIsError = false
                // Reset save parameters
                encryptTitle = ""
                encryptHint = ""
            } catch (e: Exception) {
                encryptStatusMsg = "Failed to save locally: ${e.message}"
                encryptIsError = true
            }
        }
    }

    // --- Action: Delete From Database ---
    fun deleteConversation(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    // --- Action: Helper to auto-fill Decrypt Screen from Storage items ---
    fun transferToDecryptScreen(payload: String) {
        decryptPayload = payload
        decryptedResult = ""
        decryptStatusMsg = ""
        decryptIsError = false
        currentTab = AppTab.DECRYPT
    }

    // --- Resets ---
    fun resetEncryptSection() {
        encryptMessage = ""
        encryptPassword = ""
        encryptedResult = ""
        encryptTitle = ""
        encryptHint = ""
        encryptStatusMsg = ""
        encryptIsError = false
    }

    fun resetDecryptSection() {
        decryptPayload = ""
        decryptPassword = ""
        decryptedResult = ""
        decryptStatusMsg = ""
        decryptIsError = false
    }
}
