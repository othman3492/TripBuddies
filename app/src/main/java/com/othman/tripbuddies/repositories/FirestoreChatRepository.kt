package com.othman.tripbuddies.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Message

class FirestoreChatRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllMessagesFromConversation(city: City): CollectionReference =
        firestoreDB.collection("cities").document(city.cityId).collection("messages")


    // CREATE
    fun createMessage(city: City, message: Message) = firestoreDB.collection("cities").document(city.cityId)
        .collection("messages").add(message)

}