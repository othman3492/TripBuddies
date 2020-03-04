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
    var tripBuddiesList: MutableLiveData<List<String>> = MutableLiveData()
    var tripCitiesList: MutableLiveData<List<String>> = MutableLiveData()
    var tripPhotosList: MutableLiveData<List<String>> = MutableLiveData()


    // CREATE
    fun createTripIntoFirestore(trip: Trip) = tripRepository.createTrip(trip)
    fun addBuddyToTrip(tripId: String, userId: String) = tripRepository.addBuddyToTrip(tripId, userId)
    fun addCityToTrip(tripId: String, cityId: String) = tripRepository.addCityToTrip(tripId, cityId)
    fun addPhotoToTrip(tripId: String, photo: String) = tripRepository.addPhotoToTrip(tripId, photo)


    // UPDATE
    fun updateTripIntoFirestore(trip: Trip) = tripRepository.updateTrip(trip)


    // DELETE
    fun deleteTripFromFirestore(tripId: String) = tripRepository.deleteTrip(tripId)
    fun removeBuddyFromTrip(tripId: String, userId: String) = tripRepository.removeBuddyFromTrip(tripId, userId)
    fun removeCityFromTrip(tripId: String, cityId: String) = tripRepository.removeCityFromTrip(tripId, cityId)
    fun removePhotoFromTrip(tripId: String, photo: String) = tripRepository.removePhotoFromTrip(tripId, photo)



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



    // Retrieve trip's buddies list from Firestore and convert it to usable List<LiveData>
    fun getAllBuddiesFromTrip(tripId: String): LiveData<List<String>> {

        tripRepository.getAllBuddiesFromTrip(tripId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripBuddiesList.value = null
                return@EventListener
            }

            val savedTripBuddiesList: MutableList<String> = mutableListOf()
            for (doc in value!!) {

                val user = doc.id
                savedTripBuddiesList.add(user)
            }

            tripBuddiesList.value = savedTripBuddiesList
        })

        return tripBuddiesList
    }


    // Retrieve trip's destinations list from Firestore and convert it to usable List<LiveData>
    fun getAllCitiesFromTrip(tripId: String): LiveData<List<String>> {

        tripRepository.getAllCitiesFromTrip(tripId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripCitiesList.value = null
                return@EventListener
            }

            val savedTripCitiesList: MutableList<String> = mutableListOf()
            for (doc in value!!) {

                val city = doc.id
                savedTripCitiesList.add(city)
            }

            tripCitiesList.value = savedTripCitiesList
        })

        return tripCitiesList
    }



    // Retrieve trip's photos list from Firestore and convert it to usable List<LiveData>
    fun getPhotosFromTrip(tripId: String): LiveData<List<String>> {

        tripRepository.getAllPhotosFromTrip(tripId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->

            if (e != null) {

                tripPhotosList.value = null
                return@EventListener
            }

            val savedTripPhotosList: MutableList<String> = mutableListOf()
            for (doc in value!!) {

                val photo = doc.id
                savedTripPhotosList.add(photo)
            }

            tripPhotosList.value = savedTripPhotosList
        })

        return tripPhotosList
    }



}