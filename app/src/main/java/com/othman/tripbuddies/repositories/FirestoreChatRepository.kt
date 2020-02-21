package com.othman.tripbuddies.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.models.Message

class FirestoreChatRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllMessagesFromConversation(cityId: String): CollectionReference =
        firestoreDB.collection("chatRooms").document(cityId).collection("messages")


    // CREATE
    fun createMessage(message: Message) = firestoreDB.collection("users").document(message.userId)
        .collection("messages").document(message.messageId).set(message)

}