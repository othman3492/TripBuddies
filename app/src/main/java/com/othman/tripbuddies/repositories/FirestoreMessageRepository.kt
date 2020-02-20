package com.othman.tripbuddies.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.models.Message

class FirestoreMessageRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllMessagesFromConversation(userId: String): CollectionReference =
        firestoreDB.collection("users").document(userId).collection("messages")

    fun getMessage(message: Message): DocumentReference =
        firestoreDB.collection("users/${message.userId}/messages").document()


    // CREATE
    fun createMessage(message: Message) = firestoreDB.collection("users").document(message.userId)
        .collection("messages").document(message.messageId).set(message)

}