package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User


class FirestoreTripRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllTrips(): CollectionReference =
        firestoreDB.collection("users").document().collection("trips")

    fun getAllTripsFromUser(user: User): CollectionReference =
        firestoreDB.collection("users/${user.userId}/trips")

    fun getTrip(trip: Trip): CollectionReference =
        firestoreDB.collection("users/${trip.user.userId}/${trip.tripId}")


    // CREATE
    fun createTrip(trip: Trip) = firestoreDB.collection("users").document(trip.user.userId)
        .collection("trips").document(trip.tripId).set(trip)


    // UPDATE
    fun updateTrip(trip: Trip) = firestoreDB.collection("users").document(trip.user.userId)
        .collection("trips").document(trip.tripId).set(trip)

    // DELETE
    fun deleteTrip(trip: Trip) = firestoreDB.collection("users").document(trip.user.userId)
        .collection("trips").document(trip.tripId).delete()


    /*
    fun getTrip(trip: Trip): Task<DocumentSnapshot> = getUsersCollection()
        .document(trip.user.userId).collection("trips").document(trip.tripId).get()

    fun getAllTripsFromUser(user: User): Task<QuerySnapshot> = getUsersCollection()
        .document(user.userId).collection("trips").get()

    fun getAllTrips(): Task<QuerySnapshot> = getUsersCollection().document().collection("trips").get()
    */

}



