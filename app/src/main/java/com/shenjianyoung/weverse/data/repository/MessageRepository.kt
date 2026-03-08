package com.shenjianyoung.weverse.data.repository

import com.shenjianyoung.weverse.data.model.Message
import com.shenjianyoung.weverse.db.dao.MessageDao

class MessageRepository(
    private val dao: MessageDao
) {

    suspend fun insert(message: Message) {
        dao.insert(message)
    }

    suspend fun getAll(): List<Message> {
        return dao.getAll()
    }

    suspend fun getByMemberId(memberId:Long):List<Message>{
        return dao.getByMemberId(memberId)
    }

    suspend fun delete(message: Message) {
        dao.delete(message)
    }

    suspend fun update(message: Message) {
        dao.update(message)
    }

}