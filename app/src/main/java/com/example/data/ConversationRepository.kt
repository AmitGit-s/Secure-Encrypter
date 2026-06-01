package com.example.data

import kotlinx.coroutines.flow.Flow

class ConversationRepository(private val conversationDao: ConversationDao) {
    val allConversations: Flow<List<ConversationEntity>> = conversationDao.getAllConversations()

    suspend fun insert(conversation: ConversationEntity) {
        conversationDao.insertConversation(conversation)
    }

    suspend fun deleteById(id: Int) {
        conversationDao.deleteConversationById(id)
    }

    suspend fun getById(id: Int): ConversationEntity? {
        return conversationDao.getConversationById(id)
    }
}
