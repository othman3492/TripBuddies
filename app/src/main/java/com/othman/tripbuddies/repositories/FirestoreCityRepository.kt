package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Message
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper


class FirestoreCityRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllCities() = firestoreDB.collection("cities")

    fun getCity(cityId: String) = getAllCities().document(cityId)

    fun getAllVisitorsFromCity(cityId: String) = getAllCities().document(cityId).collection("visitors")

    fun getWishListFromCity(cityId: String) = getAllCities().document(cityId).collection("wishList")

    fun getAllTripsFromCity(cityId: String) = getAllCities().document(cityId).collection("cityTrips")

    fun getAllMessagesFromChat(cityId: String) = getAllCities().document(cityId).collection("chatMessages")



    // CREATE
    fun createCity(city: City): Task<Void> = getAllCities().document(city.cityId).set(city)

    fun addVisitorToCity(cityId: String, user: User): Task<Void> =
        getAllCities().document(cityId).collection("visitors").document(user.userId).set(user)

    fun addUserToWishList(cityId: String, user: User): Task<Void> =
        getAllCities().document(cityId).collection("wishList").document(user.userId).set(user)

    fun addTripToCity(cityId: String, trip: Trip): Task<Void> =
        getAllCities().document(cityId).collection("cityTrips").document(trip.tripId).set(trip)

    fun createMessage(cityId: String, message: Message): Task<Void> =
        getAllCities().document(cityId).collection("cityTrips").document(message.messageId).set(message)

    // UPDATE
    fun updateCity(city: City): Task<Void> = getAllCities().document(city.cityId).set(city)


    // DELETE
    fun deleteCity(cityId: String): Task<Void> = getAllCities().document(cityId).delete()

    fun removeVisitorFromCity(cityId: String, user: User): Task<Void> =
        getAllCities().document(cityId).collection("visitors").document(user.userId).delete()

    fun removeUserFromWishList(cityId: String, user: User): Task<Void> =
        getAllCities().document(cityId).collection("wishList").document(user.userId).delete()

    fun removeTripFromCity(cityId: String, trip: Trip): Task<Void> =
        getAllCities().document(cityId).collection("cityTrips").document(trip.tripId).delete()

    fun deleteMessage(cityId: String, message: Message): Task<Void> =
        getAllCities().document(cityId).collection("cityTrips").document(message.messageId).delete()
}



