package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper


class FirestoreTripRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllTrips() = firestoreDB.collection("trips")

    fun getTrip(tripId: String) = getAllTrips().document(tripId)



    // CREATE
    fun createTrip(trip: Trip): Task<Void> = getAllTrips().document(trip.tripId).set(trip)



    // UPDATE
    fun updateTrip(trip: Trip): Task<Void> = getAllTrips().document(trip.tripId).set(trip)

    fun addBuddyToTrip(tripId: String, userId: String): Task<Void> =
        getAllTrips().document(tripId).update("buddiesList", FieldValue.arrayUnion(userId))

    fun addCityToTrip(tripId: String, cityId: String): Task<Void> =
        getAllTrips().document(tripId).update("destinationsList", FieldValue.arrayUnion(cityId))

    fun addPhotoToTrip(tripId: String, photo: String): Task<Void> =
        getAllTrips().document(tripId).update("photosList", FieldValue.arrayUnion(photo))

    fun removeBuddyFromTrip(tripId: String, userId: String): Task<Void> =
        getAllTrips().document(tripId).update("buddiesList", FieldValue.arrayRemove(userId))

    fun removeCityFromTrip(tripId: String, cityId: String): Task<Void> =
        getAllTrips().document(tripId).update("destinationsList", FieldValue.arrayRemove(cityId))

    fun removePhotoFromTrip(tripId: String, photo: String): Task<Void> =
        getAllTrips().document(tripId).update("photosList", FieldValue.arrayRemove(photo))





    // DELETE
    fun deleteTrip(tripId: String): Task<Void> = getAllTrips().document(tripId).delete()
}



