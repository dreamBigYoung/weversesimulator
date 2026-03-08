package com.shenjianyoung.weverse.ui.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shenjianyoung.weverse.MyApplication
import com.shenjianyoung.weverse.data.model.Message
import com.shenjianyoung.weverse.data.repository.MessageRepository
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val resposity = MessageRepository(MyApplication.messageDao)

    private val __messageList = MutableLiveData<List<Message>>()

    val messageList: LiveData<List<Message>>
        get() = __messageList

    val latestMsg = MutableLiveData<Message>()

    val modifyMsg = MutableLiveData<Message>()

    fun addMessage(
        oriMsg: String,
        transMsg: String,
        timeStr: String,
        memberId: Long,
        imgPath: String
    ) {
        val message = Message(
            originalText = oriMsg,
            translatedText = transMsg,
            timestamp = timeStr,
            memberId = memberId,
            imgPath = imgPath
        )
        viewModelScope.launch {
            resposity.insert(message)
            latestMsg.postValue(message)
        }
    }

    fun updateMessage(
        msgId: Long,
        oriMsg: String,
        transMsg: String,
        timeStr: String,
        memberId: Long,
        imgPath: String
    ) {
        val message = Message(
            id = msgId,
            originalText = oriMsg,
            translatedText = transMsg,
            timestamp = timeStr,
            memberId = memberId,
            imgPath = imgPath
        )
        viewModelScope.launch {
            resposity.update(message)
            modifyMsg.postValue(message)
        }
    }


    fun fetchMessage(memberId: Long) {
        viewModelScope.launch {
            val all = resposity.getByMemberId(memberId)
            __messageList.postValue(all)
        }
    }
}