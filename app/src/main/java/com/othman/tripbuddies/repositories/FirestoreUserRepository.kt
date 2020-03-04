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

    fun getAllTripsFromUser(userId: String) = getAllUsers().document(userId).collection("userTrips")

    fun getWishListFromUser(userId: String) = getAllUsers().document(userId).collection("userCities")

    fun getVisitedCitiesFromUser(userId: String) = getAllUsers().document(userId).collection("userVisitedCities")



    // CREATE
    fun createUser(user: User): Task<Void> = getAllUsers().document(user.userId).set(user)

    fun addTripToUser(userId: String, tripId: String): Task<Void> = getAllUsers()
        .document("$userId/userTrips/$tripId").set({tripId})

    fun addCityToWishList(userId: String, cityId: String): Task<Void> = getAllUsers()
        .document("$userId/userCities/$cityId").set({cityId})

    fun addVisitedCity(userId: String, cityId: String): Task<Void> = getAllUsers()
        .document("$userId/userVisitedCities/$cityId").set({cityId})



    // UPDATE
    fun updateUser(user: User): Task<Void> = getAllUsers().document(user.userId).set(user)



    // DELETE
    fun deleteUser(userId: String): Task<Void> = getAllUsers().document(userId).delete()

    fun removeTripFromUser(userId: String, tripId: String): Task<Void> = getAllUsers()
        .document("$userId/userTrips/$tripId").delete()

    fun removeCityFromWishList(userId: String, cityId: String): Task<Void> = getAllUsers()
        .document("$userId/userCities/$cityId").delete()

    fun removeVisitedCity(userId: String, cityId: String): Task<Void> = getAllUsers()
        .document("$userId/userVisitedCities/$cityId").delete()
}