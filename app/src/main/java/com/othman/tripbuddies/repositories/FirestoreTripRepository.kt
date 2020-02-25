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

    fun getAllBuddiesFromTrip(tripId: String) = getAllTrips().document(tripId)
        .collection("buddies")

    fun getAllCitiesFromTrip(tripId: String) = getAllTrips().document(tripId)
        .collection("tripCities")

    fun getPhotosFromTrip(tripId: String) = getAllTrips().document(tripId)
        .collection("photos")

    // CREATE
    fun createTrip(trip: Trip): Task<Void> = getAllTrips().document(trip.tripId).set(trip)


    // UPDATE
    fun updateTrip(trip: Trip): Task<Void> = getAllTrips().document(trip.tripId).set(trip)

    fun addBuddyToTrip(tripId: String, user: User): Task<Void> =
        getAllTrips().document(tripId).collection("buddies").document(user.userId).set(user)

    fun removeBuddyFromTrip(tripId: String, user: User): Task<Void> =
        getAllTrips().document(tripId).collection("buddies").document(user.userId).delete()

    fun addCityToTrip(tripId: String, city: City): Task<Void> =
        getAllTrips().document(tripId).collection("tripCities").document(city.cityId).set(city)

    fun removeCityFromTrip(tripId: String, city: City): Task<Void> =
        getAllTrips().document(tripId).collection("tripCities").document(city.cityId).delete()

    fun addPhotoToTrip(tripId: String, path: String): Task<Void> =
        getAllTrips().document(tripId).collection("photos").document(path).set(path)

    fun removePhotoFromTrip(tripId: String, path: String): Task<Void> =
        getAllTrips().document(tripId).collection("photos").document(path).delete()


    // DELETE
    fun deleteTrip(trip: Trip): Task<Void> = getAllTrips().document(trip.tripId).delete()

}



