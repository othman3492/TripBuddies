package com.othman.tripbuddies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.repositories.FirestoreTripRepository

class FirestoreTripViewModel: ViewModel() {


    var tripRepository = FirestoreTripRepository()
    var trip: MutableLiveData<Trip> = MutableLiveData()
    var tripList: MutableLiveData<List<Trip>> = MutableLiveData()


    // CREATE
    fun createTripIntoFirestore(trip: Trip) = tripRepository.createTrip(trip)


    // UPDATE
    fun updateTripIntoFirestore(trip: Trip) = tripRepository.updateTrip(trip)
    fun addBuddyToTrip(tripId: String, userId: String) = tripRepository.addBuddyToTrip(tripId, userId)
    fun addCityToTrip(tripId: String, cityId: String) = tripRepository.addCityToTrip(tripId, cityId)
    fun addPhotoToTrip(tripId: String, photo: String) = tripRepository.addPhotoToTrip(tripId, photo)
    fun removeBuddyFromTrip(tripId: String, userId: String) = tripRepository.removeBuddyFromTrip(tripId, userId)
    fun removeCityFromTrip(tripId: String, cityId: String) = tripRepository.removeCityFromTrip(tripId, cityId)
    fun removePhotoFromTrip(tripId: String, photo: String) = tripRepository.removePhotoFromTrip(tripId, photo)


    // DELETE
    fun deleteTripFromFirestore(tripId: String) = tripRepository.deleteTrip(tripId)



    // Retrieve single trip from Firestore and convert it to usable LiveData
    fun getTrip(tripId: String): LiveData<Trip> {

        tripRepository.getTrip(tripId).addSnapshotListener { doc, e ->
            if (e != null) {
                this.trip.value = null
                return@addSnapshotListener
            }

            val savedTrip = doc!!.toObject(Trip::class.java)

            this.trip.value = savedTrip
        }

        return this.trip
    }


    // Retrieve trip list from Firestore and convert it to usable List<LiveData>
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