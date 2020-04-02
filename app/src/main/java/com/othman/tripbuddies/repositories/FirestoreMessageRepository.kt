package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.models.Message


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



