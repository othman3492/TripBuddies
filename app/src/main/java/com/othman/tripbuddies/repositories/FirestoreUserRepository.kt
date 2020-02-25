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

    fun getAllTripsFromUser(userId: String) = getAllUsers().document(userId)
        .collection("trips")

    fun getWishListFromUser(userId: String) = getAllUsers().document(userId)
        .collection("userCities")


    // CREATE
    fun createUser(user: User) = getAllUsers().document(user.userId).set(user)


    // UPDATE
    fun updateUser(user: User) = getAllUsers().document(user.userId).set(user)

    fun addTripToUser(userId: String, trip: Trip): Task<Void> =
        getAllUsers().document(userId).collection("trips").document(trip.tripId).set(trip)

    fun removeTripFromUser(userId: String, trip: Trip): Task<Void> =
        getAllUsers().document(userId).collection("trips").document(trip.tripId).delete()

    fun addCityToWishList(userId: String, city: City): Task<Void> =
        getAllUsers().document(userId).collection("userCities").document(city.cityId).set(city)

    fun removeCityFromWishList(userId: String, city: City): Task<Void> = getAllUsers()
        .document(userId).collection("userCities").document(city.cityId).delete()


    // DELETE
    fun deleteUser(userId: String): Task<Void> = this.getAllUsers().document(userId).delete()
}