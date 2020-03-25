package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User


class FirestoreUserRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllUsers() = firestoreDB.collection("users")

    fun getUser(userId: String) = getAllUsers().document(userId)



    // CREATE
    fun createUser(user: User): Task<Void> = getAllUsers().document(user.userId).set(user)



    // UPDATE
    fun updateUser(user: User): Task<Void> = getAllUsers().document(user.userId).set(user)

    fun addTripToUser(userId: String, tripId: String): Task<Void> =
        getAllUsers().document(userId).update("tripList", FieldValue.arrayUnion(tripId))

    fun addCityToWishList(userId: String, cityId: String): Task<Void> =
        getAllUsers().document(userId).update("wishList", FieldValue.arrayUnion(cityId))

    fun removeTripFromUser(userId: String, tripId: String): Task<Void> =
        getAllUsers().document(userId).update("tripList", FieldValue.arrayRemove(tripId))

    fun removeCityFromWishList(userId: String, cityId: String): Task<Void> =
        getAllUsers().document(userId).update("wishList", FieldValue.arrayRemove(cityId))



    // DELETE
    fun deleteUser(userId: String): Task<Void> = getAllUsers().document(userId).delete()
}