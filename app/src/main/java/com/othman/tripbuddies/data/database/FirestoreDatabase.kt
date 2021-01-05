package com.othman.tripbuddies.data.database

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.othman.tripbuddies.data.model.City
import com.othman.tripbuddies.data.model.Message
import com.othman.tripbuddies.data.model.Trip
import com.othman.tripbuddies.data.model.User
import kotlinx.coroutines.tasks.await


class FirestoreDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val cityCollection = firestore.collection("cities")
    private val userCollection = firestore.collection("users")
    private val tripCollection = firestore.collection("messages")
    private val messageCollection = firestore.collection("messages")



    /*-----------------------------
    CITY
    ---------------------------- */

    // GET COLLECTIONS
    suspend fun getAllCities(): List<City> =
        cityCollection.get().await().toObjects(City::class.java)

    suspend fun getCity(cityId: String): City? =
        cityCollection.document(cityId).get().await().toObject(City::class.java)


    // CREATE
    fun createCity(city: City): Task<Void> = cityCollection.document(city.cityId).set(city)


    // UPDATE
    fun updateCity(city: City): Task<Void> = cityCollection.document(city.cityId).set(city)

    fun addUserToWishList(cityId: String, userId: String): Task<Void> =
        cityCollection.document(cityId).update("wishList", FieldValue.arrayUnion(userId))

    fun addTripToCity(cityId: String, tripId: String): Task<Void> =
        cityCollection.document(cityId).update("tripList", FieldValue.arrayUnion(tripId))

    fun addMessageToChat(cityId: String, messageId: String): Task<Void> =
        cityCollection.document(cityId).update("messagesList", FieldValue.arrayUnion(messageId))

    fun removeUserFromWishList(cityId: String, userId: String): Task<Void> =
        cityCollection.document(cityId).update("wishList", FieldValue.arrayRemove(userId))

    fun removeTripFromCity(cityId: String, tripId: String): Task<Void> =
        cityCollection.document(cityId).update("tripList", FieldValue.arrayRemove(tripId))

    fun removeMessageFromChat(cityId: String, messageId: String): Task<Void> =
        cityCollection.document(cityId).update("messagesList", FieldValue.arrayRemove(messageId))


    // DELETE
    fun deleteCity(cityId: String): Task<Void> = cityCollection.document(cityId).delete()



    /*-----------------------------
    USER
    ---------------------------- */

    // GET COLLECTIONS
    suspend fun getAllUsers(): List<User> =
        userCollection.get().await().toObjects(User::class.java)

    suspend fun getUser(userId: String): User? =
        userCollection.document(userId).get().await().toObject(User::class.java)


    // CREATE
    fun createUser(user: User): Task<Void> = userCollection.document(user.userId).set(user)


    // UPDATE
    fun updateUser(user: User): Task<Void> = userCollection.document(user.userId).set(user)

    fun addTripToUser(userId: String, tripId: String): Task<Void> =
        userCollection.document(userId).update("tripList", FieldValue.arrayUnion(tripId))

    fun addCityToWishList(userId: String, cityId: String): Task<Void> =
        userCollection.document(userId).update("wishList", FieldValue.arrayUnion(cityId))

    fun removeTripFromUser(userId: String, tripId: String): Task<Void> =
        userCollection.document(userId).update("tripList", FieldValue.arrayRemove(tripId))

    fun removeCityFromWishList(userId: String, cityId: String): Task<Void> =
        userCollection.document(userId).update("wishList", FieldValue.arrayRemove(cityId))


    // DELETE
    fun deleteUser(userId: String): Task<Void> = userCollection.document(userId).delete()



    /*-----------------------------
    TRIP
    ---------------------------- */

    // GET COLLECTIONS
    suspend fun getAllTrips(): List<Trip> =
        tripCollection.get().await().toObjects(Trip::class.java)

    suspend fun getTrip(tripId: String): Trip? =
        tripCollection.document(tripId).get().await().toObject(Trip::class.java)


    // CREATE
    fun createTrip(trip: Trip): Task<Void> = tripCollection.document(trip.tripId).set(trip)


    // UPDATE
    fun updateTrip(trip: Trip): Task<Void> = tripCollection.document(trip.tripId).set(trip)

    fun addBuddyToTrip(tripId: String, userId: String): Task<Void> =
        tripCollection.document(tripId).update("buddiesList", FieldValue.arrayUnion(userId))

    fun addCityToTrip(tripId: String, cityId: String): Task<Void> =
        tripCollection.document(tripId).update("destinationsList", FieldValue.arrayUnion(cityId))

    fun addPhotoToTrip(tripId: String, photo: String): Task<Void> =
        tripCollection.document(tripId).update("photosList", FieldValue.arrayUnion(photo))

    fun removeBuddyFromTrip(tripId: String, userId: String): Task<Void> =
        tripCollection.document(tripId).update("buddiesList", FieldValue.arrayRemove(userId))

    fun removeCityFromTrip(tripId: String, cityId: String): Task<Void> =
        tripCollection.document(tripId).update("destinationsList", FieldValue.arrayRemove(cityId))

    fun removePhotoFromTrip(tripId: String, photo: String): Task<Void> =
        tripCollection.document(tripId).update("photosList", FieldValue.arrayRemove(photo))


    // DELETE
    fun deleteTrip(tripId: String): Task<Void> = tripCollection.document(tripId).delete()



    /*-----------------------------
    MESSAGE
    ---------------------------- */

    // GET COLLECTIONS
    suspend fun getAllMessages(): List<Message> =
        messageCollection.get().await().toObjects(Message::class.java)

    suspend fun getMessage(messageId: String): Message? =
        messageCollection.document(messageId).get().await().toObject(Message::class.java)

    // CREATE
    fun createMessage(message: Message): Task<Void> = messageCollection.document(message.messageId).set(message)

    // UPDATE
    fun updateMessage(message: Message): Task<Void> = messageCollection.document(message.messageId).set(message)

    // DELETE
    fun deleteMessage(messageId: String): Task<Void> = messageCollection.document(messageId).delete()
}