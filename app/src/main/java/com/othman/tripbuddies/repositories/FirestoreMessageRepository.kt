package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Message
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper


class FirestoreMessageRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllMessages() = firestoreDB.collection("messages")

    fun getMessage(messageId: String) = getAllMessages().document(messageId)



    // CREATE
    fun createMessage(message: Message): Task<Void> = getAllMessages().document(message.messageId).set(message)



    // UPDATE
    fun updateMessage(message: Message): Task<Void> = getAllMessages().document(message.messageId).set(message)



    // DELETE
    fun deleteMessage(messageId: String): Task<Void> = getAllMessages().document(messageId).delete()

}



