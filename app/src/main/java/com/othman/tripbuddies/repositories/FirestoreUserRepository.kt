package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.models.User

class FirestoreUserRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()



    // GET COLLECTIONS
    fun getAllUsers(): CollectionReference = firestoreDB.collection("users")
    fun getUser(user: User): CollectionReference = firestoreDB.collection("users/${user.userId}")


    // CREATE
    fun createUser(user: User): Task<Void> = this.getAllUsers().document(user.userId).set(user)


    // UPDATE
    fun updateUser(user: User): Task<Void> = this.getAllUsers().document(user.userId).set(user)


    // DELETE
    fun deleteUser(user:User): Task<Void> = this.getAllUsers().document(user.userId).delete()
}