package com.othman.tripbuddies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.data.database.FirestoreDatabase
import com.othman.tripbuddies.data.model.Message
import kotlinx.coroutines.launch

class FirestoreMessageViewModel: ViewModel() {


    private val database = FirestoreDatabase()

    private val _message = MutableLiveData<Message>()
    private val message: LiveData<Message> = _message
    private val _messageList = MutableLiveData<List<Message>>()
    private val messageList: LiveData<List<Message>> = _messageList


    // Retrieve single message from Firestore and convert it to usable LiveData
    fun getMessage(messageId: String): LiveData<Message> {

        viewModelScope.launch {
            _message.value = database.getMessage(messageId)
        }
        return message
    }


    // Retrieve message list from Firestore and convert it to usable List<LiveData>
    fun getAllMessages(): LiveData<List<Message>> {

        viewModelScope.launch {
            _messageList.value = database.getAllMessages()
        }
        return messageList
    }


    // CREATE
    fun createMessage(message: Message) = database.createMessage(message)

    // UPDATE
    fun updateMessage(message: Message) = database.updateMessage(message)

    // DELETE
    fun deleteMessage(messageId: String) = database.deleteMessage(messageId)


}