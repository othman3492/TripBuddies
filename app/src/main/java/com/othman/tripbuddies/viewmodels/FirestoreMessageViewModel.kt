package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.internal.Mutable
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Message
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.repositories.FirestoreCityRepository
import com.othman.tripbuddies.repositories.FirestoreMessageRepository
import com.othman.tripbuddies.repositories.FirestoreTripRepository
import java.util.concurrent.Executor

class FirestoreMessageViewModel: ViewModel() {


    var messageRepository = FirestoreMessageRepository()
    var message: MutableLiveData<Message> = MutableLiveData()
    var messageList: MutableLiveData<List<Message>> = MutableLiveData()


    // CREATE
    fun createMessage(message: Message) = messageRepository.createMessage(message)


    // UPDATE
    fun updateMessage(message: Message) = messageRepository.updateMessage(message)


    // DELETE
    fun deleteMessage(messageId: String) = messageRepository.deleteMessage(messageId)



    // Retrieve single message from Firestore and convert it to usable LiveData
    fun getMessage(messageId: String): LiveData<Message> {

        messageRepository.getMessage(messageId).addSnapshotListener { doc, e ->
            if (e != null) {
                this.message.value = null
                return@addSnapshotListener
            }

            val savedMessage = doc!!.toObject(Message::class.java)

            this.message.value = savedMessage
        }

        return this.message
    }


    // Retrieve message list from Firestore and convert it to usable List<LiveData>
    fun getAllMessages(): LiveData<List<Message>> {

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