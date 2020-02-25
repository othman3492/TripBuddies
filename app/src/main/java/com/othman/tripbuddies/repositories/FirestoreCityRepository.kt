package com.othman.tripbuddies.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper


class FirestoreCityRepository {


    private var firestoreDB = FirebaseFirestore.getInstance()


    // GET COLLECTIONS
    fun getAllCitiesFromUser(userId: String): CollectionReference =
        firestoreDB.collection("users").document(userId).collection("userCities")

    fun getAllCitiesFromTrip(trip: Trip): CollectionReference =
        firestoreDB.collection("users").document(trip.userId).collection("trips")
            .document(trip.tripId).collection("tripCities")

    // CREATE


    fun addCityToTrip(trip: Trip, city: City): Task<Void> = firestoreDB.collection("users")
        .document(trip.userId).collection("trips").document(trip.tripId).collection("tripCities")
        .document(city.cityId).set(city)

    // DELETE


    fun removeCityFromTrip(trip: Trip, city: City): Task<Void> = firestoreDB.collection("users")
        .document(trip.userId).collection("trips").document(trip.tripId).collection("tripCities")
        .document(city.cityId).set(city)

}



