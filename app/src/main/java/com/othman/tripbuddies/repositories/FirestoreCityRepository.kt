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

    fun getAllVisitorsFromCity(cityId: String) = getAllCities().document(cityId).collection("cityVisitors")

    fun getWishListFromCity(cityId: String) = getAllCities().document(cityId).collection("cityWishList")

    fun getAllTripsFromCity(cityId: String) = getAllCities().document(cityId).collection("cityTrips")

    fun getAllMessagesFromChat(cityId: String) = getAllCities().document(cityId).collection("chatMessages")



    // CREATE
    fun createCity(city: City): Task<Void> = getAllCities().document(city.cityId).set(city)

    fun addVisitorToCity(cityId: String, userId: String): Task<Void> =
        getAllCities().document("$cityId/cityVisitors/$userId").set({cityId})

    fun addUserToWishList(cityId: String, userId: String): Task<Void> =
        getAllCities().document("$cityId/cityWishList/$userId").set({userId})

    fun addTripToCity(cityId: String, tripId: String): Task<Void> =
        getAllCities().document("$cityId/cityTrips/$tripId").set({tripId})

    fun createMessage(cityId: String, message: Message): Task<Void> =
        getAllCities().document("$cityId/chatMessages/${message.messageId}").set(message)


    // UPDATE
    fun updateCity(city: City): Task<Void> = getAllCities().document(city.cityId).set(city)


    // DELETE
    fun deleteCity(cityId: String): Task<Void> = getAllCities().document(cityId).delete()

    fun removeVisitorFromCity(cityId: String, userId: String): Task<Void> =
        getAllCities().document("$cityId/cityVisitors/$userId").delete()

    fun removeUserFromWishList(cityId: String, userId: String): Task<Void> =
        getAllCities().document("$cityId/cityWishList/$userId").delete()

    fun removeTripFromCity(cityId: String, tripId: String): Task<Void> =
        getAllCities().document("$cityId/cityTrips/$tripId").delete()

    fun deleteMessage(cityId: String, messageId: String): Task<Void> =
        getAllCities().document("$cityId/chatMessages/$messageId").delete()
}



