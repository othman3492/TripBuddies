package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.User

class FirestoreUserRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()



    // GET COLLECTIONS
    fun getAllUsers(): CollectionReference = firestoreDB.collection("users")
    fun getUser(userId: String): DocumentReference = firestoreDB.collection("users").document(userId)


    // CREATE
    fun createUser(user: User): Task<Void> = this.getAllUsers().document(user.userId).set(user)


    // UPDATE
    fun updateUser(user: User): Task<Void> = this.getAllUsers().document(user.userId).set(user)

    fun addCity(user: User, city: City): Task<Void> = this.getAllUsers().document(user.userId)
        .collection("userCities").document(city.cityId).set(city)




    // DELETE
    fun deleteUser(userId: String): Task<Void> = this.getAllUsers().document(userId).delete()
}