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

    fun getAllBuddiesFromTrip(tripId: String) = getAllTrips().document(tripId).collection("tripBuddies")

    fun getAllCitiesFromTrip(tripId: String) = getAllTrips().document(tripId).collection("tripCities")

    fun getAllPhotosFromTrip(tripId: String) = getAllTrips().document(tripId).collection("tripPhotos")


    // CREATE
    fun createTrip(trip: Trip): Task<Void> = getAllTrips().document(trip.tripId).set(trip)

    fun addBuddyToTrip(tripId: String, userId: String): Task<Void> =
        getAllTrips().document("$tripId/tripBuddies/$userId").set({userId})

    fun addCityToTrip(tripId: String, cityId: String): Task<Void> =
        getAllTrips().document("$tripId/tripCities/$cityId").set({cityId})

    fun addPhotoToTrip(tripId: String, photo: String): Task<Void> = getAllTrips()
        .document("$tripId/tripPhotos/$photo").set({photo})


    // UPDATE
    fun updateTrip(trip: Trip): Task<Void> = getAllTrips().document(trip.tripId).set(trip)



    // DELETE
    fun deleteTrip(tripId: String): Task<Void> = getAllTrips().document(tripId).delete()

    fun removeBuddyFromTrip(tripId: String, userId: String): Task<Void> =
        getAllTrips().document("$tripId/tripBuddies/$userId").delete()

    fun removeCityFromTrip(tripId: String, cityId: String): Task<Void> =
        getAllTrips().document("$tripId/tripCities/$cityId").delete()

    fun removePhotoFromTrip(tripId: String, photo: String): Task<Void> =
        getAllTrips().document("$tripId/tripPhotos/$photo").delete()

}



