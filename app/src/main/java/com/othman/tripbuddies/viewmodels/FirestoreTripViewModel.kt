package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.repositories.FirestoreTripRepository
import java.util.concurrent.Executor

class FirestoreTripViewModel: ViewModel() {


    var tripRepository = FirestoreTripRepository()
    var trip: MutableLiveData<Trip> = MutableLiveData()
    var userTripList: MutableLiveData<List<Trip>> = MutableLiveData()
    var cityTripList: MutableLiveData<List<Trip>> = MutableLiveData()
    var tripList: MutableLiveData<List<Trip>> = MutableLiveData()


    // CREATE
    fun createTripIntoFirestore(trip: Trip) = tripRepository.createTrip(trip)


    // UPDATE
    fun updateTripIntoFirestore(trip: Trip) = tripRepository.updateTrip(trip)


    // DELETE
    fun deleteTripFromFirestore(trip: Trip) = tripRepository.deleteTrip(trip)



    // Retrieve single trip from Firestore and convert it to usable LiveData
    fun getTrip(trip: Trip): LiveData<Trip> {

        tripRepository.getTrip(trip).addSnapshotListener { doc, e ->
            if (e != null) {
                this.trip.value = null
                return@addSnapshotListener
            }

            val savedTrip = doc!!.toObject(Trip::class.java)

            this.trip.value = savedTrip
        }

        return this.trip
    }



    // Retrieve user's trip list from Firestore and convert it to usable List<LiveData>
    fun getAllTripsFromUser(userId: String): LiveData<List<Trip>> {

        tripRepository.getAllTripsFromUser(userId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                userTripList.value = null
                return@EventListener
            }

            val savedUserTripList: MutableList<Trip> = mutableListOf()
            for (doc in value!!) {

                val trip = doc.toObject(Trip::class.java)
                savedUserTripList.add(trip)
            }

            userTripList.value = savedUserTripList
        })

        return userTripList
    }


    // Retrieve city's trip list from Firestore and convert it to usable List<LiveData>
    /*fun getAllTripsFromCity(cityId: String): LiveData<List<Trip>> {

        tripRepository.getAllTrips().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                cityTripList.value = null
                return@EventListener
            }

            val savedCityTripList: MutableList<Trip> = mutableListOf()
            for (doc in value!!) {

                val trip = doc.toObject(Trip::class.java)

                savedCityTripList.add(trip)
            }

            userTripList.value = savedUserTripList
        })

        return userTripList
    }*/



    // Retrieve user list from Firestore and convert it to usable List<LiveData>
    fun getAllTrips(): LiveData<List<Trip>> {

        tripRepository.getAllTrips().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripList.value = null
                return@EventListener
            }

            val savedTripList: MutableList<Trip> = mutableListOf()
            for (doc in value!!) {

                val trip = doc.toObject(Trip::class.java)
                savedTripList.add(trip)
            }

            tripList.value = savedTripList
        })

        return tripList
    }



}