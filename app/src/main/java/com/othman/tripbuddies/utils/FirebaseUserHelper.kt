package com.othman.tripbuddies.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User

object FirebaseUserHelper {


    val COLLECTION_NAME = "users"



    // COLLECTION REFERENCE
    private fun getUsersCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)


    // CREATE
    fun createUser(userId: String,
                   username: String,
                   presentation: String,
                   urlPicture: String,
                   urlCoverPicture: String,
                   tripList: MutableList<Trip>,
                   wishList: MutableList<Trip>) {

        val userToCreate = User(userId, username, presentation, urlPicture, urlCoverPicture, tripList, wishList)
        this.getUsersCollection().document(userId).set(userToCreate)
    }


    // GET
    fun getUser(userId: String): Task<DocumentSnapshot> = this.getUsersCollection().document(userId).get()
    fun getAllUsers(): Task<QuerySnapshot> = this.getUsersCollection().get()


    // UPDATE



    // DELETE
    fun deleteUser(userId: String) = this.getUsersCollection().document(userId).delete()
}