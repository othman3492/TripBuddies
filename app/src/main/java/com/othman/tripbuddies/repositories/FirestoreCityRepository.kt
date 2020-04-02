package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.models.City


class FirestoreCityRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllCities() = firestoreDB.collection("cities")

    fun getCity(cityId: String) = getAllCities().document(cityId)



    // CREATE
    fun createCity(city: City): Task<Void> = getAllCities().document(city.cityId).set(city)



    // UPDATE
    fun updateCity(city: City): Task<Void> = getAllCities().document(city.cityId).set(city)

    fun addUserToWishList(cityId: String, userId: String): Task<Void> =
        getAllCities().document(cityId).update("wishList", FieldValue.arrayUnion(userId))

    fun addTripToCity(cityId: String, tripId: String): Task<Void> =
        getAllCities().document(cityId).update("tripList", FieldValue.arrayUnion(tripId))

    fun addMessageToChat(cityId: String, messageId: String): Task<Void> =
        getAllCities().document(cityId).update("messagesList", FieldValue.arrayUnion(messageId))

    fun removeUserFromWishList(cityId: String, userId: String): Task<Void> =
        getAllCities().document(cityId).update("wishList", FieldValue.arrayRemove(userId))

    fun removeTripFromCity(cityId: String, tripId: String): Task<Void> =
        getAllCities().document(cityId).update("tripList", FieldValue.arrayRemove(tripId))

    fun removeMessageFromChat(cityId: String, messageId: String): Task<Void> =
        getAllCities().document(cityId).update("messagesList", FieldValue.arrayRemove(messageId))



    // DELETE
    fun deleteCity(cityId: String): Task<Void> = getAllCities().document(cityId).delete()

}



