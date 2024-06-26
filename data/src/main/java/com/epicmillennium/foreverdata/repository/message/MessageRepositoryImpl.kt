package com.epicmillennium.foreverdata.repository.message

import com.epicmillennium.fureverdomain.message.MessageRepository

class MessageRepositoryImpl(
    private val messageRemoteDataSource: MessageRemoteDataSource
) : MessageRepository {

    override fun getMessages(matchId: String) = messageRemoteDataSource.getMessages(matchId)

    override suspend fun addMessage(matchId: String, text: String) {
        messageRemoteDataSource.sendMessage(matchId, text)
    }
}