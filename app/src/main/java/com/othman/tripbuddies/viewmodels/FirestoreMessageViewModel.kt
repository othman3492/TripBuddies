package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.Message
import com.othman.tripbuddies.repositories.FirestoreMessageRepository

class FirestoreMessageViewModel: ViewModel() {


    private var messageRepository = FirestoreMessageRepository()


    // CREATE
    fun createMessage(message: Message) = messageRepository.createMessage(message)

    // UPDATE
    fun updateMessage(message: Message) = messageRepository.updateMessage(message)

    // DELETE
    fun deleteMessage(messageId: String) = messageRepository.deleteMessage(messageId)



    // Retrieve single message from Firestore and convert it to usable LiveData
    fun getMessage(messageId: String): LiveData<Message> {

        val message: MutableLiveData<Message> = MutableLiveData()
        messageRepository.getMessage(messageId).addSnapshotListener { doc, e ->
            if (e != null) {
                message.value = null
                return@addSnapshotListener
            }

            val savedMessage = doc!!.toObject(Message::class.java)

            message.value = savedMessage
        }

        return message
    }


    // Retrieve message list from Firestore and convert it to usable List<LiveData>
    fun getAllMessages(): LiveData<List<Message>> {

        val messageList: MutableLiveData<List<Message>> = MutableLiveData()
        messageRepository.getAllMessages().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                messageList.value = null
                return@EventListener
            }

            val savedMessageList: MutableList<Message> = mutableListOf()
            for (doc in value!!) {

                val message = doc.toObject(Message::class.java)
                savedMessageList.add(message)
            }

            messageList.value = savedMessageList
        })

        return messageList
    }







}