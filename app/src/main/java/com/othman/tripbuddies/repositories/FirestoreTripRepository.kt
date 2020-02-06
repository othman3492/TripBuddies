package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User


class FirestoreTripRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllTrips(): CollectionReference =
        firestoreDB.collection("users").document().collection("trips")

    fun getAllTripsFromUser(userId: String): CollectionReference =
        firestoreDB.collection("users/$userId/trips")

    fun getTrip(trip: Trip): DocumentReference =
        firestoreDB.collection("users/${trip.user.userId}/trips").document(trip.tripId)


    // CREATE
    fun createTrip(trip: Trip) = firestoreDB.collection("users").document(trip.user.userId)
        .collection("trips").document("gfdgfdgdfhg").set(trip)


    // UPDATE
    fun updateTrip(trip: Trip) = firestoreDB.collection("users").document(trip.user.userId)
        .collection("trips").document(trip.tripId).set(trip)

    // DELETE
    fun deleteTrip(trip: Trip) = firestoreDB.collection("users").document(trip.user.userId)
        .collection("trips").document(trip.tripId).delete()


}



