package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper


class FirestoreTripRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllTrips(): CollectionReference = firestoreDB.collection("trips")

    fun getAllTripsFromUser(userId: String): CollectionReference =
        firestoreDB.collection("users").document(userId).collection("trips")

    fun getTrip(trip: Trip): DocumentReference =
        firestoreDB.collection("users/${trip.userId}/trips").document()


    // CREATE
    fun createTrip(trip: Trip) = firestoreDB.collection("users").document(trip.userId)
        .collection("trips").document(trip.tripId).set(trip)

        // UPDATE
    fun updateTrip(trip: Trip) = firestoreDB.collection("users").document(trip.userId)
        .collection("trips").document(trip.tripId).set(trip)

    // DELETE
    fun deleteTrip(trip: Trip) = firestoreDB.collection("users").document(trip.userId)
        .collection("trips").document(trip.tripId).delete()


}



